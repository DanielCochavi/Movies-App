package com.academy.fundamentals.mymoviesapp.Threads;

import android.os.AsyncTask;
import android.os.SystemClock;

// The Worker Class - Counter

public class CounterAsyncTask extends AsyncTask<Integer, Integer, Void> {

    private IAsyncTaskEvents asyncTaskEvents;

    public CounterAsyncTask(IAsyncTaskEvents iAsyncTaskEvents) {
        asyncTaskEvents = iAsyncTaskEvents;
    }

    @Override
    protected Void doInBackground(Integer... numbers) {

        for (int i = 0; i <= 10; i++) {
            if (!isCancelled()) {
                publishProgress(i);
                SystemClock.sleep(500);

            } else {
                break;
            }
        }
        return null;

    }

    // Updating the UI methods

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (asyncTaskEvents != null) {
            asyncTaskEvents.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (asyncTaskEvents != null) {
            asyncTaskEvents.onPostExecute();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (asyncTaskEvents != null) {
            asyncTaskEvents.onProgressUpdate(values[0]);
        }
    }


}
