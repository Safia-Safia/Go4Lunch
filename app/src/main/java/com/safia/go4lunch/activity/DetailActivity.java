package com.safia.go4lunch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.databinding.ActivityDetailBinding;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapsFragment;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ImageView restaurantPhoto;
    private TextView restaurantName, restaurantAddress, restaurantType;
    private ImageButton phoneBtn, likeBtn, websiteBtn;
    private Restaurant mRestaurant;
    private FloatingActionButton fab;
    private Toolbar mToolbar;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpView();
        initView();
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

    private void setUpView(){
        mRestaurant = getIntent().getParcelableExtra(MapsFragment.KEY_RESTAURANT);
        restaurantName = findViewById(R.id.restaurant_name_details);
        restaurantPhoto = findViewById(R.id.toolbarImage);
        restaurantType = findViewById(R.id.restaurant_type_detail);
        restaurantAddress = findViewById(R.id.restaurant_adress_detail);
        phoneBtn = findViewById(R.id.phone_button);
        likeBtn = findViewById(R.id.like_button);
        websiteBtn = findViewById(R.id.website_button);
        fab = findViewById(R.id.fav);
        mToolbar =findViewById(R.id.toolbar);
        ratingBar = findViewById(R.id.rating_detail);
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initPhoneBtn(){
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mRestaurant.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
    }

    private void initLikeBtn(){
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initWebsiteBtn(){
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse( mRestaurant.getWebsite());
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    private void initFabButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initView(){
        restaurantName.setText(mRestaurant.getName());
        restaurantType.setText(mRestaurant.getTypes().toUpperCase());
        restaurantAddress.setText(mRestaurant.getAddress());
        ratingBar.setRating(mRestaurant.getRating());
        Glide.with(this).load(mRestaurant.getUrlPhoto()).into(restaurantPhoto);
    }
}