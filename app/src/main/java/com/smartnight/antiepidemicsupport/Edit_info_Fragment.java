package com.smartnight.antiepidemicsupport;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_info_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Edit_info_Fragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int RESULT_OK = 1;

    SharedPreferences shp;
    private SharedPreferences.Editor editor;
    private MainMainActivity mainMainActivity ;
    //布局中的组件
    private ImageView image;
    //可修改的信息
    private String name,addr,pwd;
    private Bitmap bitmap;

    public Edit_info_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_info_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_info_Fragment newInstance(String param1, String param2) {
        Edit_info_Fragment fragment = new Edit_info_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //得到SharedPreferences
        shp = requireActivity().getSharedPreferences("UserFile", Context.MODE_PRIVATE);
        editor = shp.edit();
        //获得父Activity
        mainMainActivity = (MainMainActivity) getActivity();
        //找到组件
        View view = inflater.inflate(R.layout.fragment_edit_info_, container, false);
        Button ok = view.findViewById(R.id.edit_ok);//确认按钮
        Button choose_pic = view.findViewById(R.id.choose_btn);//选择照片按钮
        image = view.findViewById(R.id.edit_profile);//头像组件
        TextView show_identity = view.findViewById(R.id.show_identity);
        TextView show_ID = view.findViewById(R.id.show_ID);
        //输入框
        EditText text_name = view.findViewById(R.id.input_name);
        EditText text_addr = view.findViewById(R.id.input_addr);
        EditText text_pwd = view.findViewById(R.id.input_pwd);

        //显示原来的信息
        //showInfo(text_name, show_ID,text_addr, show_identity,text_pwd);

        //昵称输入框监听器事件
        text_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
            }
        });

        //地址输入框监听器事件
        text_addr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                addr = editable.toString();
            }
        });

        //密码输入框监听器事件
        text_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pwd = editable.toString();
            }
        });

        //从相册选择图片事件
        choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, RESULT_OK);

            }
        });

        //确认信息,传回和修改数据事件,待完善
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                //修改个人信息
                if(bitmap!=null){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();//头像
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);
                    String PIC64 = new String(Base64.encodeToString(out.toByteArray(), android.util.Base64.DEFAULT));
                    editor.putString("Picture",PIC64);
                }
                if(name!=null){
                    editor.putString("Name",name);//姓名
                }
                if(addr!=null){
                    editor.putString("Address",addr);//地址
                }
                if(pwd!=null){
                    editor.putString("Password",pwd);//密码
                }
                editor.commit();
                //返回个人资料页面
                navController.navigate(R.id.action_edit_info_Fragment_to_meFragment);
            }
        });
        return view;
    }

    /**
     *  显示原来的信息到界面上
     */
    public void showInfo(EditText text_name, TextView show_ID, EditText text_addr, TextView show_identity, EditText text_pwd){
        HashMap <String,String> map = GetInfo();
        text_name.setHint(map.get("Name"));
        show_ID.setText(map.get("ID"));
        text_addr.setHint(map.get("Address"));
        show_identity.setText(map.get("Identity"));
        text_pwd.setHint(map.get("PassWord"));
        //头像
        String pic = map .get("Picture");
        if(pic!=null){
            byte[] bytes = Base64.decode(pic.getBytes(),1);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
        }
    }
    /**
     *  得到原来的信息
     */
    public HashMap<String,String>GetInfo(){

        HashMap<String,String> map = new HashMap<>();
        map.put("Picture",shp.getString("Picture",""));
        map.put("Name",shp.getString("Name",""));
        map.put("ID",shp.getString("ID",""));
        map.put("Identity",shp.getString("Identity",""));
        map.put("Address",shp.getString("Adress",""));
        map.put("PassWord",shp.getString("PassWord",""));
        return map;
    }
    /**
     *  处理照片选取结果
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从相册选取
        if (requestCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = mainMainActivity.getContentResolver();
            // startSmallPhotoZoom(data.getData());
            try {
                if(uri!=null) {
                    //设置编辑页面的头像
                    //获得的信息
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    //压缩后的图
                    bitmap = centerSquareScaleBitmap(bitmap, 250);
                    /* 将Bitmap设定到ImageView */
                    image.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if(null == bitmap || edgeLength <= 0) {
            return  null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }
        return result;
    }
}