//数据通过Adapter传递给UI控件
package com.jnu.recyclerview_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewBookAdpater extends RecyclerView.Adapter<RecycleViewBookAdpater.MyViewHolder>{
    Context context;
    ArrayList<Book> books;
    public RecycleViewBookAdpater(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
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
        holder.imageView.setImageDrawable(this.context.getDrawable(this.books.get(position).getCoverResourceId()));
        holder.textView.setText(this.books.get(position).getTitle());

    }

    //获取传入数据的大小
    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_book_cover);
            textView = itemView.findViewById(R.id.text_view_book_title);
        }
    }
}
