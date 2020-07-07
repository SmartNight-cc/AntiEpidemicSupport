package com.smartnight.antiepidemicsupport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatFragment extends Fragment implements View.OnClickListener{
    String name,password,number;
    String APPKEY = "2ff7c6103ee38";
    String APPSECTET = "c0cbe83fc7ffc6cc9a8abc7af9095a9b";
    private int i = 50;//计时器
    private EditText editname,editpass,editnum,editsms;
    private Button button,buttonsms;
    TitleBar titleBar;

    public CreatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_creat, container, false);

        //如果 targetSdkVersion小于或等于22，可以忽略这一步，如果大于或等于23，需要做权限的动态申请：
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(requireActivity(), mPermissionList, 123);
        }

        editname = view.findViewById(R.id.editText3);
        editpass = view.findViewById(R.id.editText4);
        editnum = view.findViewById(R.id.editText5);
        editsms = view.findViewById(R.id.editsms);
        button = view.findViewById(R.id.button3);
        buttonsms = view.findViewById(R.id.buttongetsms);
        button.setOnClickListener(this);
        buttonsms.setOnClickListener(this);
        titleBar = view.findViewById(R.id.titleBar);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                Navigation.findNavController(requireActivity(),R.id.fragment3).navigateUp();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        MobSDK.init(requireActivity(),APPKEY,APPSECTET);
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int i, int i1, Object o) {
                Message message = new Message();
                message.arg1 = i;
                message.arg2 = i1;
                message.obj = o;
                handler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

        button.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = editname.getText().toString();
                password = editpass.getText().toString();
                number = editnum.getText().toString();
                button.setEnabled(!name.isEmpty() && !password.isEmpty() && !number.isEmpty());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        editname.addTextChangedListener(textWatcher);
        editnum.addTextChangedListener(textWatcher);
        editpass.addTextChangedListener(textWatcher);

        return view;
    }

    @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == -9){
                buttonsms.setText("重新发送("+i+")");
            }else if(msg.what == -8){
                buttonsms.setText(R.string.getSMS);
                buttonsms.setClickable(true);
                i = 30;
            }else{
                int i = msg.arg1;
                int i1 = msg.arg2;
                Object o = msg.obj;
                if(i1 == SMSSDK.RESULT_COMPLETE){
                    //短信验证成功
                    if(i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Intent intent =new Intent(getActivity(),MainMainActivity.class);
                        startActivity(intent);
                    }else if(i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
                        Toast.makeText(requireContext(),"正在发送验证码",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        String phoneNumber = editnum.getText().toString();
        switch (view.getId()){
            case R.id.buttongetsms:
                if(!isMoblieNumber(phoneNumber)){
                    Toast.makeText(requireContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86",phoneNumber);
                buttonsms.setClickable(false);
                buttonsms.setText("重新发送("+i+")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(;i>0;i--){
                            handler.sendEmptyMessage(-9);
                            if(i<=0){
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.button3:
                if(editsms == null){
                    Toast.makeText(requireContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                }
                SMSSDK.submitVerificationCode("86",phoneNumber,editsms.getText().toString());
                break;
        }
    }

    //正则表达式验证手机号格式
    public static boolean isMoblieNumber(String number){
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         * 手机号位数为11
         */
        if(number.length()!=11){
            return false;
        }
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number))
            return false;
        else
            return number.matches(telRegex);
    }

    @Override
    public void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
