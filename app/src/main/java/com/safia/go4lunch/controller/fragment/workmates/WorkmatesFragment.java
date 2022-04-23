package com.safia.go4lunch.controller.fragment.workmates;

import static com.safia.go4lunch.repository.RestaurantRepository.USER_PICKED;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.safia.go4lunch.R;

import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.controller.fragment.listview.ListViewFragment;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter2.onWorkmatesClickListener {
    private List<User> userList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    private final RestaurantViewModel restaurantViewModel = RestaurantViewModel.getInstance();
    WorkmatesAdapter2 mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.getActivity().setTitle("Available Workmates");
        View view = inflater.inflate(R.layout.workmates, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_userList);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        mAdapter = new WorkmatesAdapter2(userList, this.getActivity(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        userViewModel.getAllUsers(userList,mAdapter);
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