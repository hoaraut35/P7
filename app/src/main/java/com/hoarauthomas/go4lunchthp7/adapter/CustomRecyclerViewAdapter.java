package com.hoarauthomas.go4lunchthp7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.hoarauthomas.go4lunchthp7.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {


    private Random random;

    public CustomRecyclerViewAdapter(int seed){
        this.random = new Random(seed);
    }

    private List<String> mTest;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.textView.setText("toto");
    }



    @Override
    public int getItemCount() {
        return 100;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final TextView textView2;

        public ViewHolder(View view)
        {
            super(view);

            textView = (TextView) view.findViewById(R.id.item_list_name);
            textView2 = (TextView) view.findViewById(R.id.item_list_name2);

        }

    }
}
