package cn.monkeyapp.mavd.common.manage;

import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 线程池管理类
 *
 * @author Corbett Zhang
 */
public class ThreadPoolManager {

    /**
     * 核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
     */
    private static final int CORE_POOL_SIZE = 10;
    private static ThreadPoolExecutor threadPoolExecutor = null;
    private static ScheduledExecutorService scheduledExecutorService = null;
    private volatile static ThreadPoolManager manager = null;
    /**
     * 任务缓存队列，用来存放等待执行的任务
     */
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();


    public ThreadPoolManager() {
    }

    public static ThreadPoolManager getInstance() {
        if (manager == null) {
            synchronized (ThreadPoolManager.class) {
                if (manager == null) {
                    manager = new ThreadPoolManager();
                }
            }
        }
        return manager;
    }

    private ThreadPoolExecutor getThreadPoolExecutor() {
        if (isThreadServiceEnable()) {
            threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, 50, 0L, TimeUnit.MICROSECONDS, workQueue);
        }
        return threadPoolExecutor;
    }

    /**
     * 添加一个任务
     *
     * @param runnable 任务
     */
    public void addThreadExecutor(Runnable runnable) {
        getThreadPoolExecutor().submit(runnable);
    }

    public void shutDownThreadPool() {
        if (isThreadServiceEnable()) {
            getThreadPoolExecutor().shutdown();
        }
    }

    public void shutDownNowThreadPool() {
        if (isThreadServiceEnable()) {
            getThreadPoolExecutor().shutdownNow();
        }
    }

    private boolean isThreadServiceEnable() {
        return threadPoolExecutor == null
                || threadPoolExecutor.isShutdown()
                || threadPoolExecutor.isTerminated();
    }


    public ScheduledExecutorService getScheduledExecutorService() {
        if (isScheduledServiceEnable()) {
            scheduledExecutorService = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
        }
        return scheduledExecutorService;
    }

    /**
     * 执行循环任务
     *
     * @param timerTask    任务
     * @param initialDelay 初始延迟
     * @param period       时间周期
     * @param timeUnit     时间单位
     * @return 任务执行结果
     */
    public ScheduledFuture<?> addScheduledExecutor(TimerTask timerTask, long initialDelay, long period, TimeUnit timeUnit) {
        return getScheduledExecutorService().scheduleAtFixedRate(timerTask, initialDelay, period, timeUnit);
    }

    /**
     * 执行循环任务
     *
     * @param runnable 任务
     * @param delay    延迟时间
     * @param timeUnit 时间单位
     * @return 任务执行结果
     */
    public ScheduledFuture<?> addDelayScheduledExecutor(Runnable runnable, long delay, TimeUnit timeUnit) {
        return getScheduledExecutorService().schedule(runnable, delay, timeUnit);
    }

    /**
     * 停止线程，先前提交的任务将会被工作线程执行，新的线程将会被拒绝。这个方法不会等待提交的任务执行完，我们可以用awaitTermination来等待任务执行完。
     */
    public void shutDownScheduledExecutor() {
        if (isScheduledServiceEnable()) {
            getScheduledExecutorService().shutdown();
        }
    }

    /**
     * 停止线程
     */
    public void shutDownNowScheduledExecutor() {
        if (isScheduledServiceEnable()) {
            getScheduledExecutorService().shutdownNow();
        }
    }

    /**
     * 线程是否是启动状态
     *
     * @return false为启动状态
     */
    private boolean isScheduledServiceEnable() {
        return scheduledExecutorService == null
                || scheduledExecutorService.isShutdown()
                || scheduledExecutorService.isTerminated();
    }

}