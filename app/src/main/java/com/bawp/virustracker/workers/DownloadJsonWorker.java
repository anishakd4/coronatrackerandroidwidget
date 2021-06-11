package com.bawp.virustracker.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bawp.virustracker.Constants;

public class DownloadJsonWorker extends Worker {
    public DownloadJsonWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String inputUrl = getInputData().getString(Constants.STRING_URL);
        Data outputData = new Data.Builder().putString(Constants.DATA_OUTPUT, VirusWorkerUtils.processJson(inputUrl)).build();

        return Result.success(outputData);
    }
}
