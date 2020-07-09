package com.smartnight.antiepidemicsupport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText textID,textPassWord;
    private Button buttonLogin;
    private TextView textCreate;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textID = (EditText)view.findViewById(R.id.editUserID2);
        textPassWord = (EditText)view.findViewById(R.id.editPassword2);
        buttonLogin = (Button)view.findViewById(R.id.buttonLogin2);
        textCreate = (TextView)view.findViewById(R.id.textNewAccount2);

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
                String id = textID.getText().toString().trim();
                String password = textPassWord.getText().toString().trim();
                //判断：中转站账号五位，用户大于等于六位
                if(id.length()<=5){
                    //根据账号从服务器获取密码，判断是否正确，若正确获取其他信息保存到本地
                    Intent intent =new Intent(getActivity(),StationActivity.class);
                    startActivity(intent);
                    /*if(realpassWord.equals(password)){
                        Intent intent =new Intent(getActivity(),StationActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(requireActivity(),R.string.wrong, Toast.LENGTH_SHORT).show();
                    }*/
                }else{
                    //根据账号从服务器获取密码
                    Intent intent =new Intent(getActivity(),MainMainActivity.class);
                    startActivity(intent);
                    /*SharedPreferences shp = getActivity().getSharedPreferences("UserFile", Context.MODE_PRIVATE);
                    String realpassWord = shp.getString("PassWord",null);
                    if(realpassWord.equals(password)){
                        Intent intent =new Intent(getActivity(),MainMainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(requireActivity(),R.string.wrong, Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        });

        textCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_creatFragment);
            }
        });
        return view;
    }
}
