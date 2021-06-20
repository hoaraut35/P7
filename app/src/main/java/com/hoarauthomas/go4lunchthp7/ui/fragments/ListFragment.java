package com.hoarauthomas.go4lunchthp7.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;

import java.util.List;
import java.util.ResourceBundle;

public class ListFragment extends Fragment {

    //for add a recyclerview on this fragment
    private RecyclerView recyclerView;
    private RecyclerViewAdapter myAdapter;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);


        MainActivity activity = (MainActivity) getActivity();


        List<Result> listeRestaurants = activity.myData();



        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),layoutManager);

        recyclerView = view.findViewWithTag("recycler_view");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new RecyclerViewAdapter(0, listeRestaurants));

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // mViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        // TODO: Use the ViewModel
    }

}