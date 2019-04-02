package com.academy.fundamentals.mymoviesapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.support.constraint.Constraints.TAG;

/**
 * This service running on UI (main) thread and therefore we need thread handler for asynchronous
 * tasks on background
 */
public class HardJobService extends Service {

    ServiceHandler mServiceHandler;
    private boolean isDestroyed = true;

    public HardJobService() {
    }

    @Override
    public void onCreate() {
        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread(TAG, THREAD_PRIORITY_BACKGROUND);
        // start the new handler thread
        thread.start();
        Looper mServiceLooper = thread.getLooper();
        // start the service using the background handler
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * onStartCommand() is called every time a client starts the service
     * using startService(Intent intent).
     * This means that onStartCommand() can get called multiple times.
     *
     * If you don't implement onStartCommand() then you won't be able to get any information
     * from the Intent that the client passes to onStartCommand() and your service might not be able
     * to do any useful work.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isDestroyed = false;
        // call a new service handler. The service ID can be used to identify the service
        Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        mServiceHandler.sendMessage(message);
        // Using this return value, if the OS kills our Service it will recreate it but the
        // Intent that was sent to the Service isn’t redelivered. In this way the Service is always running.
        return START_STICKY;
    }


    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            isDestroyed = false;

            for (int i = 0; i <= 100 && !isDestroyed; i++) {
                SystemClock.sleep(100);
                Intent intent = new Intent(BGServiceActivity.PROGRESS_UPDATE_ACTION);
                intent.putExtra(BGServiceActivity.BackgroundProgressReceiver.PROGRESS_VALUE_KEY, i);
                sendBroadcast(intent);
            }
            stopSelf(msg.arg1);
        }

    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }
}