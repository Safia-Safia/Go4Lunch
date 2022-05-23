package com.safia.go4lunch.controller.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesPickedList;
import com.safia.go4lunch.databinding.ActivityDetailBinding;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.viewmodel.RestaurantAndUserViewModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView restaurantPhoto;
    private TextView restaurantName, restaurantAddress, restaurantType;
    public  ImageButton phoneBtn, likeBtn, websiteBtn;
    public Restaurant mRestaurant;
    public static FloatingActionButton fab;
    private RestaurantAndUserViewModel viewModel;
    public static final double MAX_STAR = 3;
    public static final double MAX_RATING = 5;
    RatingBar ratingBar;
    WorkmatesPickedList adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.safia.go4lunch.databinding.ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpView();
        configureViewModel();
        initFabButton();
        initView();
        initLikeBtn();
        initWebsiteBtn();
        initPhoneBtn();
        displayRating();
        likeStatus();
        pickedStatus();
        getAllUsersForThisRestaurant();
    }


    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantAndUserViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpView() {
        mRestaurant = getIntent().getParcelableExtra(MapsFragment.KEY_RESTAURANT);
        restaurantName = findViewById(R.id.restaurant_name_details);
        restaurantPhoto = findViewById(R.id.toolbarImage);
        restaurantType = findViewById(R.id.restaurant_type_detail);
        restaurantAddress = findViewById(R.id.restaurant_adress_detail);
        phoneBtn = findViewById(R.id.phone_button);
        likeBtn = findViewById(R.id.like_button);
        websiteBtn = findViewById(R.id.website_button);
        fab = findViewById(R.id.fav);
        ratingBar = findViewById(R.id.rating_detail);
        mRecyclerView = findViewById(R.id.recyclerview_userList);
    }

    private void initView() {
        restaurantName.setText(mRestaurant.getName());
        restaurantType.setText(mRestaurant.getTypes().toUpperCase());
        restaurantAddress.setText(mRestaurant.getAddress());
        Glide.with(this).load(mRestaurant.getUrlPhoto()).into(restaurantPhoto);
    }

    private void setUpRecyclerView(List<User> userList) {
        adapter = new WorkmatesPickedList(userList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    private void getAllUsersForThisRestaurant() {
        viewModel.getAllUserForThisRestaurant(mRestaurant).observe(this, this::setUpRecyclerView);
    }

    private void initPhoneBtn() {
        phoneBtn.setOnClickListener(view -> {
            String phone = mRestaurant.getPhoneNumber();
            if (phone != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), R.string.unavailable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWebsiteBtn() {
        websiteBtn.setOnClickListener(view -> {
            Uri uri = Uri.parse(mRestaurant.getWebsite());
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), R.string.unavailable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFabButton() {
        fab.setOnClickListener(view -> viewModel.getCurrentUserPickedStatus(mRestaurant).observe(this, isRestaurantPicked -> {
            if (isRestaurantPicked) {
                viewModel.removeRestaurantPicked().observe(this, aBoolean -> {
                    fab.setImageResource(R.drawable.circle_outline_24);
                    getAllUsersForThisRestaurant();
                });
            } else {
                viewModel.addPickedRestaurant(mRestaurant).observe(this, aBoolean -> {
                    fab.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    getAllUsersForThisRestaurant();
                });
            }
        }));
    }

    public void pickedStatus() {
        viewModel.getCurrentUserPickedStatus(mRestaurant).observe(this, isRestaurantPicked -> {
            if (isRestaurantPicked) {
                fab.setImageResource(R.drawable.ic_baseline_check_circle_24);
            } else {
                fab.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            }
        });
    }

    private void displayRating() {
        if (mRestaurant.getRestaurantId() != null) {
            double rating = mRestaurant.getRating() / MAX_RATING * MAX_STAR;
            ratingBar.setRating((float) rating);
        }
    }

    private void initLikeBtn() {
        likeBtn.setOnClickListener(view -> {
            viewModel.getCurrentUserLikeStatus(mRestaurant).observe(this, isRestaurantPicked -> {
                if (isRestaurantPicked) {
                    viewModel.removeRestaurantLiked(mRestaurant);
                    likeBtn.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else {
                    viewModel.addLikeForThisRestaurant(mRestaurant);
                    likeBtn.setImageResource(R.drawable.ic_star_yellow);
                }
            });
        });
    }


    public void likeStatus() {
        viewModel.getCurrentUserLikeStatus(mRestaurant).observe(this, isRestaurantPicked -> {
            if (isRestaurantPicked) {
                likeBtn.setImageResource(R.drawable.ic_star_yellow);
            } else {
                likeBtn.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        });
    }
}