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
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.UserViewModel;

import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {
    ImageView userProfilePicture;
    Button updateBtn;
    TextView userName;
    SwitchCompat btnSwitch;
    private UserViewModel restaurantAndUserViewModel;
    public static final String NOTIFICATIONS_PREFERENCES = "Notifications preferences";
    public static final String PREFERENCES_VALUE = "PREFERENCES_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        updateBtn = findViewById(R.id.update_btn);
        btnSwitch = findViewById(R.id.switchWidget);
        userName = findViewById(R.id.username_settings);
        userProfilePicture = findViewById(R.id.profilePicture_setting);
        configureViewModel();
        setUpToggleBtn();

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
        this.restaurantAndUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public  void setUpToggleBtn(){
        SharedPreferences sharedPreferences = getSharedPreferences(NOTIFICATIONS_PREFERENCES, MODE_PRIVATE);
        btnSwitch.setChecked(sharedPreferences.getBoolean(PREFERENCES_VALUE,true));

        btnSwitch.setOnClickListener(view -> {
            if (btnSwitch.isChecked()) {
                // When switch checked
                SharedPreferences.Editor editor = getSharedPreferences(NOTIFICATIONS_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(PREFERENCES_VALUE, true);
                editor.apply();
                btnSwitch.setChecked(true);
            } else {
                // When switch unchecked
                SharedPreferences.Editor editor = getSharedPreferences(NOTIFICATIONS_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(PREFERENCES_VALUE, false);
                editor.apply();
                btnSwitch.setChecked(false);
            }
        });
    }
}
