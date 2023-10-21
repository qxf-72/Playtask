package com.jnu.recyclerview_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookItemDetailsActivity extends AppCompatActivity {
    private int position ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_details);

        // 获取从MainActivity传递过来的数据
        Intent intent = getIntent();
        if(null != intent){
            String itemName = intent.getStringExtra("ItemName");
            EditText editTextItemName = findViewById(R.id.editTextText_Item_name);
            editTextItemName.setText(itemName);
            position = intent.getIntExtra("ItemPosition", -1);
        }
        else{
            EditText editTextItemName = findViewById(R.id.editTextText_Item_name);
            editTextItemName.setText("未命名");
        }


        // 为确定按钮设置监听
        Button button_ok = findViewById(R.id.button_item_details_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName = findViewById(R.id.editTextText_Item_name);
                // 将输入的数据放入intent
                intent.putExtra("ItemName", editTextItemName.getText().toString());
                intent.putExtra("ItemPosition", position);
                // 返回数据
                BookItemDetailsActivity.this.setResult(RESULT_OK, intent);
                //关闭当前Activity
                BookItemDetailsActivity.this.finish();
            }
        });

        Button button_cancel = findViewById(R.id.button_item_details_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回数据
                BookItemDetailsActivity.this.setResult(RESULT_CANCELED);
                //关闭当前Activity
                BookItemDetailsActivity.this.finish();
            }
        });
    }
}