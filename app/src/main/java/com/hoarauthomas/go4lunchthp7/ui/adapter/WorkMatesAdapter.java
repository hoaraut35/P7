package com.hoarauthomas.go4lunchthp7.ui.adapter;

import android.content.Context;
import android.location.Location;
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
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.ui.fragments.WorkFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkMatesAdapter extends RecyclerView.Adapter<WorkMatesAdapter.ViewHolder> {

    //variables ...
    private List<User> mResults;


    //interface for callback
    public interface Listener {
        void onClickDetailButton(int position);
    }

    //declare callbacl
    // private final Listener callback;

    //the constructor
    //public RecyclerViewAdapter(int mode, List<Result> myList, Listener callback) {
    public WorkMatesAdapter(int mode, List<User> myList, LatLng myLatLng) {
        this.mResults = myList;
        // this.mode = mode;//to switch between restaurant and workmates ?

        //for callabck
        //this.callback = callback;
    }

    public WorkMatesAdapter(int mode, List<User> myList) {
        this.mResults = myList;
        //   this.mode = mode;//to switch between restaurant and workmates ?

        //for callabck
        //this.callback = callback;
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

        User result = mResults.get(position);

        //show avatar
        Glide.with(holder.itemView)
                .load(result.getUrlPicture())
                //.load("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50")
             //   .load("https://www.nretnil.com/avatar/LawrenceEzekielAmos.png")
                //.centerCrop()
                .circleCrop()
                .into(holder.avatar);

        //show the name of restaurant
        holder.nameOfWorkMate.setText(result.getUsername());


        Log.i("[WORK]", "adapter workmates : " + result.getUrlPicture());

    }


    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //TODO: can we add binding here ? add views here...
        private final ImageView avatar;
        private final TextView nameOfWorkMate;


        public ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.workMateAvatar);
            nameOfWorkMate = view.findViewById(R.id.workMateName);

        }
    }
}
