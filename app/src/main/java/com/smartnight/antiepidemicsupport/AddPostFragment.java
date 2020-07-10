package com.smartnight.antiepidemicsupport;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AddPostFragment extends Fragment {

    private MainMainActivity activity;
    private EditText editText;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;
    private List<ImageView> list = new ArrayList<>();
    Button button;
    Bitmap bitmap[],bitmapCompress[];
    private int PhotoNumber;
    TitleBar titleBar;

    public AddPostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activity = (MainMainActivity)getActivity();
        Log.d("sys","fragmentActivity:"+activity);
        final View view = inflater.inflate(R.layout.fragment_add_post,container,false);
        editText = view.findViewById(R.id.editText);
        imageView1 = (ImageView)view.findViewById(R.id.imageViewadd1);
        imageView2 = (ImageView)view.findViewById(R.id.imageViewadd2);
        imageView3 = (ImageView)view.findViewById(R.id.imageViewadd3);
        imageView4 = (ImageView)view.findViewById(R.id.imageViewadd4);
        imageView5 = (ImageView)view.findViewById(R.id.imageViewadd5);
        imageView6 = (ImageView)view.findViewById(R.id.imageViewadd6);
        imageView7 = (ImageView)view.findViewById(R.id.imageViewadd7);
        imageView8 = (ImageView)view.findViewById(R.id.imageViewadd8);
        imageView9 = (ImageView)view.findViewById(R.id.imageViewadd9);
        button = view.findViewById(R.id.button2);
        titleBar = view.findViewById(R.id.titleBar);
        PhotoNumber = 0;
        bitmap = new Bitmap[9];
        bitmapCompress = new Bitmap[9];

        list.add(imageView1);list.add(imageView2);list.add(imageView3);list.add(imageView4);
        list.add(imageView5);list.add(imageView6);list.add(imageView7);list.add(imageView8);list.add(imageView9);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PhotoNumber>=9){
                    Toast.makeText(activity, "最多只能选择九张图片", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    PhotoNumber++;
                    startActivityForResult(intent,PhotoNumber);
                }
            }
        });

        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.quit_action));
                builder.setPositiveButton(R.string.QUIT_PISOTIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(view).navigateUp();
                    }
                });
                builder.setNegativeButton(R.string.QUIT_NRGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                String context = editText.getText().toString();
                if(context == null){
                    Toast.makeText(requireContext(),"发表内容不可以为空",Toast.LENGTH_SHORT).show();
                }else{
                    Post post = new Post(context,bitmapCompress,123);
                    //展示给圈子界面显示
                    Navigation.findNavController(view).navigateUp();
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode != 0){
            Uri uri = data.getData();
            ContentResolver contentResolver = activity.getContentResolver();
            try {
                //原图
                Bitmap bitmapa = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                /*保存图片
                SharedPreferences shp = requireActivity().getSharedPreferences("UserFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,50,out);
                String PIC64 = new String(Base64.encodeToString(out.toByteArray(),Base64.DEFAULT));
                editor.putString("Picture",PIC64);
                editor.commit();*/
                //压缩后的图
                Bitmap bitmapCompressa = centerSquareScaleBitmap(bitmapa,250);
                bitmap[PhotoNumber-1] = bitmapa;
                bitmapCompress[PhotoNumber-1]  = bitmapCompressa;
                list.get(PhotoNumber-1).setImageBitmap(bitmapCompressa);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if(null == bitmap || edgeLength <= 0) { return  null; }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try{ scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true); }
            catch(Exception e){ return null; }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){ return null; }
        }
        return result;
    }
}
