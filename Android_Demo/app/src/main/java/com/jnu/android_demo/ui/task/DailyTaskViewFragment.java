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


public class DailyTaskViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private SharedViewModel viewModel;
    private ArrayList<TaskItem> dailyTaskItems = new ArrayList<>();


    public DailyTaskViewFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyTaskItems=new ArrayList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily_task_view, container, false);


        // 测试数据
        TaskItem taskItem1 = new TaskItem("每日签到", 10, 0, 1, 1);
        TaskItem taskItem2 = new TaskItem("每日阅读", 10, 0, 1, 1);
        TaskItem taskItem3 = new TaskItem("每日分享", 10, 0, 1, 1);
        TaskItem taskItem4 = new TaskItem("每周签到", 10, 1, 1, 1);
        TaskItem taskItem5 = new TaskItem("每周阅读", 10, 1, 1, 1);
        TaskItem taskItem6 = new TaskItem("每周分享", 10, 1, 1, 1);
        TaskItem taskItem7 = new TaskItem("普通任务1", 10, 2, 1, 1);
        TaskItem taskItem8 = new TaskItem("普通任务2", 10, 2, 1, 1);
        TaskItem taskItem9 = new TaskItem("普通任务3", 10, 2, 1, 1);
        viewModel.addData(taskItem1);
        viewModel.addData(taskItem2);
        viewModel.addData(taskItem3);
        viewModel.addData(taskItem4);
        viewModel.addData(taskItem5);
        viewModel.addData(taskItem6);
        viewModel.addData(taskItem7);
        viewModel.addData(taskItem8);
        viewModel.addData(taskItem9);


        // 初始化recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_daily_task);
        TaskRecycleViewAdpater taskRecycleViewAdpater = new TaskRecycleViewAdpater(requireActivity(), dailyTaskItems);
        recyclerView.setAdapter(taskRecycleViewAdpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // 观察数据变化
        viewModel.getDataList().observe(getViewLifecycleOwner(), newData -> {
            // 根据TaskItem的type属性判断是否为每日任务,是则添加到dailyTaskItems中
            dailyTaskItems.clear();
            for (TaskItem taskItem : newData) {
                if (taskItem.getType() == 0) {
                    dailyTaskItems.add(taskItem);
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