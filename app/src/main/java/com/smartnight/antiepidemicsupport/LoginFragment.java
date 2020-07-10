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

import com.google.common.OperationCode;
import com.google.common.Request;
import com.google.common.Response;
import com.google.common.TableBean;
import com.google.common.TableCode;
import com.google.common.UserInfoBean;
import com.mob.wrappers.UMSSDKWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText textID,textPassWord;
    private Button buttonLogin;
    private TextView textCreate;

    private volatile Socket socket;
    private String host = "47.100.10.199";
    private int port = 9999;

    private ExecutorService mThreadPool;

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

        mThreadPool = Executors.newCachedThreadPool();
        //clientConServer();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textID.getText().toString().equals("410000")){
                    if(textPassWord.getText().toString().equals("123456")){
                        Intent intent = new Intent(requireActivity(),StationActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(requireContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //operation();
                    SharedPreferences shp = requireActivity().getSharedPreferences("UserFile", Context.MODE_PRIVATE);
                    String password = shp.getString("password","");
                    Log.d("TAG:password=",password);
                    if(textPassWord.getText().toString().equals(password)){
                        Intent intent = new Intent(requireActivity(),MainMainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(requireContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
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
    public void clientConServer() {
        Log.d("TAG", "开始连接服务器");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TAG", "开始连接服务器 进入run");
                    socket = new Socket(host, port);
                    socket.setSoTimeout(2000);
                    if (socket == null) {
                        Log.d("TAG", "socket 为空");
                        socket = new Socket(host, port);
                    }
                    Log.d("TAG", "连接是否成功" + socket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void operation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    Request request = new Request();// Request对象
                    request.setTableCode(TableCode.USERINFO);//需要操作的表为 user_info
                    request.setOperationCode(OperationCode.SELECT);//动作：遍历所有
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(Integer.parseInt(textID.getText().toString()));
                    request.setTableBean(userInfoBean);
                    oos.writeObject(request);// 发送request
                    Log.d("TAG","request发送成功");

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Response response = (Response) ois.readObject();
                    Log.d("TAG","接收到response");

                    List<TableBean> list = response.getTableBeanList();//遍历response 中的list（返回的结果）
                    for(int i=0;i<list.size();i++){
                        UserInfoBean userInfoBean1 = (UserInfoBean) list.get(i);
                        SharedPreferences shp = requireActivity().getSharedPreferences("UserFile",Context.MODE_PRIVATE);
                        shp.edit().putString("name",userInfoBean1.getUser_name())
                                .putString("password",userInfoBean1.getUser_password())
                                .putInt("id",userInfoBean1.getUser_id())
                                .putString("phone",userInfoBean1.getUser_phone())
                                .commit();
                        Log.d("TAG",userInfoBean1.getUser_name()+" "+userInfoBean1.getUser_id()+" "+
                                userInfoBean1.getUser_password());
                    }
                } catch (ClassNotFoundException e) { e.printStackTrace(); }
                  catch (IOException e) { e.printStackTrace(); }
            }
        }).start();
    }
}
