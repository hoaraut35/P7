package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
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

public class WorkMatesAdapter extends RecyclerView.Adapter<WorkMatesAdapter.ViewHolder> {

    //variables ...
    private List<User> myUserListResults;

    //interface for callback
    public interface ClickListener {
        void onClickDetailWorkMate(String restaurantId);
        void popupSnack(String message);
    }

    //declare callbacl
    private final ClickListener callback;


    public void setOnItemClickListener(ClickListener clickListener) {

    }

    //the constructor
    //public RecyclerViewAdapter(int mode, List<Result> myList, Listener callback) {
    public WorkMatesAdapter(int mode, List<User> myList, LatLng myLatLng, ClickListener callback) {
        this.myUserListResults = myList;
        this.callback = callback;
    }

    public WorkMatesAdapter(int mode, List<User> myList, ClickListener callback) {
        this.myUserListResults = myList;
        this.callback = callback;
    }

    //for holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_workmates, parent, false);

        //return null;
        return new ViewHolder(view);
    }

    //binding data to holder
    @Override
    public void onBindViewHolder(@NonNull WorkMatesAdapter.ViewHolder holder, int position) {

        User myUser = myUserListResults.get(position);

        //show avatar
        Glide.with(holder.itemView)
                .load(myUser.getUrlPicture())
                .circleCrop()
                .into(holder.avatar);

        //show the name of restaurant
        holder.nameOfWorkMate.setText(myUser.getUsername());

        //show restaurant selected here


        if (myUser.getFavoriteRestaurant() != null && myUser.getFavoriteRestaurant() != "") {
            holder.nameOfWorkMate.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setText(" is eating (" + myUser.getFavoriteRestaurant() + ")");

        } else {
            holder.nameOfWorkMate.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setText(" hasn't decided yet");
        }

        Log.i("[WORK]", "adapter workmates : " + myUser.getUrlPicture());

        if (myUser.getFavoriteRestaurant() == "" || myUser.getFavoriteRestaurant().isEmpty()) {
            Log.i("[WORK]", "restaurant non défini null");
            holder.itemView.setOnClickListener((View myUserView) -> {
                callback.popupSnack("pas de restaurant sélectionné");

            });

        } else {
            Log.i("[WORK]", "Ouverture du détail ");
            holder.itemView.setOnClickListener((View myUserView) -> {
                callback.onClickDetailWorkMate(myUser.getFavoriteRestaurant());

            });
        }

    }


    @Override
    public int getItemCount() {
        return myUserListResults.size();
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
