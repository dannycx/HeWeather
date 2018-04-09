package com.danny.heweather.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by derik on 17-12-7.
 */

public class AppExecutors {
    private static final int THREAD_COUNT = 3;
    private Executor mDiskIO;
    private Executor mNetworkIO;
    private Executor mMainThread;

    AppExecutors(Executor diskIO, Executor networkO, Executor mainThread) {
        mDiskIO = diskIO;
        mNetworkIO = networkO;
        mMainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExector(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new MainThreadExecutor());
    }


    public Executor getDiskIO() {
        return mDiskIO;
    }

    public Executor getNetworkIO() {
        return mNetworkIO;
    }

    public Executor getMainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }

}
