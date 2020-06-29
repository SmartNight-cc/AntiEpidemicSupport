package com.smartnight.antiepidemicsupport;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.giftedcat.picture.lib.PictureUseHelpr;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {
    private List<String> image;
    private RecyclerView recyclerView;

    public AddPostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = requireActivity();
        EditText editText = activity.findViewById(R.id.editText);
        recyclerView = activity.findViewById(R.id.recycleView);

    }
}
