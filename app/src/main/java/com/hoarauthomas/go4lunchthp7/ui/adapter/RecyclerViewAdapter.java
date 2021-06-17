package com.hoarauthomas.go4lunchthp7.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int mode;

    public RecyclerViewAdapter(int seed){
        this.mode = seed;
    }




    private List<Result> mTest;





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
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.textView.setText("Le Zinc");
    }


    void updateRestaurant(final List<Result> restaurants){
        this.mTest = restaurants;
        notifyDataSetChanged();
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

            textView = (TextView) view.findViewById(R.id.list_name);
            textView2 = (TextView) view.findViewById(R.id.list_adress);

        }

    }
}
