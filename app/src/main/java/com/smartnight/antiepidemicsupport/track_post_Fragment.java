package com.smartnight.antiepidemicsupport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link track_post_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class track_post_Fragment extends Fragment {

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
    private String[] type,goods,name,pos;

    public track_post_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment track_post_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static track_post_Fragment newInstance(String param1, String param2) {
        track_post_Fragment fragment = new track_post_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_track_post_, container, false);
        lview = view.findViewById(R.id.post_list);

        image = new int[]{R.drawable.ic_me,R.drawable.ic_me};
        goods=new String[]{"mask","suits"};
        type= new String[]{"offer","need"};
        name=new String[]{"willow","Yicong"};
        pos= new String[]{"Yiyang","changsha"};

        //组合成list
        List list = new ArrayList();
        for(int i=0;i<name.length;i++){
            HashMap<String,Object> item = new HashMap<String,Object>();//哈希map映射
            item.put("image",image[i]);
            item.put("goods",goods[i]);
            item.put("type",type[i]);
            item.put("name",name[i]);
            item.put("pos",pos[i]);
            list.add(item);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getContext(),list,R.layout.track_post,
                new String[]{"image","goods","type","name","pos"},
                new int[]{R.id.post_profile,R.id.post_goods,R.id.post_type,R.id.post_name,R.id.post_pos});
        lview.setAdapter(simpleAdapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_track_post_Fragment_to_personal_post_Fragment);
            }
        });
        return view;
    }
}