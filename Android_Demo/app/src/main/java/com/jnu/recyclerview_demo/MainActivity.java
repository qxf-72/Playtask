package com.jnu.recyclerview_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.recyclerview_demo.data.Book;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ActivityResultLauncher<Intent> addItem_launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("创新工程实践", R.drawable.book_no_name));
        books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
        books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));

        // 初始化recycleView
        recyclerView = this.findViewById(R.id.recycle_view_books);
        RecycleViewBookAdpater recycleViewBookAdpater = new RecycleViewBookAdpater(this, books);
        recyclerView.setAdapter(recycleViewBookAdpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 利用ActivityResultLauncher来获取从BookItemDetailsActivity返回的数据
        addItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        String itemName = intent.getStringExtra("ItemName");
                        books.add(new Book(itemName, R.drawable.book_no_name));
                        // 通知适配器数据已经改变
                        recycleViewBookAdpater.notifyItemInserted(books.size());
                    }
                });
    }


    // 为item设置长按监听
    public boolean onContextItemSelected(MenuItem item) {
        // 获取AdapterContextMenuInfo,以此来获取选择的listview项目
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // item.getOrderId()获取的是listview中的位置
        Toast.makeText(this, "click " + item.getOrder(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            // 添加选项
            case 0:
                // 跳转到BookItemDetailsActivity
                Intent intent = new Intent(this, BookItemDetailsActivity.class);
                addItem_launcher.launch(intent);
                startActivity(intent);
                break;
            // 删除选项
            case 1:

                break;
            // 修改选项
            case 2:

                break;
        }
        return super.onContextItemSelected(item);
    }
}
