package com.bawp.virustracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bawp.virustracker.workers.CleanupWorker;
import com.bawp.virustracker.workers.DownloadJsonWorker;

import java.util.List;

public class VirusViewModel extends AndroidViewModel {

    public static final String STRING_URL = "api_url";
    private WorkManager workManager;
    private LiveData<List<WorkInfo>> savedWorkInfo;
    private String outputData;

    public VirusViewModel(@NonNull @org.jetbrains.annotations.NotNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        savedWorkInfo = workManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT);
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo(){
        return savedWorkInfo;
    }

    void downloadJson() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        WorkContinuation continuation = workManager.beginUniqueWork(
                Constants.JSON_PROCESSING_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker.class));

        OneTimeWorkRequest download = new OneTimeWorkRequest.Builder(DownloadJsonWorker.class)
                .setConstraints(constraints)
                .addTag(Constants.TAG_OUTPUT)
                .setInputData(createInputUrl()).build();

        continuation = continuation.then(download);
        continuation.enqueue();


    }

    void cancelWork() {
        workManager.cancelUniqueWork(Constants.JSON_PROCESSING_WORK_NAME);
    }

    private Data createInputUrl() {
        Data.Builder builder = new Data.Builder();
        builder.putString(Constants.STRING_URL, Constants.API_URL);
        return builder.build();
    }
}
