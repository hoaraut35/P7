package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.content.Context;
import android.graphics.Typeface;
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
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;

import java.util.List;

public class RecyclerViewAdapterDetail extends RecyclerView.Adapter<RecyclerViewAdapterDetail.ViewHolder> {

    private List<FirestoreUser> myWorkMatesResult;
    private LatLng myPosition;

    public RecyclerViewAdapterDetail(List<FirestoreUser> myList, LatLng myLatLng) {
        this.myWorkMatesResult = myList;
        this.myPosition = myLatLng;
    }

    public RecyclerViewAdapterDetail() {
    }

    public RecyclerViewAdapterDetail(List<FirestoreUser> myList) {
        this.myWorkMatesResult = myList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_workmates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDetail.ViewHolder holder, int position) {
        FirestoreUser userResult = myWorkMatesResult.get(position);

        holder.nameOfWorkMate.setText(myWorkMatesResult.get(position).getUsername() + " " );

        String avatar;

        if (userResult.getUrlPicture() == null) {

            String nom = userResult.getUsername();
            String[] parts = nom.split(" ", 2);

            String z = "";

            for (String part : parts) {
                z = part.charAt(0) + z;
            }

            avatar = "https://eu.ui-avatars.com/api/?name=" + z;
        } else {
            avatar = userResult.getUrlPicture();
        }

        Glide.with(holder.itemView)
                .load(avatar)
                .circleCrop()
                .into(holder.avatar);


        if (userResult.getFavoriteRestaurant() != null && !userResult.getFavoriteRestaurant().equals(""))  {
            holder.nameOfWorkMate.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setText(R.string.is_joining_txt);

        } else {
            holder.nameOfWorkMate.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setText(R.string.restaurant_notchoice);
        }
    }

    //to map a range to another range ... from arduino library
    public double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public int getItemCount() {
        return myWorkMatesResult.size();
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