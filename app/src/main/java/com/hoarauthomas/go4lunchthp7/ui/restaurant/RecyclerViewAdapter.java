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
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //variables ...
    private final List<RestaurantPojo> myRestaurantResult;
 //   private final List<ResultPlaceDetail> myRestaurantAutocompleteResult;

    public ClickListener onClickListener;

    // private LatLng myPosition;

   /* //interface for callback
    public interface RestaurantListener {
        void onClickDetailRestaurant(String restaurantId);
        void popupSnack(String message);
    }

    */

    //declare callback
  //  private final RestaurantListener callback;

    //public RecyclerViewAdapter(List<RestaurantPojo> myList,  RestaurantListener callback) {
    public RecyclerViewAdapter(List<RestaurantPojo> myList, ClickListener myClickListener) {
        this.onClickListener = myClickListener;
        this.myRestaurantResult = myList;
      //  this.myRestaurantAutocompleteResult = myAutocompleteResult;
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

        RestaurantPojo result = myRestaurantResult.get(position);

        //show the name of restaurant
        holder.nameOfRestaurant.setText(myRestaurantResult.get(position).getName());

        //show the image of restaurant
        try {
            String base = "https://maps.googleapis.com/maps/api/place/photo?";
            String key = "key=" + BuildConfig.MAPS_API_KEY;
            String reference = "&photoreference=" + myRestaurantResult.get(position).getPhotos().get(0).getPhotoReference();
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
            holder.distanceOfRestaurant.setText(myRestaurantResult.get(position).getMyDistance() + "m");

        } catch (Exception e) {
            holder.distanceOfRestaurant.setText("erreur");
        }

        //TODO:workmates number don't work
        try{
            holder.numberOfWorkmates.setText(myRestaurantResult.get(position).getMyNumberOfWorkmates());
        }catch (Exception e)
        {
            holder.numberOfWorkmates.setText("?");
        }

        //show the address of restaurant
        holder.addressOfRestaurant.setText(myRestaurantResult.get(position).getVicinity());

        try {
            switch (result.getOpeningHours().getOpenNow().toString()) {
                case "true":
                    holder.openingHours.setText("Ouvert");
                    break;
                case " false":
                    holder.openingHours.setText("Ferm??");
                    break;
                default:
                    holder.openingHours.setText("Inconnu");
                    break;

            }
        } catch (Exception e) {
            holder.openingHours.setText("Inconnu");
        }


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
        //holder.itemView.setOnClickListener(v -> callback.onClickDetailRestaurant(result.getPlaceId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickDetailRestaurant(result.getPlaceId());
            }
        });

    }


    //to map a range to another range ... from arduino library
    public double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public int getItemCount() {
        return myRestaurantResult.size();
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
            openingHours = view.findViewById(R.id.openingHours);
            rating = view.findViewById(R.id.rating);
        }
    }
}
