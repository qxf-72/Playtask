package com.jnu.android_demo.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jnu.android_demo.R;

import java.util.Objects;


public class MeViewFragment extends Fragment {


    public MeViewFragment() {
    }

    public static MeViewFragment newInstance() {
        return new MeViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_me_view, container, false);

        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);
        root_view.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        });



        return root_view;
    }
}