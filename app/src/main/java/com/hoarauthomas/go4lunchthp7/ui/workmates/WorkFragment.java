package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoarauthomas.go4lunchthp7.databinding.FragmentWorkBinding;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkFragment extends Fragment implements WorkMatesAdapter.ClickListener {

    //Binding
    private FragmentWorkBinding binding;

    //ViewModel
    private ViewModelWorkMates myViewModelWorkMates;
    private View myView;

    private RecyclerView recyclerView;

    public final List<User> allResult = new ArrayList<>();


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

        binding = FragmentWorkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupViewModel();
        this.myView = view;
       // binding.recyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(view.getContext()));
       // binding.recyclerViewWorkmates.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
       // binding.recyclerViewWorkmates.setHasFixedSize(false);
        return view;
    }

    private void setupViewModel() {

        this.myViewModelWorkMates = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelWorkMates.class);
        this.myViewModelWorkMates.getMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<ViewStateWorkMates>() {
            @Override
            public void onChanged(ViewStateWorkMates viewStateWorkMates) {

                showWorkMates(viewStateWorkMates.myWorkMatesList);

            }
        });
    }

    private void setupRecyclerView(View view) {

        recyclerView = binding.recyclerViewWorkmates;
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new WorkMatesAdapter(0,allResult,this));
    }

    private void showWorkMates(List<User> myWorkMatesList) {

        if (myWorkMatesList == null)
        {
            Log.i("[WORKM]","Liste workmates null ");
            return;
        }else
        {
            Log.i("[WORKM]","media size" + myWorkMatesList.size() );


            allResult.clear();
            allResult.addAll(myWorkMatesList);


            setupRecyclerView(myView);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();



        }
    }

    //this is callback for click on recyclerview...
    @Override
    public void onClickDetailWorkMate(String restaurantId) {
        Log.i("[WORK]", "Item cliqué : " + restaurantId);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("WORKMATE_ID",restaurantId);
        startActivity(intent);
    }

    //this is callback for show an alert message...
    @Override
    public void popupSnack(String message) {
         showToast(message);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(),message,Toast.LENGTH_SHORT);
        toast.show();
    }

}