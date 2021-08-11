package com.hoarauthomas.go4lunchthp7.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.ui.adapter.WorkMatesAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment {

    private ViewModelGo4Lunch myViewModel;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter myAdapter;

    private View myView;


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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_work, container, false);
        setupViewModel();
        this.myView = view;
        recyclerView = view.findViewById(R.id.recycler_view_workmates);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);
        return view;

    }

    private void setupViewModel() {
        Log.i("WORK", "Setup VM in Workmates frag...");
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        this.myViewModel.getAllWorkMates().observe(getViewLifecycleOwner(), this::onUpdateWorkMates);
    }

    private void onUpdateWorkMates(List<User> users) {
        Log.i("[WORK]", "update workmatres ... in recyclerviex");
        recyclerView.setAdapter(new WorkMatesAdapter(0, users));
    }
}