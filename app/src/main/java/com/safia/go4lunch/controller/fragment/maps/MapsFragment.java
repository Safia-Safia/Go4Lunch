package com.safia.go4lunch.controller.fragment.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

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

import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestaurantViewModel mViewModel;
    List<Restaurant> restaurantsList = new ArrayList<>();
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String KEY_RESTAURANT = "KEY_RESTAURANT";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    // creating a variable
    // for search view.
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.configureViewModel();
        this.getActivity().setTitle("I'm Hungry!");
        // initializing our search view.
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        //searchView = view.findViewById(R.id.idSearchView);
        setSearchView();
        return view;
    }

    private void setSearchView() {

    }

    Map<String, Restaurant> mMarkerMap = new HashMap<>();

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
            // mMap.getUiSettings().setMyLocationButtonEnabled(false);
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

    public void getRestaurant(LatLng location) {
        mViewModel.getRestaurants(location).observe(this, nearbyRestaurantList -> {
            if (nearbyRestaurantList != null) {
                restaurantsList = nearbyRestaurantList;
                Log.e("restaurantList", nearbyRestaurantList.size() + "");
                for (Restaurant restaurant : restaurantsList) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                            .title(restaurant.getName())
                            .icon((BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))));
                    mMarkerMap.put(marker.getId(), restaurant);
                }
            }
        });
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

}