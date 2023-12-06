package com.jnu.android_demo;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jnu.android_demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 布局文件绑定类
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // 将ActivityMainBinding对象的根视图设置为Activity的视图
        setContentView(binding.getRoot());

        // 设置Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // 底部导航栏类
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 创建AppBarConfiguration对象，并将BottomNavigationView 的菜单ID传递给它
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_task, R.id.navigation_reward, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


/*        // 底部导航栏的点击事件，点击时改变图标颜色
        navView.setOnNavigationItemReselectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_task) {
                item.setIcon(R.drawable.ic_task_blue_36dp);
            } else if (id == R.id.navigation_reward) {
                item.setIcon(R.drawable.ic_reward_blue_36dp);
            } else if (id == R.id.navigation_me) {
                item.setIcon(R.drawable.ic_me_blue_36dp);
            }
        });*/
    }



}
