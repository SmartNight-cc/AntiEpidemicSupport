package com.smartnight.antiepidemicsupport;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class MainMainActivity extends AppCompatActivity {
    private NavController navController;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_mainmain);

        SharedPreferences shp = this.getSharedPreferences("UserFile",MODE_PRIVATE);
        SharedPreferences.Editor editor  = shp.edit();
        //初始化shp
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.pic3);
        ByteArrayOutputStream out = new ByteArrayOutputStream();//头像
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);
        String PIC64 = new String(Base64.encodeToString(out.toByteArray(), android.util.Base64.DEFAULT));
        editor.putString("Picture",PIC64);
        editor.putString("Address","Hunan_changsha");
        editor.putString("name","lili");
        editor.putString("password","123456");
        editor.putString("id","110");
        editor.putString("Identity","Hospital");
        editor.commit();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //底部导航功能
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView1);
        navController = Navigation.findNavController(this,R.id.fragment2);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(navController.getCurrentDestination().getId() == R.id.addPostFragment){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.quit_action));
            builder.setPositiveButton(R.string.QUIT_PISOTIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    navController.navigateUp();
                }
            });
            builder.setNegativeButton(R.string.QUIT_NRGATIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            //其他页面的非物理back键功能
            navController.navigateUp();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment2).getWindowToken(),0);
        //返回页不保留键盘
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
