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
import android.widget.Button;

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
import com.google.android.gms.maps.model.PolylineOptions;

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

        //获取当前设备地址，成功
        getLocation();
        System.out.println("getLocation--->: " + location.toString());

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        String waypointLatLng = "waypoints="+"40.036675"+","+"116.32885";
        // 如果使用途径点，需要添加此字段 String waypoints = "waypoints=";
        String parameters = null;
        // Building the parameters to the web service
        parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" +waypointLatLng;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        System.out.println("getDerectionsURL--->: " + url);
        return url;
    }

    //请求url得到json数据
    private String getjson(String strurl) throws IOException{
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            data = stringBuffer.toString();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //异步获取json数据
    private class GetJsonTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try{
                data = getjson(strings[0]);
            } catch (IOException e) {
                System.out.println("getjsonError!");
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(s);
        }
    }

    //异步操作，转换json数据显示在地图上
    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String, String>>>>{
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject object;
            List<List<HashMap<String, String>>> routes = null;
            try{
                object = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for(int i=0;i<lists.size();i++){
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = lists.get(i);
                for(int j=0;j<path.size();j++){
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat,lng);
                    points.add(position);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(5);
                polylineOptions.color(Color.GREEN);
                polylineOptions.jointType(JointType.ROUND);
            }
            mMap.addPolyline(polylineOptions);
        }
    }

    //解析json获取路线点集
    private class DirectionJSONParser{
        public List<List<HashMap<String,String>>> parse(JSONObject jsonObject){
            List<List<HashMap<String,String>>> routes = new ArrayList<List<HashMap<String,String>>>();
            JSONArray jRoutes = null , jLegs = null , jSteps = null;
            try{
                jRoutes = jsonObject.getJSONArray("routes");
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ((JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String,String>>();
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            for(int l=0;l<list.size();l++){
                                HashMap<String,String> hm = new HashMap<String,String>();
                                hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){

            }
            return routes;
        }
        //解析返回的xml
        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;
                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        /*google demo
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap = googleMap;
        LatLng start = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(start).title("你的位置"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        //获取中转站经纬度
        LatLng end = new LatLng(37.07,114.48);
        //获取请求url
        final String url = getDirectionsUrl(start,end);

        //选择谷歌地图或浏览器打开
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        GetJsonTask task = new GetJsonTask();
        task.execute(url);
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
