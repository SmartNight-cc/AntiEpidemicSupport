package com.smartnight.antiepidemicsupport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class StationAdapter extends ListAdapter<Content, StationAdapter.MyViewHolder> {

    StationAdapter() {
        super(new DiffUtil.ItemCallback<Content>() {
            @Override
            public boolean areItemsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Content oldItem, @NonNull Content newItem) {
                return ((oldItem.getText1().equals(newItem.getText1()))
                        && (oldItem.getText2().equals(newItem.getText2()))
                        && (oldItem.getText3().equals(newItem.getText3())));
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemview = layoutInflater.inflate(R.layout.stationitem,parent,false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemview);
        myViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Content content = getItem(position);
        holder.text1.setText(content.getText1());
        holder.text2.setText(content.getText2());
        holder.text3.setText(content.getText3());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.textNumber.setText(String.valueOf(holder.getAdapterPosition()+1));
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text1,text2,text3,textNumber;
        Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.textView3);
            text2 = itemView.findViewById(R.id.textView5);
            text3 = itemView.findViewById(R.id.textView6);
            textNumber = itemView.findViewById(R.id.textViewNumber);
            button = itemView.findViewById(R.id.button4);
        }
    }
}
