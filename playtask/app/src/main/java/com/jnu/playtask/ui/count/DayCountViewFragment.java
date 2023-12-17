package com.jnu.playtask.ui.count;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.jnu.playtask.MainActivity;
import com.jnu.playtask.R;
import com.jnu.playtask.data.CountItem;
import com.jnu.playtask.util.CountViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Xiaofeng Qiu
 * create at 2023/12/15
 */
public class DayCountViewFragment extends Fragment {

    private CountViewModel countViewModel;

    private LineChart incomeChart;
    private LineChart expendChart;
    private LineChart balanceChart;

    private TextView textView_income;
    private TextView textView_expend;
    private TextView textView_balance;

    public DayCountViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countViewModel = new ViewModelProvider(requireActivity()).get(CountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_day_count_view, container, false);

        incomeChart = rootView.findViewById(R.id.day_income_chart);
        expendChart = rootView.findViewById(R.id.day_expend_chart);
        balanceChart = rootView.findViewById(R.id.day_balance_chart);

        textView_income = rootView.findViewById(R.id.textView_income);
        textView_expend = rootView.findViewById(R.id.textView_expend);
        textView_balance = rootView.findViewById(R.id.textView_balance);


        // 当数据发生变化时，重新绘制折线图
        countViewModel.getData().observe(getViewLifecycleOwner(), countItems -> {
            drawLineChart(incomeChart);
            drawLineChart(expendChart);
            drawLineChart(balanceChart);
        });


        return rootView;

    }


    /**
     * 绘制折线图
     */
    @SuppressLint("SetTextI18n")
    public void drawLineChart(LineChart lineChart) {
        String colorStr = null;
        ArrayList<CountItem> countItems = MainActivity.mDBMaster.mCountDAO.queryDataByDay(new Timestamp(System.currentTimeMillis()));

        if (countItems == null) {
            //无数据时显示
            lineChart.setNoDataText("当前没有获取到数据哦~");
            lineChart.setNoDataTextColor(Color.parseColor("#000000"));
            return;
        }

        int[] time = new int[24];
        int[] income = new int[24];
        int total = 0;
        for (int i = 0; i < 24; i++) {
            time[i] = i;
            income[i] = 0;
        }

        if (lineChart.equals(incomeChart)) {
            for (CountItem countItem : countItems) {
                if (countItem.getScore() > 0) {
                    int hour = countItem.getTime().getHours();
                    income[hour] += countItem.getScore();
                    total += countItem.getScore();
                }
            }
            colorStr = "#2196F3";
        } else if (lineChart.equals(expendChart)) {
            for (CountItem countItem : countItems) {
                if (countItem.getScore() < 0) {
                    int hour = countItem.getTime().getHours();
                    income[hour] -= countItem.getScore();
                    total -= countItem.getScore();
                }
            }
            colorStr = "#FFA2A2";
        } else if (lineChart.equals(balanceChart)) {
            for (CountItem countItem : countItems) {
                int hour = countItem.getTime().getHours();
                income[hour] += countItem.getScore();
                total += countItem.getScore();
            }
            colorStr = "#5FE05D";
        }


        // 缩放
        lineChart.setScaleEnabled(true);
        // 高亮
        lineChart.setHighlightPerTapEnabled(false);
        // description
        lineChart.getDescription().setEnabled(false);
        // 边界
        lineChart.setDrawBorders(false);
        // 图例
        lineChart.getLegend().setEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setExtraBottomOffset(10f);
        // 获取X轴
        XAxis xAxis = lineChart.getXAxis();

        // 设置X轴标签为日期
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value) + "时";
            }
        });

        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0);
        leftYAxis.setEnabled(false);
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);

        // 设置数据
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries_future = new ArrayList<>();

        int cur_hour = new Timestamp(System.currentTimeMillis()).getHours();
        for (int i = 0; i < 24; i++) {
            if (i <= cur_hour)
                entries.add(new Entry(time[i], income[i]));
            else
                entries_future.add(new Entry(time[i], 0));
        }
        // 将数据赋给数据集,一个数据集表示一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        LineDataSet lineDataSet_future = new LineDataSet(entries_future, "");

        // 线宽度
        lineDataSet.setLineWidth(2.0f);
        // 显示圆点
        lineDataSet.setDrawCircles(true);
        lineDataSet_future.setDrawCircles(true);


        // 设置线条为平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet_future.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        // 设置直线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet_future.setDrawFilled(true);

        lineDataSet.setColor(Color.parseColor(colorStr));
        lineDataSet.setFillColor(Color.parseColor(colorStr));
        lineDataSet.setCircleColor(Color.parseColor(colorStr));
        lineDataSet.setCircleHoleColor(Color.parseColor(colorStr));

        lineDataSet_future.setColor(Color.parseColor(colorStr));
        lineDataSet_future.setFillColor(Color.parseColor(colorStr));
        lineDataSet_future.setCircleColor(Color.parseColor(colorStr));
        lineDataSet_future.setCircleHoleColor(Color.parseColor(colorStr));


        if (lineChart.equals(incomeChart)) {
            textView_income.setText("本日收入：" + total);

        } else if (lineChart.equals(expendChart)) {
            textView_expend.setText("本日支出：" + total);
        } else if (lineChart.equals(balanceChart)) {
            textView_balance.setText("本日结余：" + total);
        }

        LineData lineData = new LineData(lineDataSet, lineDataSet_future);
        // 曲线点的具体数值
        lineData.setDrawValues(true);

        lineChart.setData(lineData);
    }
}