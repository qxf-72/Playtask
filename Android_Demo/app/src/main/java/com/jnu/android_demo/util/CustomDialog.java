package com.jnu.android_demo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.jnu.android_demo.MainActivity;
import com.jnu.android_demo.R;

public class CustomDialog {

    public static void showCustomDialog(Context context, ViewModel viewModel) {
        // 获取布局文件
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // 初始化Spinner1
        Spinner spinner1 = dialogView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                context,
                R.array.options1,  // 定义在res/values/arrays.xml中的选项
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        // 初始化Spinner2
        Spinner spinner2 = dialogView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                context,
                R.array.options2,  // 定义在res/values/arrays.xml中的选项
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // 创建AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setTitle("排序选项")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 处理确定按钮点击事件
                    String selectedOption1 = spinner1.getSelectedItem().toString();
                    String selectedOption2 = spinner2.getSelectedItem().toString();

                    int Mode;
                    if (selectedOption1.equals(context.getResources().getString(R.string.text_creation_ime))
                            && selectedOption2.equals(context.getResources().getString(R.string.text_asc))) {
                        Mode = 1;
                    } else if (selectedOption1.equals(context.getResources().getString(R.string.text_creation_ime))
                            && selectedOption2.equals(context.getResources().getString(R.string.text_desc))) {
                        Mode = 2;
                    } else if (selectedOption1.equals(context.getResources().getString(R.string.text_score_amount))
                            && selectedOption2.equals(context.getResources().getString(R.string.text_asc))) {
                        Mode = 3;
                    } else {
                        Mode = 4;
                    }

                    // 确定排序的对象，是Task还是Reward
                    if(viewModel instanceof TaskViewModel) {
                        ((TaskViewModel) viewModel).setDataList(MainActivity.mDBMaster.mTaskDAO.queryDataList(Mode));
                    } else {
                        ((RewardViewModel) viewModel).setDataList(MainActivity.mDBMaster.mRewardDAO.queryDataList(Mode));
                    }

                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 处理取消按钮点击事件
                });

        // 显示AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
