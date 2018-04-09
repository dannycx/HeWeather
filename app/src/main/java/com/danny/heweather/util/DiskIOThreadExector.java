package com.danny.heweather.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by derik on 17-12-7.
 */

public class DiskIOThreadExector implements Executor {
    private final Executor dislIO;

    public DiskIOThreadExector() {
        this.dislIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        dislIO.execute(runnable);
    }
}
