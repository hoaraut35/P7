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
import com.hoarauthomas.go4lunchthp7.R;

import java.util.List;

public class WorkMatesAdapter extends RecyclerView.Adapter<WorkMatesAdapter.ViewHolder> {

    private final List<ViewModelWorkMates.SpecialWorkMate> myWorkMatesToShow;

    public interface WorkMatesListener {
        void onClickWorkMatesRestaurant(String restaurantId);
        void popupSnack(String message);
    }

    private final WorkMatesListener callback;

    //the constructor
    //public RecyclerViewAdapter(int mode, List<Result> myList, Listener callback) {
    // public WorkMatesAdapter(int mode, List<User> myList, LatLng myLatLng, WorkMatesListener callback) {
    //   this.myWorkMatesToShow = myList;
    // this.callback = callback;
    // }

    /*
    //good but disable to check my special listr
    public WorkMatesAdapter(int mode, List<User> myList, WorkMatesListener callback) {
        Log.i("","");
        this.myUserListResults = myList;
        this.callback = callback;
    }

     */

    public WorkMatesAdapter( List<ViewModelWorkMates.SpecialWorkMate> myList, WorkMatesListener callback) {
        this.myWorkMatesToShow = myList;
        this.callback = callback;
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
    public void onBindViewHolder(@NonNull WorkMatesAdapter.ViewHolder holder, int position) {

        ViewModelWorkMates.SpecialWorkMate myWorkMates = myWorkMatesToShow.get(position);


    String avatar = "";

        if (myWorkMates.getAvatar().isEmpty()){

            String nom = myWorkMates.getNameOfWorkMates();

            String [] parts =  nom.split(" ",2);

            String z = "";

            for (int i=0; i< parts.length;i++){
                z =   parts[i]+ z;

            }
            Log.i("[LOGIN]",""+ z);

            avatar = "https://eu.ui-avatars.com/api/?name="+ z;
        }else
        {
            avatar = myWorkMates.getAvatar();


        }

        Glide.with(holder.itemView)
                .load(avatar)
                .circleCrop()
                .into(holder.avatar);




        holder.nameOfWorkMate.setText(myWorkMates.getNameOfWorkMates());

        Log.i("[NEW]","" + myWorkMates.getNameOfRestaurant());
        if (myWorkMates.getNameOfRestaurant() != null && myWorkMates.getNameOfRestaurant() != "") {
            holder.nameOfWorkMate.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setTypeface(null, Typeface.BOLD);
            holder.workMateState.setText(" is eating (" + myWorkMates.getNameOfRestaurant() + ")");

        } else {
            holder.nameOfWorkMate.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setTypeface(null, Typeface.ITALIC);
            holder.workMateState.setText(" hasn't decided yet");
        }




        //Log.i("[WORK]", "adapter workmates : " + myWorkMates.getUrlPicture());

        if (myWorkMates.getNameOfRestaurant() == null || myWorkMates.getNameOfRestaurant().isEmpty()) {
            Log.i("[WORK]", "restaurant non défini null");
            holder.itemView.setOnClickListener((View myUserView) -> {
                callback.popupSnack("pas de restaurant sélectionné");
            });

        } else {
            Log.i("[WORK]", "Ouverture du détail ");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View myUserView) {
                    callback.onClickWorkMatesRestaurant(myWorkMates.getPlaceId());

                }
            });
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

        public ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.workMateAvatar);
            nameOfWorkMate = view.findViewById(R.id.workMateName);
            workMateState = view.findViewById(R.id.workmateState);
        }
    }
}
