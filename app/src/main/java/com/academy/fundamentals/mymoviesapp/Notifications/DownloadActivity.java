package com.academy.fundamentals.mymoviesapp.Notifications;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.ImageView;


import com.academy.fundamentals.mymoviesapp.Model.MovieDetailsModel;
import com.academy.fundamentals.mymoviesapp.R;

public class DownloadActivity extends AppCompatActivity {

    private static final String ARG_MOVIE_MODEL = "arg_movie_model";
    public static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int PERMISSIONS_REQUEST_CODE = 42;
    public static String ARG_FILE_PATH = "Image-File-Path";

    private BroadcastReceiver broadcastReceiver;

    String BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String filePath = intent.getStringExtra(ARG_FILE_PATH);
                if (!TextUtils.isEmpty(filePath)) {
                    showImage(filePath);
                }
            }
        };

        if (isPermissionGranted()) {
            startDownloadService();
        } else {
            requestPermission();
        }
    }

    private void showImage(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ImageView imageView = findViewById(R.id.photo_to_download);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
            showExplainingRationaleDialog();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION}, PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showExplainingRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.permission_msg)
                .setTitle(R.string.permission_title);

        builder.setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(this,
                new String[]{PERMISSION}, PERMISSIONS_REQUEST_CODE));

        builder.setNegativeButton("Cancel", (dialog, which) -> finishActivity(PERMISSIONS_REQUEST_CODE));

        builder.create().show();

    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted.
                startDownloadService();
            } else {
                // permission denied. Disable the functionality that depends on this permission.
                finishActivity(PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void startDownloadService() {
        MovieDetailsModel movieDetailsModel = getIntent().getParcelableExtra(ARG_MOVIE_MODEL);

        String full_url = BASE_URL + movieDetailsModel.getBackImageResUrl();

        DownloadService.startService(this, full_url);
    }

    public static void startActivity(Context context, MovieDetailsModel movieDetailsModel) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra(ARG_MOVIE_MODEL, movieDetailsModel);
        context.startActivity(intent);
    }


}