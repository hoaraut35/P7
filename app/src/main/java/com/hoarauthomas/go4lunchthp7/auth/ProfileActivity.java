package com.hoarauthomas.go4lunchthp7.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    //Added for view binding
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        updateUIWhenCreating();


    }

    private void updateUIWhenCreating() {

        if (getCurrentUser() != null) {
            //TODO: get avatar name email
            //get data from firebase or default data
            String name = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found): this.getCurrentUser().getDisplayName();

            //update ui
            binding.textVUserName.setText(name);
            Log.i("THOMAS","utilisateur deja login");
        }
    }


    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

}