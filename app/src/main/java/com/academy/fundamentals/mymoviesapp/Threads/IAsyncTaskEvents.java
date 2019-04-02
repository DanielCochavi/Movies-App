package com.academy.fundamentals.mymoviesapp.Threads;

//Interface for communicating with the UI
/**
 * This interface must be implemented by activities that contain the
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * **/
public interface IAsyncTaskEvents {

    void createAsyncTask();
    void startAsyncTask();
    void cancelAsyncTask();

    void onPreExecute();
    void onPostExecute();
    void onProgressUpdate(Integer integer);
    void onCancel();
}
