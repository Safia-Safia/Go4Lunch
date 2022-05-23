package com.safia.go4lunch.controller.activity;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantAndUserViewModel;

import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCE_APP_NAME = "PREFERENCE_APP_NAME";
    ImageView userProfilePicture;
    Button updateBtn;
    TextView userName;
    private RestaurantAndUserViewModel restaurantAndUserViewModel;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        mPreferences = getSharedPreferences(PREFERENCE_APP_NAME, MODE_PRIVATE);
//        switchBtn.findViewById(R.id.switchBtn);
        updateBtn = findViewById(R.id.update_btn);
        userName = findViewById(R.id.username_settings);
        userProfilePicture = findViewById(R.id.profilePicture_setting);
        configureViewModel();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userName.setText(UserRepository.getInstance().getCurrentUser().getDisplayName());
        String userPhotoUrl = (restaurantAndUserViewModel.getCurrentUser().getPhotoUrl() != null) ? restaurantAndUserViewModel.getCurrentUser().getPhotoUrl().toString() : null;
        Glide.with(this)
                .load(userPhotoUrl)
                .circleCrop()
                .into(userProfilePicture);
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.restaurantAndUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantAndUserViewModel.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("backpressed", "clicked");
        this.finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
