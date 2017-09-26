package com.ksdc.hongyangtest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liangkun on 2017/9/14 0014.
 */

public class ThreadPool {
    private static final int MAX_THREAD_SIZE = 15;
    private static ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(MAX_THREAD_SIZE);

    public static void execute(Runnable runnable) {
        mFixedThreadPool.execute(runnable);
    }
}
