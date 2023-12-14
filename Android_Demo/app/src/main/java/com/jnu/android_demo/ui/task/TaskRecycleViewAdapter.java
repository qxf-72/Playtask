package com.jnu.android_demo.ui.task;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.android_demo.MainActivity;
import com.jnu.android_demo.R;
import com.jnu.android_demo.data.TaskItem;

import java.util.ArrayList;

/**
 * 任务列表的适配器
 */
public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TaskItem> taskItems;
    // 单击和长按监听器
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

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
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.MyViewHolder holder, int position) {
        // 获取当前项的数据
        TaskItem taskItem = taskItems.get(position);
        holder.name.setText(taskItem.getName());
        holder.total_amount.setText(taskItem.getFinishedAmount() + "/" + taskItem.getTotalAmount());
        holder.score.setText("+" + taskItem.getScore());

        // 设置单击和长按监听器
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(null, view, position, holder.getItemId());
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(null, view, position, holder.getItemId());
                return true; // 返回 true 表示消费了长按事件，不再触发单击事件
            }
            return false;
        });

        // 设置 CheckBox 的状态和点击监听器
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 当 CheckBox 被点击时，更新数据源中该项的状态
            if (isChecked) {
                taskItem.setFinishedAmount(taskItem.getFinishedAmount() + 1);
                if (taskItem.getFinishedAmount() >= taskItem.getTotalAmount()) {
                    MainActivity.mDBMaster.mTaskDAO.deleteData((int) taskItem.getId());
                    taskItems.remove(position);

                } else {
                    MainActivity.mDBMaster.mTaskDAO.updateData((int) taskItem.getId(), taskItem);
                }
                // 延迟0.8s后更新数据源
                buttonView.postDelayed(this::notifyDataSetChanged, 800);

                // 延迟 0.8s 后恢复 CheckBox 的状态
                buttonView.postDelayed(() -> buttonView.setChecked(false), 800);
            }
        });

    }

    /**
     * 获取数据项的数量
     */
    @Override
    public int getItemCount() {
        return taskItems.size();
    }


    /**
     * 设置单击监听器
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 设置长按监听器
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }


    /**
     * 任务列表的ViewHolder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView name;
        TextView total_amount;
        TextView score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox_in_recycle_view);
            name = itemView.findViewById(R.id.textView_task_name_in_recycle_view);
            total_amount = itemView.findViewById(R.id.textView_task_amount_in_recycle_view);
            score = itemView.findViewById(R.id.textView_task_score_in_recycle_view);
        }
    }
}