package com.safia.go4lunch.controller.activity;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private Uri uriImageSelected;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    ImageView userProfilePicture;
    EditText userName;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        userName = findViewById(R.id.editUserName);
        updateBtn = findViewById(R.id.update_btn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository.getInstance().setProfileUpdates(userName.getText().toString());
            }
        });
        userProfilePicture = findViewById(R.id.profilePicture_setting);
        String userPhotoUrl = (UserViewModel.getInstance().getCurrentUser().getPhotoUrl() != null) ?UserViewModel.getInstance().getCurrentUser().getPhotoUrl().toString() : null;
        Glide.with(this)
                .load(userPhotoUrl)
                .circleCrop()
                .into(userProfilePicture);
    }



    @Override
    public void onBackPressed() {
            super.onBackPressed();
        Log.e("backpressed" , "clicked");
            this.finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            this.handleResponse(requestCode, resultCode, data);
        }

        @AfterPermissionGranted(RC_IMAGE_PERMS)
        private void addFile(){
            if (!EasyPermissions.hasPermissions(this, PERMS)) {
                EasyPermissions.requestPermissions(this, "Access", RC_IMAGE_PERMS, PERMS);
                return;
            }
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RC_CHOOSE_PHOTO);
        }

        // Handle activity response (after user has chosen or not a picture)
        private void handleResponse(int requestCode, int resultCode, Intent data){
            if (requestCode == RC_CHOOSE_PHOTO) {
                if (resultCode == RESULT_OK) { //SUCCESS
                    this.uriImageSelected = data.getData();
                } else {
                    Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
