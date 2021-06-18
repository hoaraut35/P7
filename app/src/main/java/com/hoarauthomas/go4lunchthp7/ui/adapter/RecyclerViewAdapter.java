package com.hoarauthomas.go4lunchthp7.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int mode;

    private List<Result> privateList;

    public RecyclerViewAdapter(int mode, List<Result> myList) {

        this.privateList = myList;
        this.mode = mode;
    }


    private List<Result> mTest;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
        
        /*switch (this.mode)
        {
            case 0:
                view = inflater.inflate(R.layout.item_restaurant, parent, false);
                break;
            case 1 :
                view = inflater.inflate(R.layout.item_workmates, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.mode);
        }

         */



        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }



    //binding data to holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.nameOfRestaurant.setText(privateList.get(position).getName());
        holder.addressOfRestaurant.setText(privateList.get(position).getOpeningHours().toString());
       // holder.distanceOfRestaurant.setText(privateList.get(position).getScope());
//        Log.i("[THOMAS]","Photo reference : " + privateList.get(position).getPhotos().get(position).getPhotoReference());


         Glide.with(holder.imageOfRestaurant)
                .load("https://maps.googleapis.com/maps/api/place/photo?key=AIzaSyCKxZQFg6PZiBL9kHASYdFWt6A0Ai2mAZw&maxheight=40&maxwidth=40&photoreference=ATtYBwKfP7fjUwWCPvczXeECjw_KOYuyed0RsyQ6mbAdz87EwTyi0QtI0GkjAfMCxtOFASLXbuWsNnJxcMNIaCYbEyVjvEjbHUD5nfEPepz7szTX_nYWH7GYNIPSdBoved1Y-eAcQuC8C4rZkw7OQnXgCZfqBQ9H3YC90ivzgbf1Vw9FNzXK")
                .circleCrop()
                .into(holder.imageOfRestaurant);







    }


    void updateRestaurant(final List<Result> restaurants) {
        this.mTest = restaurants;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 100;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameOfRestaurant;
        private final TextView addressOfRestaurant;
        private final TextView distanceOfRestaurant;
        private final ImageView imageOfRestaurant;

        public ViewHolder(View view) {
            super(view);

            nameOfRestaurant = (TextView) view.findViewById(R.id.nameOfRestaurant);
            distanceOfRestaurant =  (TextView) view.findViewById(R.id.distanceOfRestaurant);
            imageOfRestaurant = (ImageView)view.findViewById(R.id.imageOfRestaurant);
            //typr
            addressOfRestaurant =  (TextView) view.findViewById(R.id.addressOfRestaurant);
           //nb
            //horaire
            //nb avis
        }

    }
}
