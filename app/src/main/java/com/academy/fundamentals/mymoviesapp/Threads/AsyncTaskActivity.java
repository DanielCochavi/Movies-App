package com.academy.fundamentals.mymoviesapp.Threads;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.academy.fundamentals.mymoviesapp.R;

public class AsyncTaskActivity extends AppCompatActivity implements IAsyncTaskEvents {

    private CounterFragment fragment;
    private CounterAsyncTask asyncTask;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        //Get Fragment Manager
        if (manager == null) {
            manager = getSupportFragmentManager();
        }

        //setArguments can only be called before the Fragment is attached to the Activity.
        if (fragment == null) {
            fragment = new CounterFragment(); //Get Fragment Instance
            Bundle bundle = new Bundle(); //Use bundle to pass data
            bundle.putString(CounterFragment.ARGS_TYPE, getString(R.string.AsyncTask));
            fragment.setArguments(bundle); //Finally set argument bundle to fragment
            manager.beginTransaction().replace(R.id.AsyncFragmnent, fragment).commit(); //now replace the argument fragment
        }
    }


    @Override
    public void createAsyncTask() {
        Toast.makeText(this, getString(R.string.msg_oncreate), Toast.LENGTH_SHORT).show();
        asyncTask = new CounterAsyncTask(this);
    }

    @Override
    public void startAsyncTask() {
        if (asyncTask == null || asyncTask.isCancelled()) {
            Toast.makeText(this, getString(R.string.msg_need_to_create), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.msg_onstart), Toast.LENGTH_SHORT).show();
            asyncTask.execute(10);
        }

    }

    @Override
    public void cancelAsyncTask() {
        if (asyncTask == null) {
            Toast.makeText(this, getString(R.string.msg_need_to_create), Toast.LENGTH_SHORT).show();
        } else {
            asyncTask.cancel(true);
        }
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(this, getString(R.string.msg_preexecute), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
        Toast.makeText(this, getString(R.string.msg_postexecute), Toast.LENGTH_SHORT).show();
        fragment.updateFragmentText(getString(R.string.done));
        asyncTask = null;
    }

    @Override
    public void onProgressUpdate(Integer integer) {
        fragment.updateFragmentText(String.valueOf(integer));
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, getString(R.string.msg_cancel), Toast.LENGTH_SHORT).show();
    }
}
