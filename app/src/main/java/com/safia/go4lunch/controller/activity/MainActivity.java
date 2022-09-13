package com.safia.go4lunch.controller.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.safia.go4lunch.Injection.Injection;
import com.safia.go4lunch.Injection.ViewModelFactory;
import com.safia.go4lunch.R;
import com.safia.go4lunch.notification.WorkManager;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.Collections;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.safia.go4lunch.controller.activity.SettingsActivity.NOTIFICATIONS_PREFERENCES;
import static com.safia.go4lunch.controller.activity.SettingsActivity.PREFERENCES_VALUE;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private UserViewModel viewModel;
    private Button facebookButton, googleButton;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        setUpView();
        configureViewModel();
        isUserLogged();
        setupListeners();

        //Get the preferences of the user to receive notification
        sharedPreferences = this.getSharedPreferences(NOTIFICATIONS_PREFERENCES, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(PREFERENCES_VALUE,true)){
            WorkManager.scheduleWork();
        }
    }

    public void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    private void setUpView() {
        googleButton = findViewById(R.id.login_button_google);
        facebookButton = findViewById(R.id.login_button_facebook);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        progressBar.setVisibility(View.INVISIBLE);

        facebookButton.setOnClickListener(view ->
                signInBuilder(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build()))
        );
        googleButton.setOnClickListener(view ->
                signInBuilder(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
        );
    }

    private void signInBuilder(List<AuthUI.IdpConfig> providers) {
        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Show Toast with a message
    private void showToast(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                viewModel.createUser();
                showToast(String.valueOf(getString(R.string.connected)));
                startHomeActivity();
                progressBar.setVisibility(View.INVISIBLE);
            } else if (response == null) {
                showToast(String.valueOf(getString(R.string.notConnected)));
            }
        }
    }

    public void isUserLogged() {
        if (viewModel.isCurrentUserLogged()) {
            startHomeActivity();
        } else {
            startActivity(this.getIntent());
        }
    }

    private void startHomeActivity() {
        Intent homeActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(homeActivityIntent);
        finish();
    }
}