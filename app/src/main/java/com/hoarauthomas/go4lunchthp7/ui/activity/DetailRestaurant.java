package com.hoarauthomas.go4lunchthp7.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;

public class DetailRestaurant extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtonPhone();
        setupButtonLike();
        setupButtonWeb();
    }

    private void setupButtonWeb() {
    }

    private void setupButtonLike() {
    }

    private void setupButtonPhone() {
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("[DETAIL]","clic sur phone");

                Intent makeCall = new Intent(Intent.ACTION_CALL);
                makeCall.setData(Uri.parse("tel:" + "0781804664"));
                startActivity(makeCall);
            }
        });

    }


}