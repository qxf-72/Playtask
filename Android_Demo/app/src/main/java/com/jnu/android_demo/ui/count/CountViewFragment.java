package com.jnu.android_demo.ui.count;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.jnu.android_demo.ui.bill.ShowBillActivity;


/**
 * @author Xiaofeng Qiu
 * create at 2023/12/15
 */
public class CountViewFragment extends Fragment {
    private final String[] titles = new String[]{"日", "周", "月", "年"};

    public CountViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_count_view, container, false);


        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);
        rootView.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        });


        // 获取ViewPager2和TabLayout的实例
        ViewPager2 viewPager = rootView.findViewById(R.id.view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        // 创建适配器
        CountViewFragment.FragmentAdapter fragmentAdapter = new CountViewFragment.FragmentAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

        return rootView;
    }


    /**
     * 在toolbar添加“账单”
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bill, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * 处理toolbar中的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bill) {
            // 处理“账单”菜单项点击事件——跳转到ShowBillActivity
            Intent intent = new Intent(requireActivity(), ShowBillActivity.class);
            // 启动目标页面
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Fragment适配器
     */
    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 4;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        /**
         * 根据position创建Fragment
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DayCountViewFragment();
                case 1:
                    return new WeekCountViewFragment();
                case 2:
                    return new MonthCountViewFragment();
                case 3:
                    return new YearCountViewFragment();
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }

        /**
         * 返回Fragment的数量
         */
        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}