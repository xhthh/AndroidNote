package com.xht.androidnote.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.xht.androidnote.base.App;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private static final String TAG = "LocationUtils";
    private Context mContext;

    private LocationManager locationManager;
    private String locationProvider = null;

    private LocationUtils() {
        mContext = App.getAppContext();
    }

    public static LocationUtils getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final LocationUtils instance = new LocationUtils();
    }


    public void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        locationProvider = getLocationProvider();
        //3.获取上次的位置，一般第一次运行，此值为null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean hasPermission = AndPermission.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasPermission) {
                getLoAndLa();
            } else {
                AndPermission.with(mContext)
                        .runtime()
                        .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                getLoAndLa();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                Toast.makeText(mContext, "请开启位置权限", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start();
            }
        } else {
            getLoAndLa();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLoAndLa() {
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            Log.e(TAG, "获取上次的位置-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
            getAddress(location);
        } else {
            //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            locationManager.requestLocationUpdates(locationProvider, 3000, 0.1F, locationListener);
        }
    }

    //获取地址信息:城市、街道等信息
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(mContext, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                Log.e(TAG, "获取地址信息：" + result.toString());
                if (result.get(0) != null) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //如果位置发生变化，重新显示地理位置经纬度
                Log.e(TAG, "监视地理位置变化-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
            }
        }
    };

    private String getLocationProvider() {
        //2获取位置提供器，GPS或是NetWork
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);

        criteria.setBearingRequired(false);

        criteria.setCostAllowed(false);

        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        return provider;
    }
}
