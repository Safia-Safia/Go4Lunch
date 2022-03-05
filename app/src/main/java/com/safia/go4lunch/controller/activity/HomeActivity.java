package com.safia.go4lunch.controller.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.configureBottomView();
        this.configureMapsFragment();
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        headerView = navigationView.getHeaderView(0);
        this.loadCurrentUserMail();
        this.loadUserName();
        this.loadCurrentUserPicture();
    }

    private void loadCurrentUserMail() {
        TextView userMail = headerView.findViewById(R.id.user_email_nav_header);
        userMail.setText(userViewModel.getCurrentUser().getEmail());
    }

    private void loadUserName() {
        TextView userName = headerView.findViewById(R.id.user_name_nav_header);
        userName.setText(userViewModel.getCurrentUser().getDisplayName());
    }

    private void loadCurrentUserPicture() {
        ImageView userPicture = headerView.findViewById(R.id.nav_header_user_picture);
        String userPhotoUrl = (userViewModel.getCurrentUser().getPhotoUrl() != null) ? userViewModel.getCurrentUser().getPhotoUrl().toString() : null;
        Glide.with(this)
                .load(userPhotoUrl)
                .circleCrop()
                .into(userPicture);
    }

    //Configure BottomNavigationView Listener
    private void configureBottomView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.map, R.id.list_view, R.id.workmates).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void configureMapsFragment() {
        MapsFragment mMapsFragment;
        mMapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mMapsFragment)
                .commit();
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Search Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    // Navigation Drawer setting button
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_main_drawer_your_lunch) {
            Toast.makeText(this, (R.string.your_lunch), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.activity_main_drawer_settings) {
            Toast.makeText(this, (R.string.parameters), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.activity_main_drawer_logout) {
            userViewModel.signOut(this);
            finish();
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}