package com.jnu.android_demo.ui.task;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jnu.android_demo.R;
import com.jnu.android_demo.data.TaskItem;

import java.util.ArrayList;
import java.util.Objects;


public class WeeklyTaskViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedViewModel viewModel;
    private ArrayList<TaskItem> weeklyTaskItems = new ArrayList<>();


    public WeeklyTaskViewFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weeklyTaskItems = new ArrayList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weekly_task_view, container, false);

        // 初始化recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_weekly_task);
        TaskRecycleViewAdpater taskRecycleViewAdpater = new TaskRecycleViewAdpater(requireActivity(), weeklyTaskItems);
        recyclerView.setAdapter(taskRecycleViewAdpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // 观察数据变化
        viewModel.getDataList().observe(getViewLifecycleOwner(), newData -> {
            // 根据数据类型筛选出每周任务
            weeklyTaskItems.clear();
            for (TaskItem taskItem : newData) {
                if (taskItem.getType() == 1) {
                    weeklyTaskItems.add(taskItem);
                }
            }
            // 更新RecyclerView数据
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        });

        // 设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(requireContext().getResources().getDrawable(R.drawable.divider_drawable));
        recyclerView.addItemDecoration(itemDecoration);

        return rootView;
    }
}