package com.smartnight.antiepidemicsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    //组件
    private ImageView Myprofile;
    private TextView Myname;
    private TextView Myid;
    private Button edit;
    private Button track_acount,track_post;
    private ListView lview1,lview2;
    //记录
    private String[] rec_time;
    private String[] rec_type;
    private String[] rec_goods;
    private String[] rec_from;
    private String[] rec_dest;
    //物流
    private String[] loc_time;
    private String[] loc_type;
    private String[] loc_goods;
    private String[] loc_pos;
    private String[] loc_dest;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment设置布局文件
       View view = inflater.inflate(R.layout.fragment_me, container, false);

        Myprofile = view.findViewById(R.id.profile);//头像
        Myname = view.findViewById(R.id.name);//昵称
        edit = view.findViewById(R.id.edit_btn);//编辑按钮
        track_acount=view.findViewById(R.id.account);//关注用户
        track_post=view.findViewById(R.id.post);//关注帖子
        Myid = view.findViewById(R.id.MyID);


        SharedPreferences shp = requireActivity().getSharedPreferences("UserFile",Context.MODE_PRIVATE);
        //头像
        String pic = shp.getString("Picture","");
        if(pic!=null){
            byte[] bytes = Base64.decode(pic.getBytes(),1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Myprofile.setImageBitmap(bitmap);
        }

        Myname.setText(shp.getString("Name",""));
        Myid.setText(shp.getString("ID",""));


        //编辑信息点击事件
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_meFragment_to_edit_info_Fragment);
            }
        });

       //关注用户的点击事件
        track_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_meFragment_to_track_account_Fragment);
            }
        });

        //帖子的点击事件
        track_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_meFragment_to_track_post_Fragment);
            }
        });


        //找到两个ListView组件
       lview1=view.findViewById(R.id.it1);
       lview2=view.findViewById(R.id.it2);
       int head1=R.drawable.record;//图片
       int head2=R.drawable.pos;

       //获得信息
        rec_time = new String[]{"2020/6/3/12:00", "2020/3/14/17:30", "2020/3/14/17:30"};
        rec_type = new String[]{"receive", "offer", "offer"};
        rec_goods = new String[]{"mask", "suits","mask"};
        rec_from = new String[]{"Hunan-changsha","Hubei-Wuhan","Hunan-changsha"};
        rec_dest = new String[]{"Hunan-Yiyang","Guangdong-Guangzhou","Hunan-Yiyang"};

        loc_time = new String[]{"2020/6/3/12:00", "2020/3/14/17:30"};
        loc_type = new String[]{"receive", "offer"};
        loc_goods = new String[]{"mask", "suits"};
        loc_pos = new String[]{"Hunan-changsha","Hubei-Wuhan"};
        loc_dest = new String[]{"Hunan-Yiyang","Guangdong-Guangzhou"};

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        for(int i=0;i<rec_time.length;i++){
            HashMap<String,Object> item = new HashMap<String,Object>();//哈希map映射
            item.put("image",R.drawable.record);
            item.put("time",rec_time[i]);
            item.put("type",rec_type[i]);
            item.put("goods",rec_goods[i]);
            item.put("from",rec_from[i]);
            item.put("dest",rec_dest[i]);
            list1.add(item);
        }

        for(int i=0;i<loc_time.length;i++){
            HashMap<String,Object> item = new HashMap<String,Object>();//哈希map映射
            item.put("image",R.drawable.pos);
            item.put("time",loc_time[i]);
            item.put("type",loc_type[i]);
            item.put("goods",loc_goods[i]);
            item.put("from",loc_pos[i]);
            item.put("dest",loc_dest[i]);
            list2.add(item);
        }

        SimpleAdapter simpleAdapter1 =
                new SimpleAdapter(this.getContext(),list1,R.layout.item,
                new String[]{"image","time","type","goods","from","dest"},
                new int[]{R.id.item_image,R.id.time,R.id.type,R.id.goods,R.id.from,R.id.dest});

        SimpleAdapter simpleAdapter2 =
                new SimpleAdapter(this.getContext(),list2,R.layout.item,
                new String[]{"image","time","type","goods","from","dest"},
                new int[]{R.id.item_image,R.id.time,R.id.type,R.id.goods,R.id.from,R.id.dest});

        lview1.setAdapter(simpleAdapter1);
        lview2.setAdapter(simpleAdapter2);

        lview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_meFragment_to_show_track_Fragment);
            }
        });
        return view;
    }

    //修改信息
    public void setData(HashMap<String, Object> elem){
        String name = (String)elem.get("name");
        Uri edit_profile = (Uri)elem.get("profile");
        Myname.setText(name);
        Myprofile.setImageURI(edit_profile);
    }
}
