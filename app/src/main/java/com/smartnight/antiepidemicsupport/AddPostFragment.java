package com.smartnight.antiepidemicsupport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import com.giftedcat.picture.lib.PictureUseHelpr;
import com.giftedcat.picture.lib.selector.MultiImageSelector;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_CANCELED;
import static com.giftedcat.picture.lib.selector.MultiImageSelectorActivity.EXTRA_RESULT;


/**
 * A simple {@link Fragment} subclass.
 * luban压缩图片，Glide加载图片，Matisse选择图片
 */
public class AddPostFragment extends Fragment {

    private MainMainActivity activity;
    RecyclerView recyclerView;
    EditText editText;
    private ArrayList<String> listImagePath;//选择的图片
    private ArrayList<String> mList = new ArrayList<>();//recycleview要展示的图片
    private ArrayList<String> list = new ArrayList<>();
    private ReleaseMsgAdapter adapter;

    public AddPostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //点击取消和发表的动作事件

        activity = (MainMainActivity)getActivity();
        Log.d("sys","fragmentActivity:"+activity);
        View view = inflater.inflate(R.layout.fragment_add_post,container,false);
        recyclerView = view.findViewById(R.id.recycleView);
        editText = view.findViewById(R.id.editText);

        setRecycleView();
        return view;
    }
    private void setRecycleView(){
        //if(mList == null){
            GridLayoutManager manager = new GridLayoutManager(activity,4);
            recyclerView.setLayoutManager(manager);
            adapter = new ReleaseMsgAdapter(activity,mList);
            recyclerView.setAdapter(adapter);
        //}
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Toast.makeText(activity, "activityResult", Toast.LENGTH_SHORT).show();
        Log.d("sys","fragment resultCode"+resultCode);
        if(resultCode == -1){
           //Toast.makeText(activity, "接收到图片啦", Toast.LENGTH_SHORT).show();
            listImagePath = data.getStringArrayListExtra(EXTRA_RESULT);
            Log.d("sys","listImagePath:"+listImagePath);
            compress(listImagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void compress(ArrayList<String> list){
        for(String imageUrl : list){
            Log.d("sys",imageUrl);
            File file = new File(imageUrl);
            compressImage(file);
        }
        adapter.addMoreItem(list);
    }
    private void compressImage(File file){
        Luban.with(requireContext())
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        //压缩开始前调用
                    }
                    @Override
                    public void onSuccess(File file) {
                        URI uri = file.toURI();
                        String[] split = uri.toString().split(":");
                        list.add(split[1]);
                    }
                    @Override
                    public void onError(Throwable e) {
                        //压缩过程出现问题时调用
                    }
                }).launch();
    }
}
