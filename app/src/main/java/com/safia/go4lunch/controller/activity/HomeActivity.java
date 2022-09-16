package com.safia.go4lunch.controller.activity;

import static com.safia.go4lunch.repository.UserRepository.getInstance;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private UserViewModel viewModel;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    UserRepository userRepository = UserRepository.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.configureViewModel();
        this.configureBottomView();
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        headerView = navigationView.getHeaderView(0);
        this.loadCurrentUserMail();
        this.loadUserName();
        this.loadCurrentUserPicture();
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    private void loadCurrentUserMail() {
        TextView userMail = headerView.findViewById(R.id.user_email_nav_header);
        userMail.setText(viewModel.getCurrentUser().getEmail());
    }

    private void loadUserName() {
        TextView userName = headerView.findViewById(R.id.user_name_nav_header);
        userName.setText(viewModel.getCurrentUser().getDisplayName());
    }

    private void loadCurrentUserPicture() {
        ImageView userPicture = headerView.findViewById(R.id.nav_header_user_picture);
        String userPhotoUrl = (viewModel.getCurrentUser().getPhotoUrl() != null) ? viewModel.getCurrentUser().getPhotoUrl().toString() : null;
        Glide.with(this)
                .load(userPhotoUrl)
                .circleCrop()
                .into(userPicture);
    }

    //Configure BottomNavigationView Listener
    private void configureBottomView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        new AppBarConfiguration.Builder(
                R.id.map, R.id.list_view, R.id.workmates).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
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
        this.drawerLayout = findViewById(R.id.container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
        }
    }


    // Navigation Drawer setting button
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.activity_main_drawer_your_lunch) {
            getLunch();
        } else if (id == R.id.activity_main_drawer_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this,getString (R.string.parameters), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.activity_main_drawer_logout) {
            viewModel.signOut(this);
            finish();
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLunch() {
        userRepository.getUsersCollection().document(Objects.requireNonNull(getInstance()
                .getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            Restaurant restaurant = user.getRestaurantPicked();
            if (user.getRestaurantPicked() != null) {
                Intent intent = new Intent(this.getApplicationContext(), DetailActivity.class);
                intent.putExtra(MapsFragment.KEY_RESTAURANT, restaurant);
                startActivity(intent);
            } else {
                Toast.makeText(this,getString( R.string.hasnt_decided), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

}