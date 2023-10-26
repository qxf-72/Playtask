package com.jnu.recyclerview_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jnu.recyclerview_demo.data.BookItem;
import com.jnu.recyclerview_demo.data.DataBank;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> addItem_launcher;
    private ActivityResultLauncher<Intent> updateItem_launcher;


    public BookListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment__book_list, container, false);

        // 加载数据
        bookItems=new DataBank().LoadBookItem(requireActivity());
        if( bookItems.isEmpty()){
            bookItems.add(new BookItem("创新工程实践", R.drawable.book_no_name));
            bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));
            bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
        }

        // 初始化recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_books);
        RecycleViewBookAdpater recycleViewBookAdpater = new RecycleViewBookAdpater(requireActivity(), bookItems);
        recyclerView.setAdapter(recycleViewBookAdpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

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
                        new DataBank().SaveBookItem(requireActivity(), bookItems);
                    }else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        Toast.makeText(requireActivity(), "操作取消", Toast.LENGTH_SHORT).show();
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
                        new DataBank().SaveBookItem(requireActivity(), bookItems);
                    }else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        Toast.makeText(requireActivity(), "操作取消", Toast.LENGTH_SHORT).show();
                    }
                });
        return rootView;
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
                Intent intent = new Intent(requireActivity(), BookItemDetailsActivity.class);
                // 启动BookItemDetailsActivity
                addItem_launcher.launch(intent);
                break;
            // 修改选项
            case 1:
                Intent intentUpdate = new Intent(requireActivity(), BookItemDetailsActivity.class);
                BookItem bookItem = bookItems.get(item.getOrder());
                intentUpdate.putExtra("ItemName", bookItem.getTitle());
                intentUpdate.putExtra("ItemPosition", item.getOrder());
                updateItem_launcher.launch(intentUpdate);
                break;
            // 删除选项
            case 2:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除");
                builder.setMessage("确定删除第" + (item.getOrder()+1) + "项吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    bookItems.remove(item.getOrder());
                    // 通知适配器数据已经改变
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(item.getOrder());
                    // 持久化保存数据
                    new DataBank().SaveBookItem(requireActivity(), bookItems);
                });
                builder.setNegativeButton("取消", (dialog, which) -> {
                    Toast.makeText(requireActivity(), "操作取消", Toast.LENGTH_SHORT).show();
                });
                builder.show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}