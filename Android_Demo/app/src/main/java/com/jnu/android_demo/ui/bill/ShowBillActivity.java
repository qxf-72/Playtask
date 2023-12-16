package com.jnu.android_demo.ui.bill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.groupedadapter.widget.StickyHeaderLayout;
import com.jnu.android_demo.MainActivity;
import com.jnu.android_demo.R;
import com.jnu.android_demo.data.CountItem;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Xiaofeng Qiu
 * 显示账单的Activity
 */
public class ShowBillActivity extends AppCompatActivity {

    private NoFooterAdapter adapter;

    private TextView textView_month;

    private static final String[] MONTHS = {"一月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "十一月", "十二月"};

    // 当前时间
    private Timestamp currentTime;
    // 要显示的数据的时间（月份）
    private Timestamp showTime;


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill);

        // toolbar中设置返回按钮
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("账单");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        RecyclerView recycler = (RecyclerView) findViewById(R.id.rv_list);
        // 设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.divider_drawable));
        recycler.addItemDecoration(itemDecoration);
        // 设置StickyHeaderLayout
        StickyHeaderLayout stickyLayout = (StickyHeaderLayout) findViewById(R.id.sticky_layout);

        ImageButton button_previous = findViewById(R.id.button_previous_month);
        ImageButton button_next = findViewById(R.id.button_next_month);
        textView_month = findViewById(R.id.textView_month);


        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        // 设置适配器
        ArrayList<CountItem> countItems_ = MainActivity.mDBMaster.mCountDAO.queryDataByMonth(new Timestamp(System.currentTimeMillis()));
        if (countItems_ == null) {
            countItems_ = new ArrayList<>();
        }
        adapter = new NoFooterAdapter(this, GroupModel.getGroups(countItems_));   // 放入数据
        recycler.setAdapter(adapter);
        //设置是否吸顶
        stickyLayout.setSticky(true);


        // 设置按钮监听器
        button_previous.setOnClickListener(v -> {
            showTime = getPreviousMonthTime(showTime);
            ArrayList<CountItem> countItems = MainActivity.mDBMaster.mCountDAO.queryDataByMonth(showTime);
            if (countItems == null) {
                countItems = new ArrayList<>();
            }
            int year = getYear(showTime);
            textView_month.setText(year + "年 " + MONTHS[getMonth(showTime) - 1]);
            adapter.clear();
            adapter.setGroups(GroupModel.getGroups(countItems));
        });

        button_next.setOnClickListener(v -> {

            if (isInSameMonth(currentTime, showTime)) {
                return;
            }
            showTime = getNextMonthTime(showTime);
            ArrayList<CountItem> countItems = MainActivity.mDBMaster.mCountDAO.queryDataByMonth(showTime);
            if (countItems == null) {
                countItems = new ArrayList<>();
            }
            int year = getYear(showTime);
            textView_month.setText(year + "年 " + MONTHS[getMonth(showTime) - 1]);
            adapter.clear();
            adapter.setGroups(GroupModel.getGroups(countItems));
        });
    }


    /**
     * 在每次显示页面时执行的动作放在这里
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        currentTime = new Timestamp(System.currentTimeMillis());
        showTime = currentTime;
        int year = getYear(showTime);
        textView_month.setText(year + "年 " + MONTHS[getMonth(showTime) - 1]);
    }


    /**
     * 处理toolbar菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 获取月份信息 1-12
     */
    public static int getMonth(Timestamp timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp.getTime());
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.getMonthValue();
    }

    /**
     * 获取年份信息
     */
    public static int getYear(Timestamp timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp.getTime());
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.getYear();
    }


    /**
     * 判断两个时间戳是否在同一个月
     */
    public boolean isInSameMonth(Timestamp timestamp1, Timestamp timestamp2) {
        return getYear(timestamp1) == getYear(timestamp2) && getMonth(timestamp1) == getMonth(timestamp2);
    }

    /**
     * 获取上一个月的时间戳
     */
    public static Timestamp getPreviousMonthTime(Timestamp timestamp) {
        // 计算上一个月的年份和月份
        int previousYear = getMonth(timestamp) == 1 ? getYear(timestamp) - 1 : getYear(timestamp);
        int previousMonth = getMonth(timestamp) == 1 ? 12 : getMonth(timestamp) - 1;
        // 创建上一个月的 LocalDateTime
        LocalDateTime previousMonthDateTime = LocalDateTime.of(previousYear, previousMonth, 1,
                0, 0, 0);
        // 转换为 Timestamp
        Instant previousMonthInstant = previousMonthDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Timestamp(previousMonthInstant.toEpochMilli());
    }

    /**
     * 获取下一个月的时间戳
     */
    public static Timestamp getNextMonthTime(Timestamp timestamp) {
        int nextYear = getMonth(timestamp) == 12 ? getYear(timestamp) + 1 : getYear(timestamp);
        int nextMonth = getMonth(timestamp) == 12 ? 1 : getMonth(timestamp) + 1;
        LocalDateTime nextMonthDateTime = LocalDateTime.of(nextYear, nextMonth, 1,
                0, 0, 0);
        Instant nextMonthInstant = nextMonthDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Timestamp(nextMonthInstant.toEpochMilli());
    }
}