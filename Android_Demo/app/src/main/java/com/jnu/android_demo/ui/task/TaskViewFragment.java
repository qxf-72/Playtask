package com.jnu.android_demo.ui.task;

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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.android_demo.R;

import java.util.Objects;

public class TaskViewFragment extends Fragment {
    private final String[] titles = new String[]{"每日任务", "每周任务", "普通任务"};
    private ActivityResultLauncher<Intent> addItem_launcher;


    public TaskViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_task_view, container, false);

        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);
        root_view.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            // 显示返回按钮
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
        });


        // 获取ViewPager2和TabLayout的实例
        ViewPager2 viewPager = root_view.findViewById(R.id.view_pager);
        TabLayout tabLayout = root_view.findViewById(R.id.tab_layout);
        // 创建适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();


        // 利用ActivityResultLauncher来获取从AddTaskItemActivity返回的数据
        addItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {

                    } else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {

                    }
                });

        return root_view;
    }


    // 在toolbar添加menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // toolbar按钮点击事件
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


    // 弹出菜单
    private void showPopupMenu(View view) {
        String[] options = {"新建任务", "排序"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                // 处理新建菜单项点击事件
                case 0:
                    // 跳转到BookItemDetailsActivity
                    Intent intent = new Intent(requireContext(), AddTaskItemActivity.class);
                    // 启动BookItemDetailsActivity
                    addItem_launcher.launch(intent);
                    break;
                // 处理排序菜单项点击事件
                case 1:

                    break;
            }
        });
        builder.show();
    }


    // 适配器内部类,用于管理Fragment
    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment实例
            switch (position) {
                case 0:
                    return new DailyTaskViewFragment();
                case 1:
                    return new WeeklyTaskViewFragment();
                case 2:
                    return new OrdinaryTaskViewFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}