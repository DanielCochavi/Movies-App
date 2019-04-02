package com.academy.fundamentals.mymoviesapp.Threads;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.academy.fundamentals.mymoviesapp.R;


public class CounterFragment extends Fragment implements View.OnClickListener {

    public static final String ARGS_TYPE = "CounterFragment-data";

    private Button startBtn;
    private Button cancelBtn;
    private Button createBtn;
    private TextView content;

    private IAsyncTaskEvents callbackListener;

    public CounterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_counter, container, false);

        createBtn = rootView.findViewById(R.id.create);
        startBtn = rootView.findViewById(R.id.start);
        cancelBtn = rootView.findViewById(R.id.cancel);
        content = rootView.findViewById(R.id.textViewAsync);

        createBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        //UNPACK OUR DATA FROM OUR BUNDLE
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String fragmentText = this.getArguments().getString(ARGS_TYPE);
            content.setText(fragmentText);
        }
        return rootView;
    }


    /**
     * Called when the fragment has been associated with the activity (the Activity is passed in here).
     * Suppose if there are 2 Activities which host the same Fragment,
     * and at runtime we don't know from which activity is the fragment is currently.
     * That is the reason we use onAttach().
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity hostActivity = getActivity();
        if (hostActivity instanceof IAsyncTaskEvents) {
            callbackListener = (IAsyncTaskEvents) hostActivity;
        } else {
            throw new RuntimeException(hostActivity.toString()
                    + " must implement IAsyncTaskEvents");
        }
    }

    /**
     * This method called after onDestroy() method to notify that the fragment has been disassociated
     * from its hosting activity means Fragment is detached from its host Activity.
     */

    @Override
    public void onDetach() {
        super.onDetach();
        callbackListener = null;
    }

    public void updateFragmentText(String text) {
        if (content != null) {
            content.setText(text);
        }
    }

    // Hook method into UI event
    @Override
    public void onClick(View v) {
        if (callbackListener != null) {
            switch (v.getId()) {
                case (R.id.create):
                    callbackListener.createAsyncTask();
                    break;
                case (R.id.start):
                    callbackListener.startAsyncTask();
                    break;
                case (R.id.cancel):
                    callbackListener.cancelAsyncTask();
                    break;
            }
        }
    }
}
