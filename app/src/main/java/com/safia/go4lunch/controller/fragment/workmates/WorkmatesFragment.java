package com.safia.go4lunch.controller.fragment.workmates;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;

import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.viewmodel.RestaurantAndUserViewModel;

import java.util.List;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter2.onWorkmatesClickListener {
    private RecyclerView mRecyclerView;
    private RestaurantAndUserViewModel viewModel;
    WorkmatesAdapter2 mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getActivity().setTitle("Available Workmates");
        View view = inflater.inflate(R.layout.workmates, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_userList);
        configureViewModel();
        getAllUsers();
        return view;
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantAndUserViewModel.class);
    }

    private void setUpRecyclerView(List<User> userList) {
        mAdapter = new WorkmatesAdapter2(userList, this.getActivity(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAllUsers(){
        viewModel.getAllUsers().observe(this.getActivity(),this::setUpRecyclerView);

    }

    @Override
    public void onWorkmatesClick(Restaurant restaurant) {
        Intent intent = new Intent(WorkmatesFragment.this.getContext(), DetailActivity.class);

        intent.putExtra(MapsFragment.KEY_RESTAURANT, restaurant);
        if (restaurant != null){
            startActivity(intent);
        }
        else
            Toast.makeText(this.getActivity(), "Aucun restaurant a été selectionner.", Toast.LENGTH_SHORT).show();
    }
}