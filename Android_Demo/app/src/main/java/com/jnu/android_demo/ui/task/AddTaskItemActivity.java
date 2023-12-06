package com.jnu.android_demo.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.jnu.android_demo.R;

public class AddTaskItemActivity extends AppCompatActivity {
    private TextView textView_task_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_item);

        // 点击响应：点击三角形图标弹出选项——每日、每周、普通
        ImageView imageView = findViewById(R.id.imageView_triangle_gray);
        textView_task_type = findViewById(R.id.textView_task_type);
        imageView.setOnClickListener(this::showPopupMenu);



    }

    // 弹出菜单
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu_choose_task_type);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_option_daily) {
                textView_task_type.setText("每日任务");
            } else if (id == R.id.menu_option_weekly) {
                textView_task_type.setText("每周任务");
            } else if (id == R.id.menu_option_ordinary) {
                textView_task_type.setText("普通任务");
            }
            return true;
        });

        popupMenu.show();
    }
}