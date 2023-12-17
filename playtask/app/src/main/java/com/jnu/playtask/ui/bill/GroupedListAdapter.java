package com.jnu.playtask.ui.bill;


import android.content.Context;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.jnu.playtask.R;
import com.jnu.playtask.data.CountItem;

import java.util.ArrayList;


/**
 * @author Xiaofeng Qiu
 * recycleview的适配器
 */
public class GroupedListAdapter extends GroupedRecyclerViewAdapter {
    protected ArrayList<GroupEntity> mGroups;

    public GroupedListAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context);
        mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<CountItem> children = mGroups.get(groupPosition).getChildren();
        return children == null ? 0 : children.size();
    }

    public void clear() {
        mGroups.clear();
        notifyDataChanged();
    }

    public void setGroups(ArrayList<GroupEntity> groups) {
        mGroups = groups;
        notifyDataChanged();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_child;
    }


    /**
     * 绑定头部布局数据
     */
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_header, entity.getHeader());
    }


    /**
     * 绑定尾部布局数据
     */
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_footer, entity.getFooter());
    }


    /**
     * 绑定子项布局数据
     */
    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        CountItem entity = mGroups.get(groupPosition).getChildren().get(childPosition);
        holder.setText(R.id.textView_name, entity.getName());

        int score = entity.getScore();
        if (score > 0) {
            holder.setText(R.id.textView_score, "+" + score);
            holder.setTextColor(R.id.textView_score, mContext.getResources().getColor(R.color.blue));
        } else {
            holder.setText(R.id.textView_score, "" + score);
            holder.setTextColor(R.id.textView_score, mContext.getResources().getColor(R.color.red));
        }

        // 获取时间字符串，格式为：HH:mm:ss
        String time = entity.getTime().toString().substring(11, 19);
        holder.setText(R.id.textView_time, time);
    }
}
