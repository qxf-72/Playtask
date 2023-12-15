package com.jnu.android_demo;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jnu.android_demo.data.CountItem;
import com.jnu.android_demo.data.DBMaster;
import com.jnu.android_demo.data.RewardItem;
import com.jnu.android_demo.data.TaskItem;
import com.jnu.android_demo.databinding.ActivityMainBinding;
import com.jnu.android_demo.util.CountViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // 声明数据库操作实例
    @SuppressLint("StaticFieldLeak")
    public static DBMaster mDBMaster;
    private CountViewModel countViewModel;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //启动数据库
        mDBMaster = new DBMaster(getApplicationContext());
        mDBMaster.openDataBase();


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
                R.id.navigation_task, R.id.navigation_reward,R.id.navigation_count, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }



    @Override
    protected void onStart() {
        super.onStart();
        // 创建ViewModel实例，和子Fragment共享数据
        countViewModel = new ViewModelProvider(this).get(CountViewModel.class);
        int total = 0;
        ArrayList<CountItem> countItems = mDBMaster.mCountDAO.queryDataList();
        if (countItems != null) {
            for (CountItem countItem : countItems) {
                total += countItem.getScore();
            }
        }
        countViewModel.setData(total);

//        mDBMaster.mTaskDAO.insertData(new TaskItem(new Timestamp(System.currentTimeMillis()), "", 0, 0, 0, 0));
    }

}
