package com.safia.go4lunch.controller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesAdapter;
import com.safia.go4lunch.databinding.ActivityDetailBinding;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView restaurantPhoto;
    private TextView restaurantName, restaurantAddress, restaurantType;
    private ImageButton phoneBtn, likeBtn, websiteBtn;
    public Restaurant mRestaurant;
    private FloatingActionButton fab;
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    private final RestaurantViewModel restaurantViewModel = RestaurantViewModel.getInstance();
    public static final double MAX_STAR = 3;
    public static final double MAX_RATING = 5;
    private boolean likeOn = false;
    private boolean fabOn = false;
    RatingBar ratingBar;
    List<User> userList = new ArrayList<>();
    WorkmatesAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.safia.go4lunch.databinding.ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpView();
        initView();
        initLikeBtn();
        initWebsiteBtn();
        initPhoneBtn();
        initFabButton();
        displayRating();
        likeStatus();
        pickedStatus();
        configureRecyclerView();
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

    @SuppressLint("NotifyDataSetChanged")
    private void configureRecyclerView() {
        adapter = new WorkmatesAdapter(userList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantViewModel.getAllUserForThisRestaurant(mRestaurant)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                        adapter.notifyDataSetChanged();
                    }
                });
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

    private void initLikeBtn() {
        likeBtn.setOnClickListener(view -> {
            if (likeOn) {
                userViewModel.removeRestaurantLiked(mRestaurant);
                likeBtn.setImageResource(R.drawable.ic_baseline_star_border_24);
                likeOn = false;
            } else {
                userViewModel.addLikeForThisRestaurant(mRestaurant);
                likeBtn.setImageResource(R.drawable.ic_star_yellow);
                likeOn = true;
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
        fab.setOnClickListener(view -> {
            if (fabOn) {
                userViewModel.removeRestaurantPicked();
                fab.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            } else {
                userViewModel.addPickedRestaurant(mRestaurant);
                fab.setImageResource(R.drawable.ic_baseline_check_circle_24);
                fabOn = true;
            }
        });
    }

    private void displayRating() {
        if (mRestaurant.getRestaurantId() != null) {
            double rating = mRestaurant.getRating() / MAX_RATING * MAX_STAR;
            ratingBar.setRating((float) rating);
        }
    }


    public void likeStatus() {
        UserRepository.getInstance().getLikedCollection().document(mRestaurant.getRestaurantId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        likeBtn.setImageResource(R.drawable.ic_star_yellow);
                    } else {
                        likeBtn.setImageResource(R.drawable.ic_baseline_star_border_24);

                    }
                }
            }
        });
    }

    public void pickedStatus() {
        RestaurantRepository.getInstance().getRestaurantCollection().document(mRestaurant.getRestaurantId())
                .collection(RestaurantRepository.USER_PICKED).document(userViewModel.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    document.getData();
                    fab.setImageResource(R.drawable.ic_baseline_check_circle_24);
                } else {
                    fab.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                }
            }
        });

    }
}