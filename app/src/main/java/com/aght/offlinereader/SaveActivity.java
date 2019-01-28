package com.aght.offlinereader;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aght.offlinereader.downloader.DownloadJobService;

public class SaveActivity extends Activity {

    private static final String TAG = SaveActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        handleIntent(intent, action, type);
    }

    private void handleIntent(Intent intent, String action, String type) {
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                final String url = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (url != null) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    startDownloadService(url);
                    Toast.makeText(this, "Already Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startDownloadService(String url) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("url", url);

        ComponentName componentName = new ComponentName(this, DownloadJobService.class);

        // TODO: Change the jobId to be unique when there are multiple jobs
        JobInfo jobInfo = new JobInfo.Builder(0, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setExtras(bundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(jobInfo);

        finish();
    }
}
