package com.jnu.android_demo.ui.reward;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.android_demo.R;
import com.jnu.android_demo.data.RewardItem;

import java.util.ArrayList;

public class RewardRecycleViewAdapter extends RecyclerView.Adapter<RewardRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<RewardItem> rewardItems;
    // 单击和长按监听器
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public RewardRecycleViewAdapter(Context context, ArrayList<RewardItem> rewardItems) {
        this.context = context;
        this.rewardItems = rewardItems;
    }


    @NonNull
    @Override
    public RewardRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 绑定一项数据所用到的Layout
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycle_view_reward_item, parent, false);
        return new RewardRecycleViewAdapter.MyViewHolder(view);
    }

    /**
     * 绑定数据到视图
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RewardRecycleViewAdapter.MyViewHolder holder, int position) {
        // 获取当前项的数据
        RewardItem rewardItem = rewardItems.get(position);
        holder.rewardName.setText(rewardItem.getName());
        if (rewardItem.getType() == 0) {
            holder.rewardAmount.setText("0/1");
        } else {
            holder.rewardAmount.setText(rewardItem.getFinishedAmount() + " / ∞");
        }
        holder.rewardScore.setText("-" + rewardItem.getScore());


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


    }

    /**
     * 获取数据项的数量
     */
    @Override
    public int getItemCount() {
        return rewardItems.size();
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
        TextView rewardName;
        TextView rewardAmount;
        TextView rewardScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rewardName = itemView.findViewById(R.id.textView_reward_name_in_recycle_view);
            rewardAmount = itemView.findViewById(R.id.textView_reward_amount_in_recycle_view);
            rewardScore = itemView.findViewById(R.id.textView_reward_score_in_recycle_view);
        }
    }
}
