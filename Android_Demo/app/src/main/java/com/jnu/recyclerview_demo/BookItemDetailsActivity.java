package com.jnu.recyclerview_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item_details);

        Button button_ok = findViewById(R.id.button_item_details_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName = findViewById(R.id.editTextText_Item_name);
                // 将输入的数据放入intent
                intent.putExtra("ItemName", editTextItemName.getText().toString());
                // 返回数据
                BookItemDetailsActivity.this.setResult(RESULT_OK, intent);
                //关闭当前Activity
                BookItemDetailsActivity.this.finish();
            }
        });
    }
}