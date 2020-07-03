package com.smartnight.antiepidemicsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMainActivity extends AppCompatActivity {

    private  NavController navController;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmain);

        //设置非物理back键
        if(findViewById(R.id.fragment)!=null){
            navController = Navigation.findNavController(findViewById(R.id.fragment));
        }
        NavigationUI.setupActionBarWithNavController(this,navController);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        navController.navigateUp();
    }

    //左上角箭头
    @Override
    public boolean onSupportNavigateUp() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        //返回页不保留键盘
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}
