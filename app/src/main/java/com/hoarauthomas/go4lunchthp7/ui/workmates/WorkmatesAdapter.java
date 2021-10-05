package com.hoarauthomas.go4lunchthp7.ui.workmates;

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
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private final Context context;
    private final List<WorkmatesPojoForUI> myWorkMatesToShow;

    public interface WorkMatesListener {
        void onClickWorkMatesRestaurant(String restaurantId);
        void popupSnack(String message);
    }

    private final WorkMatesListener callback;

    public WorkmatesAdapter(List<WorkmatesPojoForUI> myList, WorkMatesListener callback, Context context) {
        this.myWorkMatesToShow = myList;
        this.callback = callback;
        this.context = context;
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
    public void onBindViewHolder(@NonNull WorkmatesAdapter.ViewHolder holder, int position) {

        WorkmatesPojoForUI myWorkMates = myWorkMatesToShow.get(position);

        String avatar;

        if (myWorkMates.getAvatar() == null) {

            //generate an avatar
            String nom = myWorkMates.getNameOfWorkMates();
            String[] parts = nom.split(" ", 2);
            String z = "";

            for (int i = 0; i < parts.length; i++) {
                z = parts[i].charAt(0) + z;
            }




            avatar = "https://eu.ui-avatars.com/api/?name=" + z;
        } else {
            avatar = myWorkMates.getAvatar();
        }

        Glide.with(holder.itemView)
                .load(avatar)
                .circleCrop()
                .into(holder.avatar);

        holder.nameOfWorkMate.setText(myWorkMates.getNameOfWorkMates() + " ");

        if (myWorkMates.getNameOfRestaurant() != null && !myWorkMates.getNameOfRestaurant().equals("")) {
            holder.nameOfWorkMate.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setTypeface(null, Typeface.BOLD);
            holder.workmateRestaurant.setText(" (" + myWorkMates.getNameOfRestaurant() + ")");

        } else {
            holder.nameOfWorkMate.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setText(R.string.restaunochoice);
        }

        if (myWorkMates.getNameOfRestaurant() == null || myWorkMates.getNameOfRestaurant().isEmpty()) {
            holder.itemView.setOnClickListener((View myUserView) -> callback.popupSnack(context.getString(R.string.toast_no_restaurant)));

        } else {

            holder.itemView.setOnClickListener(myUserView -> callback.onClickWorkMatesRestaurant(myWorkMates.getPlaceId()));
        }
    }

    @Override
    public int getItemCount() {
        return myWorkMatesToShow.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView nameOfWorkMate;
        private final TextView workMateState;
        private final TextView workmateRestaurant;

        public ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.workMateAvatar);
            nameOfWorkMate = view.findViewById(R.id.workMateName);
            workMateState = view.findViewById(R.id.workmateState);
            workmateRestaurant = view.findViewById(R.id.workmateRestaurant);
        }
    }
}
