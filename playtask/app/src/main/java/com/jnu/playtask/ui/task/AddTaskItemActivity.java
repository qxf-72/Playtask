package com.jnu.playtask.ui.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jnu.playtask.R;

public class AddTaskItemActivity extends AppCompatActivity {
    private Spinner spinner;
    Intent intent = new Intent();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_item);

        EditText editText_name = findViewById(R.id.editTextText_title);
        EditText editText_score = findViewById(R.id.editTextNumber_reward_point);
        EditText editText_total_amount = findViewById(R.id.editTextNumber_task_amount);


        // 初始化Spinner
        spinner = this.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options_task_type,  // 定义在res/values/arrays.xml中的选项
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
            editText_total_amount.setText(String.valueOf(intentFromOther.getIntExtra("total_amount", 0)));
            spinner.setSelection(intentFromOther.getIntExtra("type", 0));
        }


        // “确定”按钮响应
        findViewById(R.id.button_add_task_item_ok).setOnClickListener(v -> {
            String name = editText_name.getText().toString();
            if (name.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请输入名称");
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

            String total_amount_text = editText_total_amount.getText().toString();
            if (total_amount_text.equals("") || Integer.parseInt(total_amount_text) <= 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请输入正确的任务数量");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            if(total_amount_text.length()>7){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("任务数量不能超过7位数");
                builder.setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            intent.putExtra("total_amount", Integer.parseInt(total_amount_text));

            intent.putExtra("type", spinner.getSelectedItemPosition());
            // 传递数据并结束当前Activity
            this.setResult(RESULT_OK, intent);
            this.finish();
        });


        // “取消”按钮响应
        findViewById(R.id.button_add_task_item_cancel).setOnClickListener(v -> {
            Intent intent = new Intent();
            this.setResult(RESULT_CANCELED, intent);
            this.finish();
        });

    }
}