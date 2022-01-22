package com.safia.go4lunch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safia.go4lunch.R;
import com.safia.go4lunch.databinding.ActivityDetailBinding;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapsFragment;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.viewmodel.UserViewModel;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ImageView restaurantPhoto;
    private TextView restaurantName, restaurantAddress, restaurantType;
    private ImageButton phoneBtn, likeBtn, websiteBtn;
    private Restaurant mRestaurant;
    private FloatingActionButton fab;
    private Toolbar mToolbar;
    private UserViewModel userViewModel = UserViewModel.getInstance();

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpView();
        initView();
        initLikeBtn();
        initWebsiteBtn();
        initPhoneBtn();
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
        mToolbar = findViewById(R.id.toolbar);
        ratingBar = findViewById(R.id.rating_detail);
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initPhoneBtn() {
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mRestaurant.getPhoneNumber();
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.unavailable, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initLikeBtn() {
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRestaurant != null && userViewModel.getCurrentUser() != null) {
                    RestaurantRepository.createLike(mRestaurant.getRestaurantId(), userViewModel.getCurrentUser().getUid()).addOnCompleteListener(likeTask -> {
                        likeBtn.setImageResource(R.drawable.ic_star_yellow);
                    });
                }
            }
        });
    }

    private boolean isOnFavorite(){
        return RestaurantRepository.getLikedCollection().document().equals(mRestaurant.getRestaurantId());
    }

    private void addToFavorite(){

        }

    private void removeFromFavorite() {
        RestaurantRepository.deleteLike(mRestaurant.getRestaurantId(), userViewModel.getCurrentUser().getUid());
        likeBtn.setImageResource(R.drawable.ic_baseline_star_border_24);
    }

    private void initWebsiteBtn() {
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(mRestaurant.getWebsite());
                if (uri != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.unavailable, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initFabButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void initView() {
        restaurantName.setText(mRestaurant.getName());
        restaurantType.setText(mRestaurant.getTypes().toUpperCase());
        restaurantAddress.setText(mRestaurant.getAddress());
        ratingBar.setRating(mRestaurant.getRating());
        Glide.with(this).load(mRestaurant.getUrlPhoto()).into(restaurantPhoto);
    }
}