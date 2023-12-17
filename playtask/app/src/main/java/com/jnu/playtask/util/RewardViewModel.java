package com.jnu.playtask.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jnu.playtask.data.RewardItem;

import java.util.ArrayList;

public class RewardViewModel extends ViewModel {
    private MutableLiveData<ArrayList<RewardItem>> dataList = new MutableLiveData<>();

    public LiveData<ArrayList<RewardItem>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<RewardItem> newData) {
        dataList.setValue(newData);
    }
}
