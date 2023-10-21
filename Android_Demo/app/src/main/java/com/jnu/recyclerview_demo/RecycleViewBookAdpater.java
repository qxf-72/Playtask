//数据通过Adapter传递给UI控件
package com.jnu.recyclerview_demo;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.recyclerview_demo.data.BookItem;

import java.util.ArrayList;

public class RecycleViewBookAdpater extends RecyclerView.Adapter<RecycleViewBookAdpater.MyViewHolder> {
    Context context;
    ArrayList<BookItem> bookItems;

    public RecycleViewBookAdpater(Context context, ArrayList<BookItem> bookItems) {
        this.context = context;
        this.bookItems = bookItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //绑定一项数据所用到的Layout
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycleview_item, parent, false);
        return new MyViewHolder(view);
    }

    //设置显示的数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageDrawable(this.context.getDrawable(this.bookItems.get(position).getCoverResourceId()));
        holder.textView.setText(this.bookItems.get(position).getTitle());

    }

    //获取传入数据的大小
    @Override
    public int getItemCount() {
        return this.bookItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_book_cover);
            textView = itemView.findViewById(R.id.text_view_book_title);

            itemView.setOnCreateContextMenuListener(this);  //为item创建上下文菜单
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("具体操作");
            // getAdapterPosition()获取当前item的位置,即在list中的位置
            // menu.add(分组id,itemid,item位置,标题)
            menu.add(0, 0, this.getAdapterPosition(), "新建");
            menu.add(0, 1, this.getAdapterPosition(), "修改第" + (this.getAdapterPosition()+1)+"项");
            menu.add(0, 2, this.getAdapterPosition(), "删除第" + (this.getAdapterPosition()+1)+"项");

        }
    }
}
