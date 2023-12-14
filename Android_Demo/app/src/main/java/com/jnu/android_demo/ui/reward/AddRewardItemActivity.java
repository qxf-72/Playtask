package com.jnu.android_demo.ui.reward;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.jnu.android_demo.R;

public class AddRewardItemActivity extends AppCompatActivity {
    private TextView textView_type;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward_item);

        textView_type = findViewById(R.id.textView_reward_type);
        ImageView imageView = findViewById(R.id.imageView_triangle_gray);
        EditText title = findViewById(R.id.editTextText_title);
        EditText score = findViewById(R.id.editTextNumber_reward_point);



        // 获取从别的页面传递过来的数据
        Intent intentFromOther = getIntent();
        if (null != intentFromOther && intentFromOther.hasExtra("position")) {

            intent.putExtra("position", intentFromOther.getIntExtra("position", 0));

            title.setText(intentFromOther.getStringExtra("name"));
            score.setText(String.valueOf(intentFromOther.getIntExtra("score", 0)));
            switch (intentFromOther.getIntExtra("type", 0)) {
                case 0:
                    textView_type.setText(getResources().getString(R.string.text_once));
                    break;
                case 1:
                    textView_type.setText(getResources().getString(R.string.text_repeatedly));
                    break;
            }
        }


        // 点击响应：点击三角形图标弹出选项——每日、每周、普通
        imageView.setOnClickListener(this::showPopupMenu);

        // 点击响应：点击输入框清空内容
        title.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && title.getText().toString().equals(getResources().getString(R.string.text_title)))
                title.setText("");
        });
        score.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && score.getText().toString().equals(getResources().getString(R.string.text_reward_points_spent)))
                score.setText("");
        });


        // 为确定按钮设置监听
        findViewById(R.id.button_add_reward_item_ok).setOnClickListener(v -> {
            // 放入数据
            intent.putExtra("name", title.getText().toString());
            intent.putExtra("score", Integer.parseInt(score.getText().toString()));
            String type = textView_type.getText().toString();
            switch (type) {
                case "单次":
                    intent.putExtra("type", 0);
                    break;
                case "多次":
                    intent.putExtra("type", 1);
                    break;
            }

            // 传递数据并结束当前Activity
            this.setResult(RESULT_OK, intent);
            this.finish();
        });

        // 为取消按钮设置监听
        findViewById(R.id.button_add_reward_item_cancel).setOnClickListener(v -> {
            Intent intent = new Intent();
            this.setResult(RESULT_CANCELED, intent);
            this.finish();
        });

    }

    // 弹出菜单
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu_choose_reward_type);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_option_once) {
                textView_type.setText("单次");
            } else if (id == R.id.menu_option_repeatedly) {
                textView_type.setText("多次");
            }
            return true;
        });

        popupMenu.show();
    }
}