package com.smartnight.antiepidemicsupport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class GiveFragment extends Fragment {

    public GiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_give, container, false);

        ListView listView = view.findViewById(R.id.list);
        TextView textView = view.findViewById(R.id.textView);

        String father = "湖南省";
        String[] son = {"长沙","株洲","岳阳","浏阳","张家界","湘潭","娄底"};
        String[] need = {"N95口罩","呼吸机","防护服","3M防护口罩","医用手套","医用口罩","医用消毒液"};
        int[] sum = {5000,200,500,6000,3000,2000,1000};
        int[] num = {200,40,100,400,500,150,50};

        textView.setText("剩余"+need.length+"条请求未处理");

        List list = new ArrayList();
        for(int i=0;i<son.length;i++){
            HashMap<String,Object> item = new HashMap<String, Object>();
            String text1 = "收到来自"+father+"的请求：";
            String text2 = son[i]+"需要"+need[i]+"共计"+sum[i]+"件";
            String text3 = "请将本地"+need[i]+"共"+num[i]+"件尽快送达";
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
