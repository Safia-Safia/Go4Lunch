package com.safia.go4lunch.controller.fragment.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import com.google.gson.Gson;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;

import com.safia.go4lunch.viewmodel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private  GoogleMap mMap;
    private RestaurantViewModel mViewModel;
    List<Restaurant> restaurantsList = new ArrayList<>();
    private static final String TAG = "MapActivity";
    public static final String KEY_RESTAURANT = "KEY_RESTAURANT";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    Map<String, Restaurant> mMarkerMap = new HashMap<>();
    Marker marker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.configureViewModel();
        this.getActivity().setTitle("I'm Hungry!");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(marker -> {
            Restaurant restaurant = mMarkerMap.get(marker.getId());
            Intent intent = new Intent(MapsFragment.this.getActivity(), DetailActivity.class);
            intent.putExtra(KEY_RESTAURANT, restaurant);
            startActivity(intent);
            return false;
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermission();
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantViewModel.class);
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
                Log.e("mapsFragment", newText);
                //Avoir une nouvelle liste de restaurant restaurantAll
                List<Restaurant> result = new ArrayList<>();
                String searchStr = newText.toLowerCase();
                for (Restaurant item : restaurantsList) {
                    if (item.getName().toLowerCase().contains(searchStr)) {
                        result.add(item);
                    }
                }
                setUpMarker(result);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getRestaurant(LatLng location) {
        mViewModel.getRestaurants(location).observe(this, nearbyRestaurantList -> {
            if (nearbyRestaurantList != null) {
                restaurantsList = nearbyRestaurantList;
                setUpMarker(restaurantsList);
            }
        });
    }

    public void setUpMarker(List<Restaurant> restaurants){
        mMap.clear();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getUsers().isEmpty()) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                        .title(restaurant.getName())
                        .icon((BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_red))));
            } else {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                        .title(restaurant.getName())
                        .icon((BitmapDescriptorFactory
                                .fromResource(R.drawable.marker_green))));
            }
            mMarkerMap.put(marker.getId(), restaurant);
        }

    }

    public void getDeviceLocation() { //TODO appeler cette methode dans l'activité HomeActivity
        try {
            if (mLocationPermissionsGranted) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        moveCamera(latLng);
                        getRestaurant(latLng);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapsFragment.DEFAULT_ZOOM));
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
                //initialize our map
                initMap();
            }
        }
    }

    public static Restaurant getEatingAtPlace(Context context) {
        SharedPreferences sp = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(KEY_RESTAURANT, "RESTAURANT");
        return gson.fromJson(json, Restaurant.class);
    }
}