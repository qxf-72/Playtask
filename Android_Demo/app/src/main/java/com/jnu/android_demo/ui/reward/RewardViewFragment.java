package com.jnu.android_demo.ui.reward;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.android_demo.MainActivity;
import com.jnu.android_demo.R;
import com.jnu.android_demo.data.RewardItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;


public class RewardViewFragment extends Fragment {
    private ActivityResultLauncher<Intent> addItem_launcher;
    private RecyclerView recyclerView;
    // 从数据库中获取数据
    private ArrayList<RewardItem> rewardItems;


    public RewardViewFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 导入数据库中的数据
        rewardItems = (ArrayList<RewardItem>) MainActivity.mDBMaster.mRewardDAO.queryDataList();
        if (rewardItems == null) {
            rewardItems = new ArrayList<>();
        }
    }


    /**
     * 在创建视图时加载布局
     */
    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reward_view, container, false);

        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);

        rootView.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

            // 显示返回按钮
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

        });


        // 初始化recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_reward_task);
        RewardRecycleViewAdapter taskRecycleViewAdapter = new RewardRecycleViewAdapter(requireActivity(), rewardItems);
        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        // 利用ActivityResultLauncher来获取从AddRewardItemActivity返回的数据
        addItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        // 获取从AddRewardItemActivity返回的数据
                        Intent intent = result.getData();
                        if (intent == null) {
                            throw new IllegalStateException("Unexpected value: " + result);
                        }
                        String name = intent.getStringExtra("name");
                        int score = intent.getIntExtra("score", 0);
                        int type = intent.getIntExtra("type", 0);

                        RewardItem rewardItem = new RewardItem(new Timestamp(System.currentTimeMillis()), name, score, type, 0);

                        // 插入数据库
                        rewardItem.setId(MainActivity.mDBMaster.mRewardDAO.insertData(rewardItem));

                        // 添加数据
                        rewardItems.add(rewardItem);

                        // 更新recycleView
                        taskRecycleViewAdapter.notifyDataSetChanged();

                    }
                });


        // 设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(requireContext().getResources().getDrawable(R.drawable.divider_drawable));
        recyclerView.addItemDecoration(itemDecoration);

        return rootView;
    }


    /**
     * 加载toolbar菜单
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 处理toolbar菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            showPopupMenu(item.getActionView());
            return true;
        } else if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 弹出菜单
     */
    private void showPopupMenu(View view) {
        String[] options = {"新建奖励", "排序"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                // 处理新建菜单项点击事件
                case 0:
                    // 跳转到BookItemDetailsActivity
                    Intent intent = new Intent(requireContext(), AddRewardItemActivity.class);
                    addItem_launcher.launch(intent);
                    break;
                // 处理排序菜单项点击事件
                case 1:

                    break;
            }
        });

        builder.show();
    }


}