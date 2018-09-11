package com.wheel.daniel.okhttputils.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/7 17:12
 */
public class ThreadMgrUtils {
    private static final String TAG = ThreadMgrUtils.class.getSimpleName();
    volatile private static ThreadMgrUtils threadMgr;
    private static Object mMgrLock = new Object();
    //本地线程池
    private ThreadPoolExecutor mLocalExecutor;
    //所有云端网络请求线程池
    private ThreadPoolExecutor mNetworkExecutor;
    //下载首页运营图片，不能用上面网络线程池，会阻塞数据
    private ThreadPoolExecutor mDownResExecutor;
    //下载首页素材，不能用上面网络线程池，会阻塞数据
    private ThreadPoolExecutor mDownMaskExecutor;

    /**
     * 执行本地任务
     *
     * @param task
     */
    public static Future executeLocalTask(Runnable task) {
        initMgrIfNeed();
        return threadMgr.runLocalTask(task);
    }

    public static Future executeLocalTask(Runnable task, long delay) {
        initMgrIfNeed();
        return threadMgr.runLocalTask(task, delay);
    }

    /**
     * 执行网络任务
     *
     * @param task
     */
    public static Future executeNetworkTask(Runnable task) {
        //NLog.d(TAG, "executeNetworkTask task = %s", task);
        initMgrIfNeed();
        return threadMgr.runNetworkTask(task);
    }

    public static Future executeDownResTask(Runnable task) {
        //NLog.d(TAG, "executeNetworkTask task = %s", task);
        initMgrIfNeed();
        return threadMgr.runDownResTask(task);
    }

    public static Future executeDownMaskTask(Runnable task) {
        initMgrIfNeed();
        return threadMgr.runDownMaskTask(task);
    }

    public static Future executeNetworkTask(Runnable task, long delay) {
        //NLog.d(TAG, "executeNetworkTask delay task = %s", task);
        initMgrIfNeed();
        return threadMgr.runNetworkTask(task, delay);
    }

    public static Executor getNetworkExecutor() {
        initMgrIfNeed();
        return threadMgr.getNetExecutor();
    }

    public static Executor getLocalExecutor() {
        initMgrIfNeed();
        return threadMgr.getLocExecutor();
    }

    private Executor getNetExecutor() {
        return mNetworkExecutor;
    }

    private Executor getDownResExecutor() {
        return mDownResExecutor;
    }

    private Executor getLocExecutor() {
        return mLocalExecutor;
    }

    public static void destroy() {
        if (threadMgr != null) {
            threadMgr.close();
            threadMgr = null;
        }
    }

    private static void initMgrIfNeed() {
        if (threadMgr == null) {
            synchronized (mMgrLock) {
                if (threadMgr == null) {
                    threadMgr = new ThreadMgrUtils();
                }
            }
        }
    }

    private ThreadMgrUtils() {
        initExecutors();
    }

    private void initExecutors() {
        mLocalExecutor = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadMgrUtils.ThreadMgrFactory("thread-local"));
        mNetworkExecutor = new ThreadPoolExecutor(2, 5, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadMgrUtils.ThreadMgrFactory("thread-network"));
        mDownResExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadMgrUtils.ThreadMgrFactory("thread-downres"));
        mDownMaskExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadMgrUtils.ThreadMgrFactory("thread-downmask"));
    }

    private Future runLocalTask(Runnable runnable) {
        return mLocalExecutor.submit(runnable);
    }

    private Future runLocalTask(Runnable runnable, long delay) {
        return mLocalExecutor.submit(new ThreadMgrUtils.DelayRunnable(runnable, delay));
    }

    private Future runNetworkTask(Runnable runnable) {
        return mNetworkExecutor.submit(runnable);
    }

    private Future runDownResTask(Runnable runnable) {
        return mDownResExecutor.submit(runnable);
    }

    private Future runDownMaskTask(Runnable runnable) {
        return mDownMaskExecutor.submit(runnable);
    }

    private Future runNetworkTask(Runnable runnable, long delay) {
        return mNetworkExecutor.submit(new ThreadMgrUtils.DelayRunnable(runnable, delay));
    }

    private void close() {
        if (mLocalExecutor != null) {
            mLocalExecutor.shutdown();
            mLocalExecutor.setCorePoolSize(0);
            mLocalExecutor = null;
        }

        if (mNetworkExecutor != null) {
            mNetworkExecutor.shutdown();
            mNetworkExecutor.setCorePoolSize(0);
            mNetworkExecutor = null;
        }
        if (mDownResExecutor != null) {
            mDownResExecutor.shutdown();
            mDownResExecutor.setCorePoolSize(0);
            mDownResExecutor = null;
        }
        if (mDownMaskExecutor != null) {
            mDownMaskExecutor.shutdown();
            mDownMaskExecutor.setCorePoolSize(0);
            mDownMaskExecutor = null;
        }
    }

    class ThreadMgrFactory implements ThreadFactory {
        private String threadName;

        public ThreadMgrFactory(String name) {
            threadName = name;
        }

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread newThread = new Thread(r);
            newThread.setName(threadName + mCount.getAndIncrement());
            return newThread;
        }
    }

    /**
     * 延时执行runnable
     */
    class DelayRunnable implements Runnable {
        private Runnable delayTask;
        private long delayTime;

        public DelayRunnable(Runnable task, long delay) {
            delayTask = task;
            delayTime = delay;
        }

        @Override
        public void run() {
            if (delayTime >= 0) {
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {

                }
            }

            if (delayTask != null) {
                delayTask.run();
            }
        }
    }
}
