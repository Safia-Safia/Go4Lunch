package com.safia.go4lunch.ui.listview;

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
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {
    private List<Restaurant> mRestaurant = new ArrayList<>();
    private MapsViewModel mViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);

        RestaurantRvAdapter mAdapter = new RestaurantRvAdapter(mRestaurant);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview_restaurant);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        configureViewModel();
        getDeviceLocation();
        return view;
    }

    private void getRestaurant(LatLng location) {
        String locationStr = location.latitude + "," + location.longitude;
        mViewModel.getRestaurants(locationStr).observe(this.getViewLifecycleOwner(), nearbyRestaurantList -> {
            if (nearbyRestaurantList != null) {
                mRestaurant = nearbyRestaurantList;
            }
        });
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

}