package com.smartnight.antiepidemicsupport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMainActivity extends AppCompatActivity {
    private NavController navController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmain);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //底部导航功能
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        NavigationUI.setupActionBarWithNavController(this,navController);
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
        }
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
