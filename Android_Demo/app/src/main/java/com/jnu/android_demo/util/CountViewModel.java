package com.jnu.android_demo.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * 用于主界面和Task\Reward子界面之间共享数据
 */
public class CountViewModel extends androidx.lifecycle.ViewModel{
    private MutableLiveData<Integer> data = new MutableLiveData<>();

    public LiveData<Integer> getData() {
        return data;
    }

    public void setData(int newData) {
        data.setValue(newData);
    }
}
