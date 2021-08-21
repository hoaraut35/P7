package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

public class RecyclerViewAdapterDetail extends RecyclerView.Adapter<RecyclerViewAdapterDetail.ViewHolder> {

    //variables ...

    private List<User> mResults;
    private LatLng myPosition;


    //interface for callback
    public interface Listener {
        void onClickDetailButton(int position);
    }

    //declare callbacl
    // private final Listener callback;

    //the constructor
    //public RecyclerViewAdapter(int mode, List<Result> myList, Listener callback) {
    public RecyclerViewAdapterDetail(List<User> myList, LatLng myLatLng) {
        this.mResults = myList;

        this.myPosition = myLatLng;

        //for callabck
        //this.callback = callback;
    }

    public RecyclerViewAdapterDetail(List<User> myList) {
        this.mResults = myList;
    }

    //for holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_workmates, parent, false);

        return new ViewHolder(view);
    }

    //binding data to holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDetail.ViewHolder holder, int position) {

        User result = mResults.get(position);

        //show the name of restaurant
        holder.nameOfWorkMate.setText(mResults.get(position).getUsername());

        //show avatar
        Glide.with(holder.itemView)
                .load(result.getUrlPicture())
                .circleCrop()
                .into(holder.avatar);


    }


    //to map a range to another range ... from arduino library
    public double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView nameOfWorkMate;
        private final TextView workMateState;

        public ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.workMateAvatar);
            nameOfWorkMate = view.findViewById(R.id.workMateName);
            workMateState = view.findViewById(R.id.workmateState);
        }

    }
}