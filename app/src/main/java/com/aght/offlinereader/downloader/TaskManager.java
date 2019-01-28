package com.aght.offlinereader.downloader;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 4;
    private static final int KEEP_ALIVE_TIME = 50;

    private static TaskManager taskManager;

    private ThreadPoolExecutor taskThreadPool;
    private BlockingQueue<Runnable> taskQueue;

    private TaskManager() {
        taskQueue = new LinkedBlockingDeque<>();
        taskThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                taskQueue);
    }

    public static TaskManager getInstance() {
        if (taskManager == null) {
            taskManager = new TaskManager();
        }

        return taskManager;
    }

    public void runTask(Runnable task) {
        taskThreadPool.execute(task);
    }
}
