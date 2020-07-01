package com.smartnight.antiepidemicsupport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReleaseMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainMainActivity activity;
    private ArrayList<String> mList;
    private final LayoutInflater inflater;
    private static final int ITEM_TYPE_ONE = 1;//图片模式
    private static final int ITEM_TYPE_TWO = 2;//加号模式

        public ReleaseMsgAdapter(MainMainActivity activity, ArrayList<String> list){
        this.activity = activity;
            Log.d("sys","adapter's activity is"+activity);
        this.mList =list;
        this.inflater = LayoutInflater.from(activity);
    }

    public void addMoreItem(ArrayList<String> loadMoreData){
        mList.addAll(loadMoreData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parent.setPadding(20,20,20,20);
        switch (viewType){
            case ITEM_TYPE_ONE:
                return new MyHolder(inflater.inflate(R.layout.photoitem,parent,false));
            case ITEM_TYPE_TWO:
                return new MyTwoHolder(inflater.inflate(R.layout.photoitem2,parent,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  MyHolder){
            bindItemMyHolder((MyHolder) holder,position);
        }else if(holder instanceof MyTwoHolder){
            bindItemTwoMyHolder((MyTwoHolder)holder,position);
        }
    }

    private void bindItemMyHolder(MyHolder holder,int position){
        Glide.with(activity)
                .load(mList.get(position))
                .centerCrop()
                .into(holder.imageView);
    }

    private void bindItemTwoMyHolder(MyTwoHolder holder,int position){
        if(mList.size()>=9){
            holder.imageView2.setVisibility(View.GONE);
        }//集合长度大于等于9，隐藏加号图片
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从相册加载
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(intent,1);
                Log.d("sys","adapter's request Code:1");
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            //position=size position从0开始
            return ITEM_TYPE_TWO;
        }else{
            return ITEM_TYPE_ONE;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
        //为空size=0，但是有一张加号图片
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //选择的每一个图片
        private final ImageView imageView;
        public MyHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }
    class MyTwoHolder extends RecyclerView.ViewHolder{
        //加号图片
        private final ImageView imageView2;
        public MyTwoHolder(View view){
            super(view);
            imageView2 = view.findViewById(R.id.imageView2);
        }
    }
}
