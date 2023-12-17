package com.jnu.android_demo.ui.task;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.android_demo.MainActivity;
import com.jnu.android_demo.R;
import com.jnu.android_demo.data.TaskDAO;
import com.jnu.android_demo.data.TaskItem;
import com.jnu.android_demo.util.CountViewModel;
import com.jnu.android_demo.util.TaskViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Xiaofeng Qiu
 */
public class DailyTaskViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskViewModel taskViewModel;
    private ArrayList<TaskItem> dailyTaskItems = new ArrayList<>();
    private ActivityResultLauncher<Intent> updateItem_launcher;


    public DailyTaskViewFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyTaskItems = new ArrayList<>();
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily_task_view, container, false);


        // 配置recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_daily_task);
        TaskRecycleViewAdapter taskRecycleViewAdapter = new TaskRecycleViewAdapter(requireActivity(), dailyTaskItems);

        // 设置单击和长按监听器
        taskRecycleViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理点击事件
                Intent intentUpdate = new Intent(requireActivity(), AddTaskItemActivity.class);
                TaskItem taskItem = dailyTaskItems.get(position);
                intentUpdate.putExtra("position", position);
                intentUpdate.putExtra("name", taskItem.getName());
                intentUpdate.putExtra("score", taskItem.getScore());
                intentUpdate.putExtra("type", taskItem.getType());
                intentUpdate.putExtra("total_amount", taskItem.getTotalAmount());

                updateItem_launcher.launch(intentUpdate);

            }
        });
        taskRecycleViewAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理长按事件
                showDeleteConfirmationDialog(position);
                return true;
            }
        });

        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        // 观察数据变化
        taskViewModel.getDataList().observe(getViewLifecycleOwner(), newData -> {
            // 根据TaskItem的type属性判断是否为每日任务,是则添加到dailyTaskItems中
            dailyTaskItems.clear();
            if(newData == null)
                return;
            for (TaskItem taskItem : newData) {
                if (taskItem.getType() == 0 && taskItem.getFinishedAmount() < taskItem.getTotalAmount()) {
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


        // 利用ActivityResultLauncher来获取从AddTaskItemActivity返回的数据，来更新数据
        updateItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        // 获取从AddTaskItemActivity返回的数据
                        Intent intent = result.getData();
                        if (intent == null)
                            return;

                        int position = intent.getIntExtra("position", 0);
                        // 修改数据
                        TaskItem taskItem = dailyTaskItems.get(position);
                        taskItem.setTime(new Timestamp(System.currentTimeMillis()));
                        taskItem.setName(intent.getStringExtra("name"));
                        taskItem.setScore(intent.getIntExtra("score", 0));
                        taskItem.setType(intent.getIntExtra("type", 0));
                        taskItem.setFinishedAmount(0);
                        taskItem.setTotalAmount(intent.getIntExtra("total_amount", 0));

                        // 更新数据库
                        MainActivity.mDBMaster.mTaskDAO.updateData((int) taskItem.getId(), taskItem);

                        // 更新内存中的数据
                        taskViewModel.setDataList(MainActivity.mDBMaster.mTaskDAO.queryDataList(TaskDAO.NO_SORT));
                    }
                });

        return rootView;
    }


    /**
     * 显示删除确认对话框
     */
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("删除任务")
                .setMessage("是否删除该项任务？")
                .setPositiveButton(getResources().getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 同步到数据库
                        MainActivity.mDBMaster.mTaskDAO.deleteData((int) dailyTaskItems.get(position).getId());
                        // 更新内存数据
                        taskViewModel.setDataList(MainActivity.mDBMaster.mTaskDAO.queryDataList(TaskDAO.NO_SORT));
                        if (taskViewModel.getDataList().getValue() == null) {
                            taskViewModel.setDataList(new ArrayList<>());
                        }

                        // 更新RecyclerView页面
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 关闭对话框
                        dialog.dismiss();
                    }
                })
                .show();
    }

}