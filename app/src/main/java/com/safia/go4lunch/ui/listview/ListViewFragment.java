package com.safia.go4lunch.ui.listview;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapsFragment;
import com.safia.go4lunch.ui.maps.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment implements RestaurantListAdapter.onRestaurantClickListener{
    private List<Restaurant> mRestaurant;
    private MapsViewModel mViewModel;
    private RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_restaurant);

        configureViewModel();
        getDeviceLocation();
        return view;
    }

    private void setUpRecyclerView(List<Restaurant> restaurants){
        RestaurantListAdapter mAdapter = new RestaurantListAdapter(restaurants,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getRestaurant(LatLng location) {
        mViewModel.getRestaurants(location).observe(this.getViewLifecycleOwner(), this::setUpRecyclerView);
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel.class);
    }

    private static final String TAG = "MapActivity";
    FusedLocationProviderClient fusedLocationProviderClient;

    public void getDeviceLocation() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

            final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Location currentLocation = (Location) task.getResult();
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    getRestaurant(latLng);
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    @Override
    public void onRestaurantClick(int position) {
        Intent intent = new Intent(ListViewFragment.this.getActivity(), DetailActivity.class);
        intent.putExtra(MapsFragment.KEY_RESTAURANT, mRestaurant.get(position));
        startActivity(intent);
    }
}