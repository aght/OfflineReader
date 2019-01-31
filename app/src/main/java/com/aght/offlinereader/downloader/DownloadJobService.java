package com.aght.offlinereader.downloader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;

public class DownloadJobService extends JobService {

    private final String TAG = DownloadJobService.class.getSimpleName();

    private Runnable downloadTask;
    private Handler handler;
    private Downloader downloader;

    @Override
    public boolean onStartJob(final JobParameters parameters) {
        handler = new Handler();

        downloadTask = new Runnable() {
            public void run() {
                downloader = new Downloader(parameters.getExtras().getString("url")) {
                    @Override
                    public void onDownloadFinish() {
                        super.onDownloadFinish();
                        handler.removeCallbacks(downloadTask);
                        jobFinished(parameters, false);
                    }
                };

                downloader.execute();
            }
        };

        handler.post(downloadTask);

        return true; // Continue running
    }

    @Override
    public boolean onStopJob(JobParameters parameters) {
        return false; // Do not reschedule the job
    }
}
