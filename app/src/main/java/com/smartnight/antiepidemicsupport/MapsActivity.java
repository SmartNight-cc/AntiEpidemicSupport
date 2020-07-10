package com.smartnight.antiepidemicsupport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private Button button;
    private Location location;//当前设备的地址
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TitleBar titleBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        button = findViewById(R.id.startOtherMap);
        titleBar = findViewById(R.id.titleBar);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                handleLocation(locationResult.getLastLocation());
            }
        };

        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                Intent intent =new Intent(getApplicationContext(),MainMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        //获取当前设备地址，成功
        getLocation();
        Toast.makeText(this,location.toString(),Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //获取当前设备地址，设置location
    public void getLocation() {
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //请求开启位置权限
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("使用Google Map导航需要开启定位权限");
            builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    open();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //跳转回mainmainactivity
                    back();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider,3000,1,locationListener);
    }

    private void open(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.getApplicationContext().startActivity(intent);
    }

    private void back(){
        Intent intent =new Intent(this,MainMainActivity.class);
        startActivity(intent);
    }

    //组合成googlemap direction所需要的url
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Travelling Mode
        String mode = "mode=driving";
        String parameters = null;
        // Building the parameters to the web service
        parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=AIzaSyAMU4eyQvmq-DH-lV43B3frToKQb7vZvts";
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.google.com/maps/api/directions/" + output + "?" + parameters;
        System.out.println("getDerectionsURL--->: " + url);
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng start = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(start).title("你的位置"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));

        //获取中转站经纬度
        final LatLng end = new LatLng(37.07,114.48);
        //获取请求url
        final String url = getDirectionsUrl(start,end);

        //选择谷歌地图或浏览器打开
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");

                String line = "https://www.google.com/maps/dir/?api=1&origin="
                +location.getLatitude()+","+location.getLongitude()+
                        "&destination="+end.latitude+","+end.longitude;
                intent.setData(Uri.parse(line));
                startActivity(intent);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getApplicationContext(),"找不到路线",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String line = response.body().string();
                System.out.println("getResponse--->: " + line);
                drawPath(line);
            }
        });

        requestLocationPermission();
    }

    private void drawPath(String result){
        try {
            final JSONObject jsonObject = new JSONObject(result);
            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            String statusString = jsonObject.getString("status");
            Log.d("test: ", encodedString);
            List<LatLng> list = decodePoly(encodedString);
            LatLng last = null;
            for (int i = 0; i < list.size()-1; i++) {
                LatLng src = list.get(i);
                LatLng dest = list.get(i+1);
                last = dest;
                Log.d("Last latLng:", last.latitude + ", " + last.longitude );
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(4)
                        .color(Color.GREEN));
            }
            Log.d("Last latLng:", last.latitude + ", " + last.longitude );
        }catch (JSONException e){
            e.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("Caught ArrayIndexOutOfBoundsException: "+ e.getMessage());
        }
        /*int begin = string.indexOf("<points>");
        int end = string.indexOf("</points>");
        String points = string.substring(begin+8,end);
        System.out.println("getPoints--->: " + points);
        List<LatLng> path = PolyUtil.decode(points);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(path);
        polylineOptions.color(Color.GREEN);
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.width(15f);
        mMap.addPolyline(polylineOptions);*/
    }

    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0;
        int length = encoded.length();
        int latitude = 0;
        int longitude = 0;
        while(index < length){
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int destLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            latitude += destLat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b > 0x20);
            int destLong = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            longitude += destLong;
            poly.add(new LatLng((latitude / 1E5),(longitude / 1E5) ));
        }
        return poly;
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate(){
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
        );
    }

    //停止获取位置更新
    private void stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    private void handleLocation(Location location){
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        //地图中心位置移到定位位置，设置缩放等级为15
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
        //定位成功后可停止获取位置更新
        stopLocationUpdate();
    }

    private void requestLocationService(){
        if(isLocationEnabled(this)){
            requestLocationUpdate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission(){
        if(locationPermission != null){
            requestLocationService();
        }else{
            String[] location = {locationPermission};
            requestPermissions(location,1);
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length !=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            requestLocationUpdate();
        }
    }
}
