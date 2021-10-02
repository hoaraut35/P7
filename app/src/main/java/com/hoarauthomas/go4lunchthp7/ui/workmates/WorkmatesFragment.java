package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoarauthomas.go4lunchthp7.databinding.FragmentWorkBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.WorkMatesListener {

    private FragmentWorkBinding binding;
    private RecyclerView recyclerView;
    public final List<WorkmatesPojoForUI> resultToShow = new ArrayList<>();

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setupViewModel();
        return view;
    }

    private void setupViewModel() {
        ViewModelWorkMates myViewModelWorkMates = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelWorkMates.class);
        myViewModelWorkMates.getMediatorLiveData().observe(getViewLifecycleOwner(), viewStateWorkMates -> showWorkMates(viewStateWorkMates.getMySpecialWorkMAtes()));
    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerViewWorkmates;
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new WorkmatesAdapter(resultToShow, this));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showWorkMates(List<WorkmatesPojoForUI> mySpecial) {
        if (mySpecial != null) {
            resultToShow.clear();
            resultToShow.addAll(mySpecial);
            setupRecyclerView();
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    //click listener
    @Override
    public void onClickWorkMatesRestaurant(String restaurantId) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("TAG_ID", restaurantId);
        startActivity(intent);
    }

    //click listener
    @Override
    public void popupSnack(String message) {
        showToast(message);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}