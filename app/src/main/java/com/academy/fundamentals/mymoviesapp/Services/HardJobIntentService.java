package com.academy.fundamentals.mymoviesapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

/**
 * This service running on background (worker) thread and maintain a task queue with intent created
 * for each task in the queue. When the queue is empty, its mechanism knows to finish.
 */
public class HardJobIntentService extends IntentService {

    private boolean isDestroyed = true;

    public HardJobIntentService() {
        super("HardJobIntentService");
    }

    /**
     *  Ordinarily this function involve performing a task that takes some time to complete such as
     *  downloading a large file or playing audio.
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isDestroyed = false;
        for (int i = 0; i <= 100 && !isDestroyed; i++) {
            SystemClock.sleep(100);

            // The intent we’ll broadcast when the requested work is finished.
            // We’ll put our output text into this intent.
            // Any component within the app that has registered a receiver with the same
            // action in its filter will be able to process this broadcast intent.
            Intent broadcastIntent = new Intent(BGServiceActivity.PROGRESS_UPDATE_ACTION);
            broadcastIntent.putExtra(BGServiceActivity.
                    BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }
}
