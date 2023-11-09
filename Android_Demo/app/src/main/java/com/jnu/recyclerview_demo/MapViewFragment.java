package com.jnu.recyclerview_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MapViewFragment extends Fragment {

    private MapView mapView;


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = rootView.findViewById(R.id.mapView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        TencentMap tencentMap = mapView.getMap();

        tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE); //设置地图类型

        LatLng position = new LatLng(22.250093,113.534267); //地图中心点坐标(暨南大学珠海校区)
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        position,   //坐标
                        16,         //目标缩放级别
                        0f,         //目标倾斜角[0.0 ~ 45.0] (垂直地图时为0)
                        0f));       //目标旋转角 0~360° (正北方为0)

        tencentMap.moveCamera(cameraSigma); //移动地图


        BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(R.drawable.jnu_icon);
        Marker marker = tencentMap.addMarker(new MarkerOptions(position)
                .icon(custom)
                .alpha(1f)
                .flat(true)
                .clockwise(false)
                .rotation(0)
                .title("暨南大学珠海校区")
        );

        // 设置InfoWindow的内容
        marker.setSnippet("这里是暨南大学珠海校区，北纬22°，东经113°.\n\n"+
                        "暨南大学珠海校区（Jinan University Zhuhai Campus）\n"+
                        "是暨南大学的一个分校区，位于广东省珠海市。暨南大\n" +
                        "学于1998年在珠海成立珠海学院，并于1998年8月28日\n"+
                        "与珠海市人民政府签订《共建暨南大学珠海学院协议》。\n"+
                        "2009年4月1日，学校实施校区化改革，珠海学院更名为\n"+
                        "暨南大学珠海校区。\n"
        );

        //设置Marker支持点击
        marker.setClickable(true);

        //设置Marker点击事件监听
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getId().equals(marker.getId())) {
                    //自定义Marker被点击
                    marker.showInfoWindow(); //显示InfoWindow
                }
                return true;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();

    }
}