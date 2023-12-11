package com.jnu.android_demo.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

        // 为确定按钮设置监听
        findViewById(R.id.button_add_task_item_ok).setOnClickListener(v -> {
            Intent intent = new Intent();

            // 放入数据
            EditText editText_task_title = findViewById(R.id.editTextText_title);
            EditText editText_task_score = findViewById(R.id.editTextNumber_reward_point);
            EditText editText_task_amount = findViewById(R.id.editTextNumber_task_amount);
            TextView editText_task_type = findViewById(R.id.textView_task_type);


            intent.putExtra("task_name", editText_task_title.getText().toString());
            intent.putExtra("task_score", Integer.parseInt(editText_task_score.getText().toString()));
            intent.putExtra("task_amount", Integer.parseInt(editText_task_amount.getText().toString()));
            String type = editText_task_type.getText().toString();
            switch (type) {
                case "每日任务":
                    intent.putExtra("task_type", 0);
                    break;
                case "每周任务":
                    intent.putExtra("task_type", 1);
                    break;
                case "普通任务":
                    intent.putExtra("task_type", 2);
                    break;
            }

            // 传递数据并结束当前Activity
            this.setResult(RESULT_OK, intent);
            this.finish();
        });

        // 为取消按钮设置监听
        findViewById(R.id.button_add_task_item_cancel).setOnClickListener(v -> {
            Intent intent = new Intent();
            this.setResult(RESULT_CANCELED, intent);
            this.finish();
        });

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