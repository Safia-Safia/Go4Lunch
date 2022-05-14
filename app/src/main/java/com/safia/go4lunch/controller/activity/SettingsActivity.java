package com.safia.go4lunch.controller.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.safia.go4lunch.R;
import com.safia.go4lunch.databinding.SettingsActivityBinding;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.UserViewModel;

import org.w3c.dom.Text;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCE_APP_NAME = "PREFERENCE_APP_NAME";
    ImageView userProfilePicture;
    Button updateBtn;
    TextView userName;
    private SharedPreferences mPreferences;
    Switch switchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        mPreferences = getSharedPreferences(PREFERENCE_APP_NAME, MODE_PRIVATE);
//        switchBtn.findViewById(R.id.switchBtn);
        updateBtn = findViewById(R.id.update_btn);
        userName = findViewById(R.id.username_settings);
        userProfilePicture = findViewById(R.id.profilePicture_setting);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userName.setText(UserRepository.getInstance().getCurrentUser().getDisplayName());
        String userPhotoUrl = (UserViewModel.getInstance().getCurrentUser().getPhotoUrl() != null) ? UserViewModel.getInstance().getCurrentUser().getPhotoUrl().toString() : null;
        Glide.with(this)
                .load(userPhotoUrl)
                .circleCrop()
                .into(userProfilePicture);
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
