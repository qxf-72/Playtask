package com.jnu.android_demo.ui.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jnu.android_demo.R;
import com.jnu.android_demo.util.CountViewModel;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MeViewFragment extends Fragment {

    private CountViewModel countViewModel;

    public MeViewFragment() {
    }

    public static MeViewFragment newInstance() {
        return new MeViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countViewModel = new ViewModelProvider(requireActivity()).get(CountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me_view, container, false);

        // 设置toolbar
        setToolbar(rootView);

        // 设置头像和背景效果
        setHeadAndBack(rootView);

        // 设置响应
        setResponse(rootView);

        return rootView;
    }


    /**
     * 设置toolbar
     */
    private void setToolbar(View rootView) {
        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);
        rootView.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        });
    }


    /**
     * 设置头像和背景效果
     */
    private void setHeadAndBack(View rootView) {
        ImageView mHBack = rootView.findViewById(R.id.h_back);
        ImageView mHHead = rootView.findViewById(R.id.h_head);
        // 背景磨砂效果
        Glide.with(this)
                .load(R.drawable.user_center_background)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(5)))
                .into(mHBack);
        // 圆形图像效果
        Glide.with(this)
                .load(R.drawable.user_center_avatar)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(mHHead);
    }


    /**
     * 设置响应
     */
    private void setResponse(View rootView) {
        RelativeLayout budget_btn, help_btn, update_btn, coffee_btn;  //我的积分，使用帮助，检查更新，coffee
        budget_btn = rootView.findViewById(R.id.budget_btn);
        help_btn = rootView.findViewById(R.id.help_btn);
        update_btn = rootView.findViewById(R.id.update_btn);
        coffee_btn = rootView.findViewById(R.id.coffee_btn);

        // “我的积分”点击事件
        budget_btn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("积分查询");
            builder.setMessage("当前累计积分为：" + countViewModel.getData().getValue() + " 。\n\n"+
                    "详细收支信息，请在“统计”页面，点击右上角“账单”进行查看 。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            // 创建并显示对话框
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // “使用帮助”点击事件
        help_btn.setOnClickListener(v -> {
            // 创建一个 Intent，动作为 ACTION_VIEW，指定 Uri 为要打开的网页的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zhihu.com/question/360644017"));
            // 启动浏览器
            startActivity(intent);
        });

        update_btn.setOnClickListener(v -> {
            // 创建一个 Toast 对象，设置提示信息和时长
            Toast toast = Toast.makeText(requireContext(), "JNU版PlayTask 1.0.0 ，最新版本", Toast.LENGTH_SHORT);
            // 设置 Toast 的显示位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        coffee_btn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("给作者点赞");
            builder.setMessage("原作者多年前就放弃了这个项目，故在此不再放出原作者的支付宝链接。\n" +
                    "如果您觉得这个项目对您有帮助，请到Github上面给这个项目点一个star，感谢！\n\n" +
                    "注：本项目仅供学习交流，不得用于商业用途，如有侵权，请联系我删除。");
            builder.setPositiveButton("关闭对话框", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("跳转到Github项目仓库", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/qxf-72/Playtask"));
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }


}