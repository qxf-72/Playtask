package com.jnu.playtask.ui.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.jnu.playtask.R;

public class AddTaskItemActivity extends AppCompatActivity {
    private TextView textView_type;
    Intent intent = new Intent();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_item);

        textView_type = findViewById(R.id.textView_reward_type);
        ImageView imageView = findViewById(R.id.imageView_triangle_gray);
        EditText editText_title = findViewById(R.id.editTextText_title);
        EditText editText_score = findViewById(R.id.editTextNumber_reward_point);
        EditText editText_total_amount = findViewById(R.id.editTextNumber_task_amount);


        // 获取从别的页面传递过来的数据
        Intent intentFromOther = getIntent();
        if (null != intentFromOther && intentFromOther.hasExtra("position")) {

            intent.putExtra("position", intentFromOther.getIntExtra("position", 0));

            editText_title.setText(intentFromOther.getStringExtra("name"));
            editText_score.setText(String.valueOf(intentFromOther.getIntExtra("score", 0)));
            editText_total_amount.setText(String.valueOf(intentFromOther.getIntExtra("total_amount", 0)));
            switch (intentFromOther.getIntExtra("type", 0)) {
                case 0:
                    textView_type.setText("每日任务");
                    break;
                case 1:
                    textView_type.setText("每周任务");
                    break;
                case 2:
                    textView_type.setText("普通任务");
                    break;
            }
        }


        // 点击响应：点击三角形图标弹出选项——每日、每周、普通
        imageView.setOnClickListener(this::showPopupMenu);

        // 点击响应：点击输入框清空内容
        editText_total_amount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && editText_total_amount.getText().toString().equals(getResources().getString(R.string.text_task_amount)))
                editText_total_amount.setText("");
        });
        editText_score.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && editText_score.getText().toString().equals(getResources().getString(R.string.text_reward_point)))
                editText_score.setText("");
        });
        editText_title.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && editText_title.getText().toString().equals(getResources().getString(R.string.text_title)))
                editText_title.setText("");
        });

        // 为确定按钮设置监听
        findViewById(R.id.button_add_task_item_ok).setOnClickListener(v -> {

            // 要求用户输入任务类型
            if (textView_type.getText().toString().equals(getResources().getString(R.string.text_task_type))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请选择任务类型");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }

            // 放入数据
            TextView editText_task_type = findViewById(R.id.textView_reward_type);
            intent.putExtra("name", editText_title.getText().toString());
            intent.putExtra("score", Integer.parseInt(editText_score.getText().toString()));
            intent.putExtra("total_amount", Integer.parseInt(editText_total_amount.getText().toString()));
            String type = editText_task_type.getText().toString();
            switch (type) {
                case "每日任务":
                    intent.putExtra("type", 0);
                    break;
                case "每周任务":
                    intent.putExtra("type", 1);
                    break;
                case "普通任务":
                    intent.putExtra("type", 2);
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
                textView_type.setText("每日任务");
            } else if (id == R.id.menu_option_weekly) {
                textView_type.setText("每周任务");
            } else if (id == R.id.menu_option_ordinary) {
                textView_type.setText("普通任务");
            }
            return true;
        });

        popupMenu.show();
    }
}