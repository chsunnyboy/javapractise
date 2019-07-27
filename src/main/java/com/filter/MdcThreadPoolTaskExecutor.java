package com.filter;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//final private boolean useFixedContext;
    //final private Map<String, String> fixedContext;

    /**
     * Pool where task threads take MDC from the submitting thread.
     */
    public static MdcThreadPoolTaskExecutor newWithInheritedMdc() {
        return new MdcThreadPoolTaskExecutor();
    }

    private MdcThreadPoolTaskExecutor() {
        //setCorePoolSize(corePoolSize);
        //setMaxPoolSize(maximumPoolSize);
        //setKeepAliveSeconds((int) unit.toSeconds(keepAliveTime));
        //setQueueCapacity(queueCapacity);
        //this.fixedContext = fixedContext;
        //useFixedContext = (fixedContext != null);
    }

    private Map<String, String> getContextForTask() {
        //return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    	return MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, getContextForTask()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, getContextForTask()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(wrap(task, getContextForTask()));
    }

    private static <T> Callable<T> wrap(final Callable<T> task, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    private static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}

