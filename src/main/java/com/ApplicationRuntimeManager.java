package com;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ServerSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
/**
 * 获取运行时系统内存使用状况，饼用socket获取状态信息
 * @author Administrator
 *
 */
public final class ApplicationRuntimeManager implements ApplicationListener<ApplicationReadyEvent> {
	final Logger logger = LoggerFactory.getLogger(getClass());

	int port = 7777;

	long interval = 1000L;

	boolean started = false;

	ObjectMapper mapper;

	Object lock = new Object();

	RuntimeInfo runtimeInfo = new RuntimeInfo();

	String runtimeFilePath = "log" + File.separator + "saas.runtime";

	String version;

	String branch;

	public ApplicationRuntimeManager() {
	}

	public ApplicationRuntimeManager(int port) {
		this.port = port;
	}

	public void startup() throws Exception {
		mapper = new ObjectMapper();

		Thread collectThread = new Thread(new Runnable() {
			public void run() {
				try {
					for (;;) {
						synchronized (lock) {
							runtimeInfo.collect();
						}
						FileWriter writer = new FileWriter(runtimeFilePath, false);
						String json = runtimeInfoAsJson();
						writer.write(json);
						writer.close();
						try {
							Thread.sleep(interval);
						} catch (Exception localException) {
						}
					}
				} catch (Throwable t) {
					logger.error("runtime info collect error", t);
				}

			}
		}, "RuntimeInfoCollector");
		collectThread.setDaemon(true);
		collectThread.setPriority(1);
		collectThread.start();

		Thread serverThread = new Thread(new Runnable() {
			public void run() {
				try {
					BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(3);
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
						public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							logger.warn("{} is rejected", r);
						}
					};
					ThreadPoolExecutor threadpool = new ThreadPoolExecutor(1, 3, 20L, TimeUnit.SECONDS, workQueue,
							threadFactory, rejectedExecutionHandler);

					ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(port);
					logger.info("RuntimeServer startup at port:{}", Integer.valueOf(port));
					try {
						for (;;) {
							Socket socket = server.accept();
							logger.debug("{} connected.", socket);
							threadpool.execute(new ApplicationRuntimeManager.RuntimeProcessor(socket));
						}
					} catch (Throwable t) {
						logger.error("error", t);
					}
				} catch (Throwable t) {
					logger.error("runtime server startup error", t);
				}
			}
		}, "RuntimeServer");
		serverThread.setDaemon(true);
		serverThread.setPriority(1);
		serverThread.start();
	}

	
	public RuntimeInfo runtimeInfo() {
		synchronized (this.lock) {
			return this.runtimeInfo;
		}
	}

	public String runtimeInfoAsJson() throws Exception {
		RuntimeInfo info = runtimeInfo();
		String json = mapper.writeValueAsString(info);
		return json;
	}

	public void onApplicationEvent(ApplicationReadyEvent event) {
		started = true;
	}

	public final class RuntimeInfo implements Serializable {
		private static final long serialVersionUID = 7096484274956992312L;
		private long timestamp;
		private boolean started = false;

		private String version;

		private String branch;

		private long totalMemory;

		private long freeMemory;

		private long maxMemory;

		private int processorCount;
		private String vmName;
		private long upTime;
		private long startTime;
		private ApplicationRuntimeManager.MemoryInfo heapMemoryUsage;
		private ApplicationRuntimeManager.MemoryInfo nonHeapMemoryUsage;
		private int threadCount;
		private int loadedClassCount;
		private double systemLoadAverage;
		@JsonIgnore
		private Runtime runtime;
		@JsonIgnore
		private RuntimeMXBean runtimeMXBean;
		@JsonIgnore
		private MemoryMXBean memoryMXBean;
		@JsonIgnore
		private ThreadMXBean threadMXBean;
		@JsonIgnore
		private ClassLoadingMXBean classLoadingMXBean;
		@JsonIgnore
		private OperatingSystemMXBean osMXBean;

		public RuntimeInfo() {
			runtime = Runtime.getRuntime();
			runtimeMXBean = ManagementFactory.getRuntimeMXBean();
			memoryMXBean = ManagementFactory.getMemoryMXBean();
			threadMXBean = ManagementFactory.getThreadMXBean();
			classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
			osMXBean = ManagementFactory.getOperatingSystemMXBean();
		}

		public RuntimeInfo collect() {
			this.timestamp = System.currentTimeMillis();
			this.started = started;

			this.version = version;
			this.branch =  branch;

			this.totalMemory = this.runtime.totalMemory();
			this.freeMemory = this.runtime.freeMemory();
			this.maxMemory = this.runtime.maxMemory();
			this.processorCount = this.runtime.availableProcessors();

			this.vmName = this.runtimeMXBean.getVmName();
			this.upTime = this.runtimeMXBean.getUptime();
			this.startTime = this.runtimeMXBean.getStartTime();

			this.heapMemoryUsage = createMemoryInfo(memoryMXBean.getHeapMemoryUsage());
			this.nonHeapMemoryUsage = createMemoryInfo(memoryMXBean.getNonHeapMemoryUsage());

			this.threadCount = threadMXBean.getThreadCount();

			this.loadedClassCount = classLoadingMXBean.getLoadedClassCount();

			this.systemLoadAverage = osMXBean.getSystemLoadAverage();

			return this;
		}

		public MemoryInfo createMemoryInfo(MemoryUsage usage) {
			MemoryInfo info = new MemoryInfo();
			info.setInit(usage.getInit());
			info.setUsed(usage.getUsed());
			info.setCommitted(usage.getCommitted());
			info.setMax(usage.getMax());
			return info;
		}

		public int getLoadedClassCount() {
			return loadedClassCount;
		}

		public void setLoadedClassCount(int loadedClassCount) {
			this.loadedClassCount = loadedClassCount;
		}

		public double getSystemLoadAverage() {
			return systemLoadAverage;
		}

		public void setSystemLoadAverage(double systemLoadAverage) {
			this.systemLoadAverage = systemLoadAverage;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public boolean isStarted() {
			return started;
		}

		public void setStarted(boolean started) {
			this.started = started;
		}

		public long getTotalMemory() {
			return totalMemory;
		}

		public void setTotalMemory(long totalMemory) {
			this.totalMemory = totalMemory;
		}

		public long getFreeMemory() {
			return freeMemory;
		}

		public void setFreeMemory(long freeMemory) {
			this.freeMemory = freeMemory;
		}

		public long getMaxMemory() {
			return maxMemory;
		}

		public void setMaxMemory(long maxMemory) {
			this.maxMemory = maxMemory;
		}

		public int getProcessorCount() {
			return processorCount;
		}

		public void setProcessorCount(int processorCount) {
			this.processorCount = processorCount;
		}

		public String getVmName() {
			return vmName;
		}

		public void setVmName(String vmName) {
			this.vmName = vmName;
		}

		public long getUpTime() {
			return upTime;
		}

		public void setUpTime(long upTime) {
			this.upTime = upTime;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public ApplicationRuntimeManager.MemoryInfo getHeapMemoryUsage() {
			return heapMemoryUsage;
		}

		public void setHeapMemoryUsage(ApplicationRuntimeManager.MemoryInfo heapMemoryUsage) {
			this.heapMemoryUsage = heapMemoryUsage;
		}

		public ApplicationRuntimeManager.MemoryInfo getNonHeapMemoryUsage() {
			return nonHeapMemoryUsage;
		}

		public void setNonHeapMemoryUsage(ApplicationRuntimeManager.MemoryInfo nonHeapMemoryUsage) {
			this.nonHeapMemoryUsage = nonHeapMemoryUsage;
		}

		public int getThreadCount() {
			return threadCount;
		}

		public void setThreadCount(int threadCount) {
			this.threadCount = threadCount;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}
	}

	final class MemoryInfo implements Serializable {
		private static final long serialVersionUID = -2222983876634686210L;

		private long init;

		private long used;

		private long committed;
		private long max;

		public MemoryInfo() {
		}

		public long getInit() {
			return init;
		}

		public long getUsed() {
			return used;
		}

		public long getCommitted() {
			return committed;
		}

		public long getMax() {
			return max;
		}

		public void setInit(long init) {
			this.init = init;
		}

		public void setUsed(long used) {
			this.used = used;
		}

		public void setCommitted(long committed) {
			this.committed = committed;
		}

		public void setMax(long max) {
			this.max = max;
		}
	}

	final class RuntimeProcessor implements Runnable {
		private Socket socket;

		public RuntimeProcessor(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				String command = dis.readUTF();
				if ("runtime".equals(command)) {
					String json = runtimeInfoAsJson();
					dos.writeUTF(json);
				} else {
					dos.writeUTF("unkonwn command");
				}
			} catch (Throwable t) {
				logger.error("error", t);
				try {
					if ((socket != null) && (!socket.isClosed())) {
						socket.close();
					}
				} catch (IOException e) {
					logger.error("error", e);
				}
			}
		}
	}
}