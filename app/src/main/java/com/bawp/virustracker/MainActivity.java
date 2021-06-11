package com.bawp.virustracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.WorkInfo;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private VirusViewModel virusViewModel;
    private ProgressBar progressBar;
    private TextView infected, deaths, recovered, countries, newCases, newDeaths, loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infected = findViewById(R.id.infected);
        deaths = findViewById(R.id.deaths);
        recovered = findViewById(R.id.recovered);
        countries = findViewById(R.id.countries);
        newCases = findViewById(R.id.new_cases);
        newDeaths = findViewById(R.id.new_deaths);
        progressBar = findViewById(R.id.progressBar);
        loading = findViewById(R.id.loading);

        virusViewModel = new ViewModelProvider.AndroidViewModelFactory((Application)
                getApplicationContext())
                .create(VirusViewModel.class);
        getVirusStatus();
    }

    private void getVirusStatus() {
        virusViewModel.getOutputWorkInfo().observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos == null || workInfos.isEmpty()) {
                    return;
                }
                WorkInfo workInfo = workInfos.get(0);
                boolean finished = workInfo.getState().isFinished();

                if (!finished) {
                    showWorkInProgress();
                } else {
                    //Log.i("anisham", "anisham showWorkdone");
                    Data outputData = workInfo.getOutputData();
                    String outputDataString = outputData.getString(Constants.DATA_OUTPUT);

                    if (!TextUtils.isEmpty(outputDataString)) {
                        virusViewModel.setOutputData(outputDataString);
                    }
                    populateUi();
                }
            }
        });

        virusViewModel.downloadJson();
    }

    private void populateUi() {
        String outputData = virusViewModel.getOutputData();
        try {
            JSONArray jsonArray = new JSONArray(outputData);
            JSONObject res = jsonArray.getJSONObject(0);
            Log.i(TAG, "populateUi: " + jsonArray);

            deaths.setText(String.format(getString(R.string.deaths),
                    res.getLong("death")));
            infected.setText(String.format(getString(R.string.infected),
                    res.getLong("positive")));
            recovered.setText(String.format(getString(R.string.recovered),
                    res.getLong("negative")));

            newCases.setText(String.format(getString(R.string.new_cases),
                    res.getLong("negativeIncrease")));

            newDeaths.setText(String.format(getString(R.string.new_deaths),
                    res.getLong("positiveIncrease")));

            countries.setText(String.format(getString(R.string.countries),
                    res.getLong("totalTestResultsIncrease")));

            processingDone();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void processingDone() {
        progressBar.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    private void showWorkInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.virus_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                getVirusStatus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}