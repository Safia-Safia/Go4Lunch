package com.safia.go4lunch.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapsFragment;

public class DetailActivity extends AppCompatActivity {

    private ImageView restaurantPhoto;
    private TextView restaurantName;
    private TextView restaurantType;
    private ImageButton phoneBtn, likeBtn, websiteBtn;
    private Restaurant mRestaurant;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
        restaurantType = findViewById(R.id.restaurant_type);
        phoneBtn = findViewById(R.id.phone_button);
        likeBtn = findViewById(R.id.like_button);
        websiteBtn = findViewById(R.id.website_button);
        fab = findViewById(R.id.fav);
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
        restaurantType.setText(mRestaurant.getName());
        Glide.with(this).load(mRestaurant.getUrlPhoto()).into(restaurantPhoto);
    }
}