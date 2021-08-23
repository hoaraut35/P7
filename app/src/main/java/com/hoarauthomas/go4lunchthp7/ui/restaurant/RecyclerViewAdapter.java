package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //variables ...

    private List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> mResults;
    private LatLng myPosition;


    //interface for callback
    public interface RestaurantListener {
        void onClickDetailRestaurant(String restaurantId);

        void popupSnack(String message);
    }

    //declare callbacl
    private final RestaurantListener callback;


    //the constructor
    //public RecyclerViewAdapter(int mode, List<Result> myList, Listener callback) {
    public RecyclerViewAdapter(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myList, LatLng myLatLng, RestaurantListener callback) {
        this.mResults = myList;

        this.myPosition = myLatLng;

        //for callabck
        this.callback = callback;
    }

    public RecyclerViewAdapter(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myList, RestaurantListener callback) {
        this.callback = callback;
        this.mResults = myList;
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

        com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo result = mResults.get(position);

        //show the name of restaurant
        holder.nameOfRestaurant.setText(mResults.get(position).getName());

        //show the image of restaurant
        try {
            //  Log.i("[THOMAS]", "Get photo [" + position + "] " + mResults.get(position).getPhotos().get(0).getPhotoReference());
            String base = "https://maps.googleapis.com/maps/api/place/photo?";
            String key = "key=" + BuildConfig.MAPS_API_KEY;
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

        //show the distance...
        try {
            holder.distanceOfRestaurant.setText(mResults.get(position).getMyDistance() + "m");

        } catch (Exception e) {
            holder.distanceOfRestaurant.setText("erreur");
        }

        //number of workmates

        try{
            holder.numberOfWorkmates.setText(mResults.get(position).getMyNumberOfWorkmates());
        }catch (Exception e)
        {
            holder.numberOfWorkmates.setText("?");
        }



        //show the address of restaurant
        holder.addressOfRestaurant.setText(mResults.get(position).getVicinity());

        try {
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
        } catch (Exception e) {
            holder.openingHours.setText("Inconnu");
        }

        //TODO: add the number of workmates

        //show star rating
        try {
            double ratingDbl = map(result.getRating(), 1.0, 5.0, 1.0, 3.0);
            int ratingInt = (int) Math.round(ratingDbl);
            Log.i("[RATING]", "Convert [1.0 ... 5.0] : " + result.getRating() + " to [1.00 ... 3.0] : " + map(result.getRating(), 1.0, 5.0, 1.0, 3.0) + "to int :" + ratingInt);

            if (ratingInt == 1) {
                holder.rating.setRating(1);
            } else if (ratingInt == 2) {
                holder.rating.setRating(2);
            } else if (ratingInt == 3) {
                holder.rating.setRating(3);
            }

        } catch (Exception e) {
            Log.i("[RATING]", "Pas de notation pour ce restaurant !");
            holder.rating.setRating(0);
        }

        //TODO add listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickDetailRestaurant(result.getPlaceId());
            }
        });

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

        //TODO: can we add binding here ? add views here...
        private final TextView nameOfRestaurant;
        private final TextView addressOfRestaurant;
        private final TextView distanceOfRestaurant;
        private final ImageView imageOfRestaurant;
        private final TextView openingHours;
        private final TextView numberOfWorkmates;
        private final RatingBar rating;

        public ViewHolder(View view) {
            super(view);
            nameOfRestaurant = view.findViewById(R.id.nameOfRestaurant);
            distanceOfRestaurant = view.findViewById(R.id.distanceOfRestaurant);
            imageOfRestaurant = view.findViewById(R.id.imageOfRestaurant);
            addressOfRestaurant = view.findViewById(R.id.addressOfRestaurant);
            numberOfWorkmates = view.findViewById(R.id.numberOfWorkmates);
            openingHours = view.findViewById(R.id.openinghours);
            rating = view.findViewById(R.id.rating);
        }
    }
}
