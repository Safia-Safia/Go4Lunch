package com.safia.go4lunch.controller.fragment.listview;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.RestaurantAndUserViewModel;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment implements RestaurantListAdapter.onRestaurantClickListener{
    private List<Restaurant> mRestaurant = new ArrayList<>();
    private RestaurantAndUserViewModel viewModel;
    private RecyclerView mRecyclerView;
    RestaurantListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_restaurant);
        setHasOptionsMenu(true);
        configureViewModel();
        getDeviceLocation();
        return view;

    }

    private void setUpRecyclerView(List<Restaurant> restaurants){
        mRestaurant = restaurants;
        mAdapter = new RestaurantListAdapter(restaurants, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getRestaurant(LatLng location) {
        viewModel.getRestaurants(location).observe(this.getViewLifecycleOwner(), this::setUpRecyclerView);
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantAndUserViewModel.class);
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
        Intent intent = new Intent(ListViewFragment.this.getContext(), DetailActivity.class);
        intent.putExtra(MapsFragment.KEY_RESTAURANT, mRestaurant.get(position));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("fragmentList", newText);
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDeviceLocation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}