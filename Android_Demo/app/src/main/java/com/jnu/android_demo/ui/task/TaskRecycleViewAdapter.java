package com.jnu.android_demo.ui.task;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.android_demo.R;
import com.jnu.android_demo.data.TaskItem;

import java.util.ArrayList;

/**
 * 任务列表的适配器
 */
public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TaskItem> taskItems;

    public TaskRecycleViewAdapter(Context context, ArrayList<TaskItem> taskItems) {
        this.context = context;
        this.taskItems = taskItems;
    }


    @NonNull
    @Override
    public TaskRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 绑定一项数据所用到的Layout
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycle_view_task_item, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 绑定数据到视图
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.MyViewHolder holder, int position) {
        // 获取当前项的数据
        TaskItem taskItem = taskItems.get(position);
        holder.taskName.setText(taskItem.getName());
        holder.amount.setText(taskItem.getFinishedAmount() + "/" + taskItem.getTotalAmount());
        holder.score.setText("+" + taskItem.getScore());
    }

    /**
     * 获取数据项的数量
     */
    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    /**
     * 任务列表的ViewHolder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView amount;
        TextView score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.textView_task_name_in_recycle_view);
            amount = itemView.findViewById(R.id.textView_task_amount_in_recycle_view);
            score = itemView.findViewById(R.id.textView_task_score_in_recycle_view);
        }
    }
}