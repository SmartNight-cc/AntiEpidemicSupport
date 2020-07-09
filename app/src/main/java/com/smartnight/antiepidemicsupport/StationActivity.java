package com.smartnight.antiepidemicsupport;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 *中转站：收集展示本地用户的物资种类和数量
 *需求是本地解决还是上报，由算法直接计算得出，中转站负责人只做结果和数据查看，实施行动
 *显示接收到的救援物品到达了，通知被救助者
 *显示我们需要对某地提供捐献，请发送物品到XXX地址
 */

public class StationActivity extends AppCompatActivity {
    NavController navController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_station);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        navController = Navigation.findNavController(this,R.id.fragment2);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}
