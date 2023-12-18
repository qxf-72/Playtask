package com.jnu.playtask.ui.reward;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jnu.playtask.R;

public class AddRewardItemActivity extends AppCompatActivity {
    Spinner spinner;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward_item);


        EditText editText_name = findViewById(R.id.editTextText_title);
        EditText editText_score = findViewById(R.id.editTextNumber_reward_point);


        // 初始化Spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options_reward_type,  // 定义在res/values/arrays.xml中的选项
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // 获取从别的页面传递过来的数据
        Intent intentFromOther = getIntent();
        if (null != intentFromOther && intentFromOther.hasExtra("position")) {
            intent.putExtra("position", intentFromOther.getIntExtra("position", 0));
            editText_name.setText(intentFromOther.getStringExtra("name"));
            editText_score.setText(String.valueOf(intentFromOther.getIntExtra("score", 0)));
            spinner.setSelection(intentFromOther.getIntExtra("type", 0));
        }


        // 为确定按钮设置监听
        findViewById(R.id.button_add_reward_item_ok).setOnClickListener(v -> {
            String name = editText_name.getText().toString();
            if (name.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请输入奖励名称");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            if(name.length()>12){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("名称长度不能超过12个字符");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            intent.putExtra("name", name);

            String score_text = editText_score.getText().toString();
            if (score_text.equals("") || Integer.parseInt(score_text) <= 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请输入正确的积分");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            if(score_text.length()>7){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("积分不能超过7位数");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            intent.putExtra("score", Integer.parseInt(score_text));


            intent.putExtra("type", spinner.getSelectedItemPosition());

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
}