package com.hoarauthomas.go4lunchthp7.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.FragmentWorkBinding;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.ui.activity.DetailRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.ui.adapter.WorkMatesAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.List;

public class WorkFragment extends Fragment implements WorkMatesAdapter.ClickListener {


    private FragmentWorkBinding binding;
    private ViewModelGo4Lunch myViewModel;
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WorkFragment newInstance(String param1, String param2) {
        WorkFragment fragment = new WorkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWorkBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        //inflater.inflate(R.layout.fragment_work, container, false);

        setupViewModel();

        binding.recyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recyclerViewWorkmates.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewWorkmates.setHasFixedSize(false);
        return view;
    }

    private void setupViewModel() {
        Log.i("WORK", "Setup VM in Workmates frag...");
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        this.myViewModel.getAllWorkMates().observe(getViewLifecycleOwner(), this::onUpdateWorkMates);
    }

    private void onUpdateWorkMates(List<User> users) {
        Log.i("[WORK]", "update workmatres ... in recyclerviex");
        binding.recyclerViewWorkmates.setAdapter(new WorkMatesAdapter(0, users, this));
    }

    //this is callback for click on recyclerview...
    @Override
    public void onClickDetailWorkMate(int position) {
        Log.i("[WORK]", "Item cliqué : " + position);

        Intent intent = new Intent(getContext(), DetailRestaurant.class);
        startActivity(intent);
    }

    @Override
    public void popupSnack(String message) {

    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.recyclerViewWorkmates, message, Snackbar.LENGTH_SHORT).show();
    }

}