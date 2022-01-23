package com.safia.go4lunch.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.safia.go4lunch.R;
import com.safia.go4lunch.ui.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
        //this.loadCurrentUserMail();
        //this.loadUserName();
        //this.loadCurrentUserPicture();
        this.configureBottomView();
        this.configureMapsFragment();
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        headerView =navigationView.getHeaderView(0);

    }

    private void loadCurrentUserMail(){
        TextView userMail = headerView.findViewById(R.id.user_email_nav_header);
        userMail.setText(userViewModel.getCurrentUser().getEmail());
    }

    private void loadUserName(){
        TextView userName = headerView.findViewById(R.id.user_name_nav_header);
        userName.setText(userViewModel.getCurrentUser().getDisplayName());
    }

    private void loadCurrentUserPicture (){
        ImageView userPicture = headerView.findViewById(R.id.nav_header_user_picture);
        Glide.with(this)
                .load( userViewModel.getCurrentUser().getPhotoUrl())
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
        MapsFragment mMapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        this.navigationView = (NavigationView) findViewById(R.id.activity_home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_main_drawer_your_lunch) {
            Toast.makeText(this, "Lieu de déjeuner", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.activity_main_drawer_settings) {
            Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.activity_main_drawer_logout) {
            userViewModel.signOut(this);
            finish();
        }


        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}