package com.smartnight.antiepidemicsupport;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText textID,textPassWord;
        final Button buttonLogin;
        textID = findViewById(R.id.editUserID);
        textPassWord = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        //两个输入框都有内容时登陆按键才可点击
        buttonLogin.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String id = textID.getText().toString().trim();
                String password = textPassWord.getText().toString().trim();
                buttonLogin.setEnabled(!id.isEmpty() && !password.isEmpty());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        textID.addTextChangedListener(textWatcher);
        textPassWord.addTextChangedListener(textWatcher);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                String id = textID.getText().toString().trim();
                String password = textPassWord.getText().toString().trim();

                //判断是否用户名和密码对应，否则给出错误提示toast直接return

                Intent intent = new Intent(MainActivity.this,MainMainActivity.class);
                startActivity(intent);
            }
        });
    }
}