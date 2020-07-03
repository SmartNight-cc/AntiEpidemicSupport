package com.smartnight.antiepidemicsupport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.annotation.Target;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

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


    //布局中的组件
    private MainMainActivity mainMainActivity ;
    private MeFragment meFragment;
    private Button ok,choose_pic;
    private ImageView image;
    private EditText text_name,text_addr,text_identity,text_inst;

   //获得的信息
    private Bitmap bitmap;
    private String name,addr,identity,inst;


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
        // Inflate the layout for this fragment
        mainMainActivity = (MainMainActivity) getActivity();


        View view = inflater.inflate(R.layout.fragment_edit_info_, container, false);
        ok = view.findViewById(R.id.edit_ok);
        choose_pic = view.findViewById(R.id.choose_btn);
        image = view.findViewById(R.id.edit_profile);
        //从相册选择图片
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

        //确认信息,传回和修改数据
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                //传回头像数据
                Bundle bundle = new Bundle();
                bundle.putParcelable("profile",bitmap);
                navController.navigate(R.id.action_edit_info_Fragment_to_meFragment,bundle);
                //navController.navigate(R.id.action_edit_info_Fragment_to_meFragment);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从相册选取
        if (requestCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = mainMainActivity.getContentResolver();
                // startSmallPhotoZoom(data.getData());
                try {
                    //设置编辑页面的头像
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    //压缩后的图
                    bitmap = centerSquareScaleBitmap(bitmap,250);
                    /* 将Bitmap设定到ImageView */
                    image.setImageBitmap(bitmap);
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
