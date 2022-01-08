package com.safia.go4lunch.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.safia.go4lunch.R;
import com.safia.go4lunch.ui.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.UserViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {
    private UserViewModel userViewModel = UserViewModel.getInstance();
    private MapsFragment mMapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configureBottomView();
        this.configureMapsFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sign_out) {
            userViewModel.signOut(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private   static FusedLocationProviderClient fusedLocationProviderClient;
    private static Boolean mLocationPermissionsGranted = false;
    private static final String TAG = "HomeActivity";
    private static GoogleMap mMap;

    public void getDeviceLocation() { //TODO appeler cette methode dans l'activité HomeActivity
        try {
            if (mLocationPermissionsGranted) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                       // moveCamera(latLng);
                        //MapsFragment.getRestaurant(latLng);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    //Configure BottomNavigationView Listener
    public void configureBottomView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.map, R.id.list_view, R.id.workmates).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void configureMapsFragment(){
        mMapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.map);
         mMapsFragment = new MapsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map, mMapsFragment)
                    .commit();

    }

}