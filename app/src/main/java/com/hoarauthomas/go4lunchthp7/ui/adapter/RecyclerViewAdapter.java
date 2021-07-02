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
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.pojo.Result;

//import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //variables ...
    private int mode;
    private List<Result> mResults;

    //the constructor
    public RecyclerViewAdapter(int mode, List<Result> myList) {
        this.mResults = myList;
        this.mode = mode;//to switch between restaurant and workmates ?
    }

    //for holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(view);
    }

    //binding data to holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Result result = mResults.get(position);

        //show the name of restaurant
        holder.nameOfRestaurant.setText(mResults.get(position).getName());

        //show the image of restaurant
        try {
          //  Log.i("[THOMAS]", "Get photo [" + position + "] " + mResults.get(position).getPhotos().get(0).getPhotoReference());
            String base = "https://maps.googleapis.com/maps/api/place/photo?";
            String key = "key="+ BuildConfig.MAPS_API_KEY;
            String reference = "&photoreference=" + mResults.get(position).getPhotos().get(0).getPhotoReference();
            String maxH = "&maxheight=157";
            String maxW = "&maxwidth=157";
            String query = base + key + reference + maxH + maxW;

            Glide.with(holder.imageOfRestaurant)
                    .load(query)
                    .centerCrop()
                    .into(holder.imageOfRestaurant);

        } catch (Exception e) {
            Log.i("[THOMAS]", "Exception : " + e.getMessage());
        }

        //TODO: must to add the distance here...

        //show the address of restaurant
        holder.addressOfRestaurant.setText(mResults.get(position).getVicinity());

       try{
           switch (result.getOpeningHours().getOpenNow().toString()) {
               case "true":
                   holder.openingHours.setText("Ouvert");
                   break;
               case " false":
                   holder.openingHours.setText("Fermé");
                   break;
               default:
                   holder.openingHours.setText("Inconnu");
                   break;

           }
       }catch (Exception e)
       {
           holder.openingHours.setText("Inconnu");
       }

        //TODO: add the number of workmates

        //TODO: add openinghours

        //TODO: add star

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //TODO: can we add binding here ? add views here...
        private final TextView nameOfRestaurant;
        private final TextView addressOfRestaurant;
        private final TextView distanceOfRestaurant;
        private final ImageView imageOfRestaurant;
        private final TextView openingHours;
        private final TextView numberOfWorkmates;

        public ViewHolder(View view) {
            super(view);
            nameOfRestaurant = view.findViewById(R.id.nameOfRestaurant);
            distanceOfRestaurant = view.findViewById(R.id.distanceOfRestaurant);
            imageOfRestaurant = view.findViewById(R.id.imageOfRestaurant);
            addressOfRestaurant = view.findViewById(R.id.addressOfRestaurant);
            numberOfWorkmates = view.findViewById(R.id.numberOfWorkmates);
            openingHours = view.findViewById(R.id.openinghours);
        }
    }
}
