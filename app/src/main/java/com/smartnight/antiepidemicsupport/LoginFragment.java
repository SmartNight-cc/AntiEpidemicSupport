package com.smartnight.antiepidemicsupport;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = requireActivity();
        final EditText textID,textPassWord;
        final Button buttonLogin;
        textID = activity.findViewById(R.id.editUserID);
        textPassWord = activity.findViewById(R.id.editPassword);
        buttonLogin = activity.findViewById(R.id.buttonLogin);

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

                //判断是否用户名和密码对应，否则给出错误提示toast直接return

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_postFragment);
            }
        });
    }
}
