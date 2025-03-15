package com.h2;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.DeleteDbFiles;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbTool {
    /**
     * 每个 ThreadPoolExecutor 还维护一些基本统计信息，例如已完成任务的数量
     * Executors.newCachedThreadPool 无界线程池，具有自动线程回收功能
     * Executors.newFixedThreadPool 固定大小的线程池
     * Executors.newSingleThreadExecutor 单个后台线程
     */
    private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 10, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    /**
     * 在 withConnection 方法中，通过调用 isOnExecutor.get() 来检查当前线程是否是由 executorService 创建的。
     * 如果是，那么直接调用 withConnectionInternal(callable) 来执行任务。
     * 如果不是，则将任务提交到 executorService 线程池中执行。
     * isOnExecutor 的作用：
     * 线程标识: 它为线程池中的线程提供了一个标识，这样可以在运行时检查当前代码是否正在由线程池中的线程执行。
     * 避免死锁: 在某些情况下，如果任务已经在线程池的线程中运行，直接在同一个线程中执行可以避免死锁或线程竞争问题。
     * 性能优化: 如果当前线程已经是由线程池创建的，那么直接执行任务可以避免额外的线程创建和上下文切换开销。
     * 总的来说，isOnExecutor 提供了一种机制，使得方法可以根据当前线程的上下文来决定如何执行任务，这有助于优化资源使用和避免潜在的问题
     */
    private final ThreadLocal<Boolean> isOnExecutor = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private JdbcConnectionPool connectionPool;
    private JdbcDataSource dataSource;
    private static final String JDBC_URL = "jdbc:h2:~/h2datadb"; //~用户主目录,文件存储形式，也可以是"jdbc:h2:file:~/kuradb",windows目录为C:\Users\User\
    private static final String JDBC_USER="username";
    private static final String JDBC_PASSWORD="password";
    private static final Pattern JDBC_URL_PARSE_PATTERN = Pattern.compile("jdbc:([^:]+):(([^:]+):)?([^;]*)(;.*)?");
    private boolean isInMemory;
    private boolean isFileBased;
    private boolean isZipBased;
    private boolean isRemote;
    private String baseUrl;
    private String dbDirectory;
    private String dbName;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> checkpointTask;
    private ScheduledFuture<?> defragTask;

    public <T> T withConnection(ConnectionCallable<T> callable) throws SQLException {

        if (this.isOnExecutor.get()) {
            return withConnectionInternal(callable);
        }

        final Future<T> result = this.executorService.submit(() -> withConnectionInternal(callable));

        try {
            return result.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            } else if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new IllegalStateException(e);
        }

    }

    private <T> T withConnectionInternal(ConnectionCallable<T> callable) throws SQLException {
        final Lock executorlock = this.rwLock.readLock();
        executorlock.lock();
        Connection connection = null;
        try {
            connection = getConnectionInternal();
            return callable.call(connection);
        } catch (final SQLException e) {
            System.out.println("Db operation failed");
            rollback(connection);
            throw e;
        } finally {
            close(connection);
            executorlock.unlock();
        }
    }
    public void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error during Connection closing"+e.getMessage());
        }
    }
    public void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error during Connection rollback."+e.getMessage());
        }
    }

    private Connection getConnectionInternal() throws SQLException {
        if (this.connectionPool == null) {
            throw new SQLException("Database instance not initialized");
        }

        Connection conn = null;
        try {
            conn = this.connectionPool.getConnection();
        } catch (SQLException e) {
            System.out.println("Error getting connection"+e);
            throw e;
        }
        return conn;
    }

    private void executeInternal(String sql) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnectionInternal();
            stmt = conn.createStatement();
            stmt.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw e;
        } finally {
            close(stmt);
            close(conn);
        }
    }
    public void close(Statement... stmts) {
        if (stmts != null) {
            for (Statement stmt : stmts) {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Error during Statement closing"+e.getMessage());
                }
            }
        }
    }

    private void openConnectionPool() {
        System.out.println("Opening database："+JDBC_URL);

        this.dataSource = new JdbcDataSource();

        this.dataSource.setURL(JDBC_URL);
        this.dataSource.setUser(JDBC_USER);
        this.dataSource.setPassword(JDBC_PASSWORD);

        this.connectionPool = JdbcConnectionPool.create(this.dataSource);

        openDatabase(true);
    }

    private void openDatabase(boolean deleteDbOnError) {
        Connection conn = null;
        try {
            conn = getConnectionInternal();
        } catch (SQLException e) {
            System.out.println("Failed to open database"+e.getMessage());
            if (deleteDbOnError && isFileBased) {
                System.out.println("Deleting database files...");
                deleteDbFiles();
                System.out.println("Deleting database files...done");
                openDatabase(false);
            } else {
                disposeConnectionPool();
                throw new RuntimeException(e);
            }
        } finally {
            close(conn);
        }
    }

    private void deleteDbFiles() {
        try {
            DeleteDbFiles.execute(dbDirectory, dbName, false);
        } catch (Exception e) {
            System.out.println("Failed to remove DB files"+e);
        }
    }

    private void computeUrlParts() {
        final Matcher jdbcUrlMatcher = JDBC_URL_PARSE_PATTERN.matcher(JDBC_URL);

        if (!jdbcUrlMatcher.matches()) {
            throw new IllegalArgumentException("Invalid DB URL");
        }

        String driver = jdbcUrlMatcher.group(1);
        if (driver == null || !"h2".equals(driver)) {
            throw new IllegalArgumentException("JDBC driver must be h2");
        }

        String protocol = jdbcUrlMatcher.group(3);
        String url = jdbcUrlMatcher.group(4);
        if (protocol == null && ".".equals(url)) {
            // jdbc:h2:. is a shorthand for jdbc:h2:mem:
            protocol = "mem";
            url = "";
        } else {
            if (protocol == null) {
                protocol = "file";
            }
            if (url == null) {
                url = "";
            }
        }

        if ("mem".equals(protocol)) {
            this.isInMemory = true;
        } else if ("file".equals(protocol)) {
            this.isFileBased = true;
        } else if ("zip".equals(protocol)) {
            this.isZipBased = true;
        } else {
            this.isRemote = true;
        }

        this.baseUrl = "jdbc:h2:" + protocol + ':' + url;

        if (this.isFileBased) {
            File file = new File(url);
            this.dbDirectory = file.getParent();
            if (this.dbDirectory == null) {
                this.dbDirectory = ".";
            }
            this.dbName = file.getName();
        }
    }

    private void disposeConnectionPool() {
        if (this.connectionPool != null) {
            this.connectionPool.dispose();//Closes all unused pooled connections
            this.connectionPool = null;
        }
    }

    private void restartCheckpointTask() {
        stopCheckpointTask();
        this.checkpointTask = this.executor.scheduleWithFixedDelay(new CheckpointTask(), 900, 900,TimeUnit.SECONDS);
    }

    private void stopCheckpointTask() {
        if (this.checkpointTask != null) {
            this.checkpointTask.cancel(false);
            this.checkpointTask = null;
        }
    }

    private void restartDefragTask() {
        stopDefragTask();
        this.defragTask = this.executor.scheduleWithFixedDelay(new DefragTask(), 15, 15,TimeUnit.MINUTES);
    }

    private void stopDefragTask() {
        if (this.defragTask != null) {
            this.defragTask.cancel(false);
            this.defragTask = null;
        }
    }
    private class CheckpointTask implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("performing checkpoint...");
                executeInternal("CHECKPOINT SYNC"); //将数据刷新到磁盘，并强制将所有系统缓冲区写入底层设备
                System.out.println("performing checkpoint...done");
            } catch (final SQLException e) {
                System.out.println("checkpoint failed"+e.getMessage());
            }
        }
    }

    private class DefragTask implements Runnable {
        private void shutdownDefrag() throws SQLException {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = DbTool.this.dataSource.getConnection();
                stmt = conn.createStatement();
                stmt.execute("SHUTDOWN DEFRAG"); //用于关闭数据库并进行碎片整理
            } finally {
                close(stmt);
                close(conn);
            }
        }

        @Override
        public void run() {
            final Lock lock = DbTool.this.rwLock.writeLock();
            lock.lock();
            try {
                System.out.println("shutting down and defragmenting db...");
                shutdownDefrag();
                disposeConnectionPool();
                openConnectionPool();
                System.out.println("shutting down and defragmenting db...done");
            } catch (final Exception e) {
                System.out.println("failed to shutdown and defrag db"+e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    private void shutdownDb() throws SQLException {
        if (this.connectionPool == null) {
            return;
        }

        stopDefragTask();
        stopCheckpointTask();

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            stmt.execute("SHUTDOWN");
        } finally {
            close(stmt);
            close(conn);
        }
        disposeConnectionPool();
    }
    private void setParameters() throws SQLException {

        /**
         * H2 数据库支持几种不同的跟踪级别，包括 OFF、ERROR、INFO 和 DEBUG
         * 每个级别对应一个数字：OFF 为 0，ERROR 为 1（默认级别），INFO 为 2，DEBUG 为 3
         * 例如，要设置文件跟踪级别为 DEBUG，您可以这样设置 URL：jdbc:h2:./test;TRACELEVELFILE=3
         * 也可以在 H2 数据库运行时通过执行 SQL 命令来更改跟踪级别，例如 SET TRACE_LEVEL_FILE 3
         * 如果不希望生成 .trace.db 日志文件，可以将日志级别设置为 OFF。这可以通过在数据库连接 URL 中添加参数 TRACELEVELFILE=0 来实现。例如，jdbc:h2:./test;TRACELEVELFILE=0 将关闭文件跟踪
         */

        executeInternal("SET TRACE_LEVEL_FILE 0");


        /**
         * 命令用于设置大对象（LOBs，包括 CLOB 和 BLOB）的最大长度
         * 根据 H2 数据库的官方文档，默认情况下，大型 LOB 对象是单独于主表数据存储的，而小型 LOB 对象则可以存储在记录内。这个阈值可以通过 MAX_LENGTH_INPLACE_LOB 进行设置。
         * 具体来说，SET MAX_LENGTH_INPLACE_LOB 命令允许你指定一个最大长度，超过这个长度的 LOB 对象将不会存储在记录内，而是单独存储。这个命令的默认值是 256 字节
         * 使用这个命令可以帮助优化数据库的性能，特别是当你知道你的应用程序将处理大量的小型 LOB 数据时。通过将小型 LOB 数据存储在记录内，可以减少磁盘 I/O 操作，从而提高效率
         */
        if (this.isInMemory) {
            executeInternal("SET MAX_LENGTH_INPLACE_LOB " + 2000000000);
        }

        this.connectionPool.setMaxConnections(10);
    }
    private void init() {
        final Lock lock = this.rwLock.writeLock();
        lock.lock();
        this.executor = Executors.newSingleThreadScheduledExecutor();
        final ThreadFactory defaultFactory = this.executorService.getThreadFactory();
        final AtomicInteger threadNumber = new AtomicInteger();
        this.executorService.setThreadFactory(r -> {
            final Thread result = defaultFactory.newThread(() -> {
                this.isOnExecutor.set(true);
                r.run();
            });
            result.setName("DbTool" + "_" + threadNumber.getAndIncrement());
            return result;
        });
        try {
            if (this.connectionPool == null) {
                openConnectionPool();
            }
            setParameters();
            if (this.isFileBased) {
                restartCheckpointTask(); //强制 H2 数据库立即将内存中的数据刷新到磁盘
                restartDefragTask(); //用于关闭数据库并进行碎片整理
            }
            this.executorService.setMaximumPoolSize(10);
            System.out.println("updating...done");
        } catch (Exception e) {
            disposeConnectionPool();
            stopCheckpointTask();
            System.out.println("Database initialization failed"+ e.getMessage());
        } finally {
            lock.unlock();
        }
    }
    @FunctionalInterface
    public interface ConnectionCallable<T> {
        public T call(Connection connection) throws SQLException;
    }

    private String create_table_sql;
    private String insert_sql;
    private String select_sql;
    private String delete_sql;
    private String tableName;
    public String sanitizeSql(String tableName) {
        return "\"" + tableName + "\"";
    }
    private void init_sql(String tableName) {
        this.tableName=this.sanitizeSql(tableName);
        this.create_table_sql = "create table if not exists " + this.tableName
                +"(id integer generated always as identity primary key, ycData VARCHAR,yxData VARCHAR,funcCode integer,createdOn timestamp);";
        this.insert_sql = "insert into " + this.tableName + "(ycData,yxData,funcCode,createdOn) values (?,?,?,?)";
        this.select_sql = "select id,ycData,yxData,funcCode,createdOn from "+this.tableName +" where funcCode=?";
        this.delete_sql = "delete from "+this.tableName +" where id=?";
    }
    public static void main(String[] args) throws Exception {
        //SET TRACE_LEVEL_FILE 0   非文件存储
        //SET MAX_LENGTH_INPLACE_LOB 2000000000 内存存储
        //CHECKPOINT SYNC 刷新数据到磁盘，立即发起一次磁盘刷新，确保所有系统缓冲区中的数据被写入到磁盘上
        DbTool dbTool = new DbTool();
        dbTool.init();
        dbTool.init_sql("com.btiot.kbox");
        dbTool.withConnection(c -> {
            try (final PreparedStatement stmt = c.prepareStatement(dbTool.create_table_sql)) {
                stmt.execute();
                c.commit();
                return (Void) null;
            }
        });

        dbTool.withConnection(c -> {
            int result = -1;
            final Timestamp now = new Timestamp(new Date().getTime());
            try (PreparedStatement pstmt = c.prepareStatement(dbTool.insert_sql, new String[] { "id" })) {
                pstmt.setString(1, "ycData");
                pstmt.setString(2, "yxData");
                pstmt.setInt(3, 4);
                pstmt.setTimestamp(4, now, Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")));

                pstmt.execute();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
            c.commit();
            return result;
        });
        dbTool.withConnection(c -> {
            Integer[] params =new Integer[]{2,3};
            try (PreparedStatement stmt = c.prepareStatement(dbTool.select_sql)) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        stmt.setInt(1 + i, params[i]);
                    }
                }
                try (final ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        rs.getString("ycData");
                    }
                }
            }
            return null;
        });

        dbTool.withConnection(c -> {
            int id=1;
            try (final PreparedStatement stmt = c.prepareStatement(dbTool.delete_sql)) {
                stmt.setInt(1, id);

                stmt.execute();
                c.commit();
                return (Void) null;
            }
        });
    }
}
