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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListViewFragment extends Fragment implements RestaurantListAdapter.onRestaurantClickListener {
    private List<Restaurant> mRestaurant = new ArrayList<>();
    private RestaurantViewModel viewModel;
    private RecyclerView mRecyclerView;
    RestaurantListAdapter mAdapter;
    Button button;

    FloatingActionButton openFab, mSortByRating, mSortByDistance;
    TextView sortByRatingTxt, sortByDistanceTxt;
    private Boolean areFabVisible;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);

        //This FAB button is the Parent
        openFab = view.findViewById(R.id.open_fab);
        // FAB button
        mSortByRating = view.findViewById(R.id.sortByRating_btn);
        mSortByDistance = view.findViewById(R.id.sortByDistance_btn);
        // FAB texts
        sortByRatingTxt = view.findViewById(R.id.sortbyRating_txt);
        sortByDistanceTxt = view.findViewById(R.id.sortByDistance_txt);

        button = view.findViewById(R.id.dede);
        mRecyclerView = view.findViewById(R.id.recyclerview_restaurant);
        setHasOptionsMenu(true);
        configureViewModel();
        getDeviceLocation();
        setUpFab();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareRestaurant(new SortByRating());
                setUpRecyclerView(mRestaurant);
            }
        });
        return view;

    }

    private void setUpFab() {

        // Now set all the FABs and all the action name texts as GONE
        mSortByRating.setVisibility(View.GONE);
        mSortByDistance.setVisibility(View.GONE);
        sortByRatingTxt.setVisibility(View.GONE);
        sortByDistanceTxt.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        areFabVisible = false;

        openFab.setOnClickListener(view -> {
            // visible only when Parent FAB button is clicked
            if (!areFabVisible) {
                mSortByRating.show();
                mSortByDistance.show();
                sortByRatingTxt.setVisibility(View.VISIBLE);
                sortByDistanceTxt.setVisibility(View.VISIBLE);

                areFabVisible = true;
            } else {
                mSortByRating.hide();
                mSortByDistance.hide();
                sortByRatingTxt.setVisibility(View.GONE);
                sortByDistanceTxt.setVisibility(View.GONE);

                areFabVisible = false;
            }
        });

        mSortByRating.setOnClickListener(v -> {
            compareRestaurant(new SortByRating());
            setUpRecyclerView(mRestaurant);
            Toast.makeText(this.getContext(), "Rating", Toast.LENGTH_SHORT).show();
        });

        mSortByDistance.setOnClickListener(v -> {
                    compareRestaurant(new SortByDistance());
                    setUpRecyclerView(mRestaurant);
                    Toast.makeText(this.getContext(), "Distance", Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpRecyclerView(List<Restaurant> restaurants) {
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
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantViewModel.class);
    }

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
            Log.e(MapsFragment.TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
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


    public static class SortByRating implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            if (o2.getRating() > o1.getRating()) {
                return 1;
            } else
                return -1;
        }
    }


    public static class SortByDistance implements Comparator<Restaurant> {

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            if (o2.getDistance() > o1.getDistance()) {
                return 1;
            } else
                return 0;
        }
    }

    public void compareRestaurant(Comparator<Restaurant> restaurantComparator) {
        Collections.sort(mRestaurant, restaurantComparator);
    }
}