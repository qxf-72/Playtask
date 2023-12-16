package com.jnu.android_demo.ui.bill;

import com.jnu.android_demo.data.CountItem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class GroupModel {

    /**
     * 获取组列表数据
     */
    public static ArrayList<GroupEntity> getGroups(ArrayList<CountItem> countItems) {
        ArrayList<GroupEntity> groupedEntities = new ArrayList<>();
        Map<String, ArrayList<CountItem>> map = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd日-E", Locale.getDefault());

        for (CountItem item : countItems) {
            Timestamp timestamp = item.getTime();
            String dateKey = dateFormat.format(new Date(timestamp.getTime()));

            if (!map.containsKey(dateKey)) {
                map.put(dateKey, new ArrayList<>());
            }
            Objects.requireNonNull(map.get(dateKey)).add(item);
        }

        for (Map.Entry<String, ArrayList<CountItem>> entry : map.entrySet()) {
            groupedEntities.add(new GroupEntity(entry.getKey(), "", entry.getValue()));
        }

        return groupedEntities;
    }
}