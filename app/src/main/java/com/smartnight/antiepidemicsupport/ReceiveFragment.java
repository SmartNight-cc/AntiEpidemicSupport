package com.smartnight.antiepidemicsupport;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiveFragment extends Fragment {

    public ReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receive, container, false);

        ListView listView = view.findViewById(R.id.listView);
        TextView textView = view.findViewById(R.id.textView2);

        String father = "山东省";
        String[] need = {"N95口罩","呼吸机","防护服","3M防护口罩","医用手套","医用口罩","医用消毒液"};
        int[] num = {200,40,100,400,500,150,50};

        textView.setText("剩余"+need.length+"条援助未处理");

        final List list = new ArrayList();
        for(int i=0;i<need.length;i++){
            HashMap<String,Object> item = new HashMap<String, Object>();
            String text1 = "收到来自"+father+"的援助：";
            String text2 = "援助的"+num[i]+"件"+need[i]+"物资到了";
            String text3 = "请尽快通知居民领取";
            item.put("text1",text1);
            item.put("text2",text2);
            item.put("text3",text3);
            list.add(item);
        }
        SimpleAdapter simpleAdapter =
                new SimpleAdapter(this.getContext(),list,R.layout.stationitem,
                        new String[]{"text1","text2","text3"},
                        new int[]{R.id.textView3,R.id.textView5,R.id.textView6});
        listView.setAdapter(simpleAdapter);

        return view;
    }
}