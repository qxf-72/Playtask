package com.jnu.recyclerview_demo;

import  android.app.Activity;
import android.app.AlertDialog;
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

import com.jnu.recyclerview_demo.data.BookItem;
import com.jnu.recyclerview_demo.data.DataBank;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    RecyclerView recyclerView;
    ActivityResultLauncher<Intent> addItem_launcher;
    ActivityResultLauncher<Intent> updateItem_launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 加载数据
        bookItems=new DataBank().LoadBookItem(this.getApplicationContext());
        if( bookItems.isEmpty()){
            bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
            bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));
            bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        }

        // 初始化recycleView
        recyclerView = this.findViewById(R.id.recycle_view_books);
        RecycleViewBookAdpater recycleViewBookAdpater = new RecycleViewBookAdpater(this, bookItems);
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
                        bookItems.add(new BookItem(itemName, R.drawable.book_no_name));
                        // 通知适配器数据已经改变
                        recycleViewBookAdpater.notifyItemInserted(bookItems.size());
                        // 持久化保存数据
                        new DataBank().SaveBookItem(this.getApplicationContext(), bookItems);
                    }else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
                    }
                });

        // 为修改选项设置监听
        updateItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        String itemName = intent.getStringExtra("ItemName");
                        int position = intent.getIntExtra("ItemPosition", -1);
                        bookItems.get(position).setTitle(itemName);
                        // 通知适配器数据已经改变
                        recycleViewBookAdpater.notifyItemChanged(position);
                        // 持久化保存数据
                        new DataBank().SaveBookItem(this.getApplicationContext(), bookItems);
                    }else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // 为item设置长按监听
    public boolean onContextItemSelected(MenuItem item) {
        // 获取AdapterContextMenuInfo,以此来获取选择的listview项目
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // item.getOrderId()获取的是listview中的位置
        // Toast.makeText(this, "click " + item.getOrder(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            // 新建选项
            case 0:
                // 跳转到BookItemDetailsActivity
                Intent intent = new Intent(this, BookItemDetailsActivity.class);
                // 启动BookItemDetailsActivity
                addItem_launcher.launch(intent);
                break;
            // 修改选项
            case 1:
                Intent intentUpdate = new Intent(this, BookItemDetailsActivity.class);
                BookItem bookItem = bookItems.get(item.getOrder());
                intentUpdate.putExtra("ItemName", bookItem.getTitle());
                intentUpdate.putExtra("ItemPosition", item.getOrder());
                updateItem_launcher.launch(intentUpdate);
                break;
            // 删除选项
            case 2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("删除");
                builder.setMessage("确定删除第" + (item.getOrder()+1) + "项吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    bookItems.remove(item.getOrder());
                    // 通知适配器数据已经改变
                    recyclerView.getAdapter().notifyItemRemoved(item.getOrder());
                    // 持久化保存数据
                    new DataBank().SaveBookItem(this.getApplicationContext(), bookItems);
                });
                builder.setNegativeButton("取消", (dialog, which) -> {
                    Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
                });
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
