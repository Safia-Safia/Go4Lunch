package com.safia.go4lunch.notification;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class Notification extends Worker {
    private final static String WORK_NAME = "noti_work";
    private final static int NOTIFICATION_REPEAT = 24;

    public Notification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }

   /*public static void scheduleOrCancelWork() {
        if (HiSettingsHelper.getInstance().isNotiTaskEnabled()) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest request
                    = new PeriodicWorkRequest
                    .Builder(Notification.class, NOTIFICATION_REPEAT, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build();
            WorkManager.getInstance()
                    .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);
        } else {
            WorkManager.getInstance().cancelUniqueWork(WORK_NAME);
        }
    }*/
}