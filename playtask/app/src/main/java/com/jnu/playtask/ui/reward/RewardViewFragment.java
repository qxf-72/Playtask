package com.jnu.playtask.ui.reward;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.playtask.MainActivity;
import com.jnu.playtask.R;
import com.jnu.playtask.data.CountItem;
import com.jnu.playtask.data.RewardDAO;
import com.jnu.playtask.data.RewardItem;
import com.jnu.playtask.util.CountViewModel;
import com.jnu.playtask.util.CustomDialog;
import com.jnu.playtask.util.RewardViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;


public class RewardViewFragment extends Fragment {
    private ActivityResultLauncher<Intent> addItem_launcher;
    private ActivityResultLauncher<Intent> updateItem_launcher;
    private RecyclerView recyclerView;
    private ArrayList<RewardItem> rewardItems;

    private RewardViewModel rewardViewModel;
    private CountViewModel countViewModel;


    public RewardViewFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 导入数据库中的数据
        rewardItems = (ArrayList<RewardItem>) MainActivity.mDBMaster.mRewardDAO.queryDataList(RewardDAO.NO_SORT);
        if (rewardItems == null) {
            rewardItems = new ArrayList<>();
        }
        countViewModel = new ViewModelProvider(requireActivity()).get(CountViewModel.class);
        rewardViewModel = new ViewModelProvider(requireActivity()).get(RewardViewModel.class);
    }


    /**
     * 在创建视图时加载布局
     */
    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reward_view, container, false);


        // 表明Fragment会添加菜单项
        setHasOptionsMenu(true);
        rootView.post(() -> {
            // 获取主页面的Toolbar引用
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        });


        // 初始化recycleView
        recyclerView = rootView.findViewById(R.id.recycle_view_reward_task);
        RewardRecycleViewAdapter taskRecycleViewAdapter = new RewardRecycleViewAdapter(requireActivity(), rewardItems);
        // 设置单击和长按监听器
        taskRecycleViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理点击事件——弹出对话框，确认是否兑换
                showRewardConfirmationDialog(position);
            }
        });
        taskRecycleViewAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理长按事件——弹出menu，选择编辑或删除
                showPopupMenuOnItemLongClick(view, position);
                return true;
            }
        });
        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        // 屏幕底部显示累积积分Count
        TextView textView_count = rootView.findViewById(R.id.textview_count);
        textView_count.setText("总积分点：" + countViewModel.getData());


        // 监听Count数据变化
        countViewModel.getData().observe(getViewLifecycleOwner(), integer -> {
            textView_count.setText("总积分点：" + integer);
        });


        // 监听数据变化
        rewardViewModel.getDataList().observe(getViewLifecycleOwner(), newData -> {
            rewardItems.clear();
            rewardItems.addAll(newData);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        });


        // 获取从AddRewardItemActivity返回的数据
        addItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        // 获取从AddRewardItemActivity返回的数据
                        Intent intent = result.getData();
                        if (intent == null) {
                            throw new IllegalStateException("Unexpected value: " + result);
                        }
                        String name = intent.getStringExtra("name");
                        int score = intent.getIntExtra("score", 0);
                        int type = intent.getIntExtra("type", 0);

                        RewardItem rewardItem = new RewardItem(new Timestamp(System.currentTimeMillis()), name, score, type, 0);

                        // 插入数据库
                        rewardItem.setId(MainActivity.mDBMaster.mRewardDAO.insertData(rewardItem));

                        // 添加数据
                        rewardItems.add(rewardItem);

                        // 更新recycleView
                        taskRecycleViewAdapter.notifyDataSetChanged();

                    }
                });
        // 获取从AddRewardItemActivity返回的数据，来更新数据
        updateItem_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null)
                            return;

                        int position = intent.getIntExtra("position", 0);
                        // 修改数据
                        RewardItem rewardItem = rewardItems.get(position);
                        rewardItem.setTime(new Timestamp(System.currentTimeMillis()));
                        rewardItem.setName(intent.getStringExtra("name"));
                        rewardItem.setScore(intent.getIntExtra("score", 0));
                        rewardItem.setType(intent.getIntExtra("type", 0));
                        // 更新数据库
                        MainActivity.mDBMaster.mRewardDAO.updateData((int) rewardItem.getId(), rewardItem);

                        Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(position);
                    }
                });


        // 设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(requireContext().getResources().getDrawable(R.drawable.divider_drawable));
        recyclerView.addItemDecoration(itemDecoration);


        return rootView;
    }


    /**
     * 加载toolbar菜单
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * 处理toolbar菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            showPopupMenuOnClickAdd(item.getActionView());
            return true;
        } else if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 点击“+”之后，执行弹出菜单
     */
    private void showPopupMenuOnClickAdd(View view) {
        String[] options = {"新建奖励", "排序"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                // 处理新建菜单项点击事件
                case 0:
                    Intent intent = new Intent(requireContext(), AddRewardItemActivity.class);
                    addItem_launcher.launch(intent);
                    break;
                // 处理排序菜单项点击事件
                case 1:
                    CustomDialog.showCustomDialog(requireContext(), rewardViewModel);
                    break;
            }
        });

        builder.show();
    }


    /**
     * 长按Item，执行弹出菜单
     */
    private void showPopupMenuOnItemLongClick(View view, int position) {
        String[] options = {"编辑", "删除"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                // 处理编辑菜单项点击事件
                case 0:
                    Intent intentUpdate = new Intent(requireActivity(), AddRewardItemActivity.class);
                    RewardItem rewardItem = rewardItems.get(position);
                    intentUpdate.putExtra("position", position);
                    intentUpdate.putExtra("name", rewardItem.getName());
                    intentUpdate.putExtra("score", rewardItem.getScore());
                    intentUpdate.putExtra("type", rewardItem.getType());

                    updateItem_launcher.launch(intentUpdate);

                    break;
                // 处理删除菜单项点击事件
                case 1:

                    MainActivity.mDBMaster.mRewardDAO.deleteData((int) rewardItems.get(position).getId());
                    rewardItems.remove(position);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);

                    break;
            }
        });

        builder.show();
    }


    /**
     * 显示删除确认对话框
     */
    private void showRewardConfirmationDialog(int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("满足奖励")
                .setMessage("确定花费 " + rewardItems.get(position).getScore() + " 积分来满足该项奖励吗？")
                .setPositiveButton(getResources().getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RewardItem rewardItem = rewardItems.get(position);

                        // 更新Count数据
                        MainActivity.mDBMaster.mCountDAO.insertData(new CountItem(new Timestamp(System.currentTimeMillis()), rewardItem.getName(), -rewardItem.getScore()));
                        countViewModel.setData(countViewModel.getData().getValue() - rewardItem.getScore());

                        if (rewardItem.getType() == 0) {
                            // 单次
                            MainActivity.mDBMaster.mRewardDAO.deleteData((int) rewardItem.getId());
                            rewardItems.remove(position);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);
                        } else {
                            // 多次
                            rewardItem.setFinishedAmount(rewardItem.getFinishedAmount() + 1);
                            MainActivity.mDBMaster.mRewardDAO.updateData((int) rewardItem.getId(), rewardItem);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(position);
                        }

                    }
                })
                .setNegativeButton(getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 关闭对话框
                        dialog.dismiss();
                    }
                })
                .show();
    }

}