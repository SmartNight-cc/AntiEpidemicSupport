package com.smartnight.antiepidemicsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatFragment extends Fragment {
    String name,password,number;

    public CreatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creat, container, false);

        final EditText editname,editpass,editnum;
        editname = view.findViewById(R.id.editText3);
        editpass = view.findViewById(R.id.editText4);
        editnum = view.findViewById(R.id.editText5);
        final Button button = view.findViewById(R.id.button3);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断信息是否合法
                Log.d("密码",password);
                Log.d("手机号",number);
                if(password.length()>=6 && password.length()<=16 && number.length() == 11){
                    //合法，创建新用户，生成ID，保存到本地跳转
                    SharedPreferences shp = requireActivity().getSharedPreferences("UserFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shp.edit();
                    editor.putString("Name",name);
                    editor.putString("PassWord",password);
                    editor.putString("Number",number);
                    editor.apply();
                    Toast.makeText(requireActivity(),R.string.NewCount, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_creatFragment_to_loginFragment);
                }else{
                    Toast.makeText(requireActivity(),R.string.NoNewCount, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
