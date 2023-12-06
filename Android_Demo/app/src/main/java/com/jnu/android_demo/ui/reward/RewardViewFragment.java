package com.jnu.android_demo.ui.reward;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jnu.android_demo.R;

import java.util.Objects;


public class RewardViewFragment extends Fragment {


    public RewardViewFragment() {

    }


    public static RewardViewFragment newInstance() {
        return new RewardViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root_view = inflater.inflate(R.layout.fragment_reward_view, container, false);

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
        String[] options = {"新建奖励", "排序"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // 处理新建菜单项点击事件
                    break;
                case 1:
                    // 处理排序菜单项点击事件
                    break;
            }
        });

        builder.show();
    }


}