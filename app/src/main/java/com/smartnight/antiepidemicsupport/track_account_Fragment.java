package com.smartnight.antiepidemicsupport;

import android.content.DialogInterface;
import android.icu.text.CaseMap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link track_account_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class track_account_Fragment extends Fragment {

    private TitleBar titleBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView lview;
    private Inflater inflater;
    private int[]image;
    private String[] track_name,IDnumber,pos;

    public track_account_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment track_account_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static track_account_Fragment newInstance(String param1, String param2) {
        track_account_Fragment fragment = new track_account_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_track_account_, container, false);
        lview = view.findViewById(R.id.account_list);
        titleBar = view.findViewById(R.id.titleBar);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(final View v) {
                Navigation.findNavController(v).navigateUp();

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        track_name=new String[]{"willow","Yicong"};
        IDnumber= new String[]{"123","456"};
        pos= new String[]{"Yiyang","changsha"};
        image = new int[]{R.drawable.ic_me,R.drawable.ic_me};

        //组合成list
        List list = new ArrayList();
        for(int i=0;i<track_name.length;i++){
            HashMap<String,Object> item = new HashMap<String,Object>();//哈希map映射
            item.put("image",image[i]);
            item.put("name",track_name[i]);
            item.put("ID",IDnumber[i]);
            item.put("pos",pos[i]);
            list.add(item);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getContext(),list,R.layout.track_account,
                new String[]{"image","goods","name","ID","pos"},
                new int[]{R.id.account_profile,R.id.account_name,R.id.account_ID,R.id.account_pos});
        lview.setAdapter(simpleAdapter);

        //设置点击事件,进入个人主页
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_track_account_Fragment_to_personal_Fragment);
            }
        });
        return view;
    }
}