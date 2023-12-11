package com.jnu.android_demo.ui.task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jnu.android_demo.data.TaskItem;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TaskItem>> dataList = new MutableLiveData<>();

    public LiveData<ArrayList<TaskItem>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<TaskItem> newData) {
        dataList.setValue(newData);
    }

    public void addData(TaskItem taskItem) {
        ArrayList<TaskItem> newData = dataList.getValue();
        if (newData != null) {
            newData.add(taskItem);
        }
        dataList.setValue(newData);
    }
}
