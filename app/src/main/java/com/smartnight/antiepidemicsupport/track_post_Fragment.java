package com.smartnight.antiepidemicsupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link track_post_Fragment} factory method to
 * create an instance of this fragment.
 */
public class track_post_Fragment extends Fragment {
    private LinkedList<Post> list_post;
    private MainMainActivity mainMainActivity;
    private View view;
    private ListView post_listview;
    private Fragment fragment;
    //  private User user;//用户类

    public track_post_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_track_post_, container, false);
        //获得父Activity
        mainMainActivity = (MainMainActivity) getActivity();
        post_listview = view.findViewById(R.id.track_post_listView);
        fragment = this;
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
     * 获取帖子信息并展示，待修改，应该动态获取帖子信息********************!!
     */

    public void initData() {
        list_post = new LinkedList<>();
        Bitmap[] bitmaps1 = new Bitmap[4];
        for (int k = 0; k < 4; k++)
            bitmaps1[k] = BitmapFactory.decodeResource(mainMainActivity.getResources(), R.drawable.pic);
        Post p1 = new Post("I need 100 masks!!",bitmaps1,10000);
        p1.setPics(bitmaps1);
        p1.setValue(10000);
        p1.setConcern_num(999);
        p1.setVote_num(283);
        p1.setVoted(true);
        p1.setConcerned(true);
        list_post.add(p1);

        Bitmap[] bitmaps2 = new Bitmap[7];
        for (int k = 0; k < 7; k++)
            bitmaps2[k] = BitmapFactory.decodeResource(mainMainActivity.getResources(), R.drawable.pic2);
        Post p2 = new Post("I need 80 fans!!",bitmaps2,10000);
        p2.setPics(bitmaps2);
        p2.setValue(23989);
        p2.setConcern_num(2134);
        p2.setVote_num(234);
        p2.setVoted(true);
        p2.setConcerned(false);
        list_post.add(p2);

        Bitmap[] bitmaps3 = new Bitmap[2];
        for (int k = 0; k < 2; k++)
            bitmaps3[k] = BitmapFactory.decodeResource(mainMainActivity.getResources(), R.drawable.pic3);
        Post p3 = new Post("I need 10 suits!!",bitmaps3,10000);
        p3.setPics(bitmaps3);
        p3.setValue(234);
        p3.setConcern_num(42);
        p3.setVote_num(123);
        p3.setVoted(false);
        p3.setConcerned(false);
        list_post.add(p3);

        track_post_Fragment.postAdapter pAdapter = new track_post_Fragment.postAdapter(list_post,view.getContext());
        post_listview.setAdapter(pAdapter);
    }

    /**
     * 更新对应view的内容,局部刷新
     * @param position 索引
     */
    private void updateData(int position) {
        Post p = list_post.get(position);
        int firstVisiblePosition = post_listview.getFirstVisiblePosition();
        int lastVisiblePosition = post_listview.getLastVisiblePosition();
        //在看见范围内才更新进度条
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            //获取指定位置view对象
            View view = post_listview.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof PostFragment.postAdapter.ViewHolder) {
                //获取指定view对应的ViewHolder
                track_post_Fragment.postAdapter.ViewHolder viewHolder= (track_post_Fragment.postAdapter.ViewHolder) view.getTag();
                if (p.getConcerned())
                    viewHolder.concerned.setImageResource(R.drawable.watched);
                else
                    viewHolder.concerned.setImageResource(R.drawable.watch);
                if (p.getVoted())
                    viewHolder.voted.setImageResource(R.drawable.voted);
                else
                    viewHolder.voted.setImageResource(R.drawable.vote);
                viewHolder.concerned_num.setText(Integer.toString(p.getConcern_num()));
                viewHolder.vote_num.setText(Integer.toString(p.getVote_num()));
                viewHolder.vote_value.setText(Integer.toString(p.getValue()));
            }
        }
    }


    /*
     *重写适配器类
     */
    class postAdapter extends BaseAdapter {
        private Context mContext;
        private LinkedList<Post> mData;
        public postAdapter(LinkedList<Post> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return list_post.size();
        }

        @Override
        public Object getItem(int i) { return list_post.get(i); }

        @Override
        public long getItemId(int i) { return i; }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, final ViewGroup viewGroup) {
            final track_post_Fragment.postAdapter.ViewHolder holder;
            final Post p = list_post.get(i);
            final int position = i;
            if (view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);
                holder = new track_post_Fragment.postAdapter.ViewHolder();
                //找到组件
                holder.gridLayout = view.findViewById(R.id.gridlayout_post);
                holder.profile = view.findViewById(R.id.post_profile);
                holder.name = view.findViewById(R.id.post_name);
                holder.content = view.findViewById(R.id.post_content);
                holder.concerned_num = view.findViewById(R.id.concern_num);
                holder.vote_value = view.findViewById(R.id.vote_value);
                holder.vote_num = view.findViewById(R.id.vote_num);
                holder.concerned = view.findViewById(R.id.concern);
                holder.voted = view.findViewById(R.id.vote);
                holder.value = view.findViewById(R.id.value);
                holder.pics = p.getPics();
                view.setTag(holder);
            }
            else{
                holder = (track_post_Fragment.postAdapter.ViewHolder)view.getTag();
            }

            /*
             *设置属性
             */
            holder.profile.setImageResource(R.drawable.ic_me);
            holder.content.setText(p.getContent());
            holder.vote_num.setText(Integer.toString(p.getVote_num()));
            holder.concerned_num.setText(Integer.toString(p.getConcern_num()));
            holder.vote_value.setText(Integer.toString(p.getValue()));
            if (p.getConcerned())
                holder.concerned.setImageResource(R.drawable.watched);
            else
                holder.concerned.setImageResource(R.drawable.watch);
            if (p.getVoted())
                holder.voted.setImageResource(R.drawable.voted);
            else
                holder.voted.setImageResource(R.drawable.vote);
            holder.value.setImageResource(R.drawable.value);
            updatePics(holder.gridLayout,holder.pics);

            /*
             * 点击关注,增加帖子关注数目
             * */
            holder.concerned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = p.getConcerned();
                    if(!flag){//没有关注
                        p.setConcern_num(p.getConcern_num()+1);
                    }else{
                        p.setConcern_num(p.getConcern_num()-1);
                    }
                    p.setConcerned(!flag);
                    updateData(position);
                    AnimationTools.scale(holder.concerned);
                }
            });

            /*
             * 点击投票
             * */
            holder.voted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = p.getVoted();
                    if(!flag){//没有投票
                        p.setValue(p.getValue()+20);//暂时设置20
                        /*获得投票用户的权值
                        int Value = ....
                        p.setValue(p.getValue()+Value);
                        */
                    }
                    else{
                        p.setVote_num(p.getVote_num()-1);
                        p.setValue(p.getValue()-20);//暂时设置20
                        // holder.voted.setImageResource(R.drawable.vote);
                    }
                    p.setVoted(!flag);
                    updateData(position);
                    AnimationTools.scale(holder.voted);
                }
            });

            /*
             * 点击头像查看个人资料,将ID号码得到，查询相应用户的个人资料
             * */
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(view);
                    int ID = p.getID();
                    Bundle bundle = new Bundle();//声明一个Bundle对象
                    bundle.putInt("person",ID);   //用Bundle对象携带需要传递的信息


                    navController.navigate(R.id.action_postFragment_to_personal_Fragment);
                }
            });

            return view;
        }


        class ViewHolder{
            private ImageView profile,value,concerned,voted;
            private TextView name,concerned_num,vote_value,vote_num,content;
            private Bitmap[]pics;
            private GridLayout gridLayout;
        }
    }

    /*
     * 动态显示帖子图片
     * @param gridLayout 对应的帖子的表格布局对象
     * @param bitmaps 对应帖子需要显示的图片
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updatePics(GridLayout gridLayout,Bitmap[]bitmaps){
        Bitmap bm = null,bmp = null;
        gridLayout.removeAllViews();//清空子视图 防止原有的子视图影响
        gridLayout.setColumnCount(3);//列数
        gridLayout.setRowCount(bitmaps.length/3+((bitmaps.length%3!=0)?1:0));//行数
        //遍历集合 动态添加
        for (int k = 0, size = bitmaps.length; k < size; k++) {
            GridLayout.Spec rowSpec = GridLayout.spec(k / 3);//行数
            GridLayout.Spec columnSpec = GridLayout.spec(k % 3, 1.0f);//列数 列宽的比例 weight=1
            ImageView imageView = new SquareImageView(view.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //由于宽（即列）已经定义权重比例 宽设置为0 保证均分
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.rowSpec=rowSpec;
            layoutParams.columnSpec=columnSpec;
            layoutParams.setMargins(10, 10, 10, 10);
            bm = bitmaps[k];
            if (bm.getWidth() == 100 && bm.getHeight() == 100) {
                bmp = bm;
            } else {
                bmp = Bitmap.createScaledBitmap(bm, 100, 100, true);
            }
            imageView.setImageBitmap(bmp);
            gridLayout.addView(imageView, layoutParams);
        }
    }

}