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

// recyclerview适配器
public class TaskRecycleViewAdpater extends RecyclerView.Adapter<TaskRecycleViewAdpater.MyViewHolder> {
    private Context context;
    private ArrayList<TaskItem> taskItems;

    public TaskRecycleViewAdpater(Context context, ArrayList<TaskItem> taskItems) {
        this.context = context;
        this.taskItems = taskItems;
    }


    @NonNull
    @Override
    public TaskRecycleViewAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 绑定一项数据所用到的Layout
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycle_view_task_item, parent, false);
        return new MyViewHolder(view);
    }

    // 设置显示的数据
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdpater.MyViewHolder holder, int position) {
        // 获取当前项的数据
        TaskItem taskItem = taskItems.get(position);
        holder.taskName.setText(taskItem.getTaskName());
        holder.amount.setText(taskItem.getFinishedAmount() + "/" + taskItem.getAmount());
        holder.score.setText("+" + taskItem.getScore());
    }

    // 获取传入数据的大小
    @Override
    public int getItemCount() {
        return taskItems.size();
    }
    // 自定义ViewHolder
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