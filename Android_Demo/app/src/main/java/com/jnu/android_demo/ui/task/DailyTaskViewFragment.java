package com.jnu.android_demo.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jnu.android_demo.R;


public class DailyTaskViewFragment extends Fragment {


    public DailyTaskViewFragment() {
    }


    public static DailyTaskViewFragment newInstance(String param1, String param2) {
        DailyTaskViewFragment fragment = new DailyTaskViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_task_view, container, false);
    }
}