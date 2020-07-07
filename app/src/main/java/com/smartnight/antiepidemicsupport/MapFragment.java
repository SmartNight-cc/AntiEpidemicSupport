package com.smartnight.antiepidemicsupport;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    private static MapFragment mapFragment = null;
    private MapView mapView;
    private GoogleMap map;
    private View mapLayout;
    private int CURRENT_MODEL;//记录当前地图模式，避免重复消耗

    public MapFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        if(mapFragment ==null){
            synchronized (MapFragment.class){
                if(mapFragment == null){
                    mapFragment = new MapFragment();
                }
            }
        }
        return mapFragment;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //记录上一次选择的模式，如果连续两次选择相同的，给出已在当前模式的提示
        if(CURRENT_MODEL == item.getItemId()){
            Toast.makeText(getActivity().getApplicationContext(),R.string.map_message+item.getTitle().toString(),Toast.LENGTH_LONG).show();
        }else{
            CURRENT_MODEL = item.getItemId();
            switch(item.getItemId()){
                case R.id.item_normal:
                    break;
                case R.id.item_navigation:
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mapmenu,menu);
        CURRENT_MODEL = R.id.item_normal;//初始化为普通模式

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //如果提交的地点没有匹配，给出提示
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //显示备选地点
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        if(mapLayout == null){
            Log.i("sys","MF onCreatView() null");
            mapLayout = inflater.inflate(R.layout.fragment_map,null);
            mapView = mapLayout.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            if(map == null){
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng sydney = new LatLng(-34, 151);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }
                });
            }
        }else{
            if(mapLayout.getParent() !=null){
                ((ViewGroup)mapLayout.getParent()).removeView(mapLayout);
            }
        }
        return mapLayout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        Log.i("sys","mf onResume");
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        Log.i("sys","mf onPause");
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        Log.i("sys","mf onDestroy");
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("sys","mf onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}