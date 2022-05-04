package com.safia.go4lunch.repository;

import static android.content.ContentValues.TAG;
import static com.safia.go4lunch.controller.activity.DetailActivity.likeBtn;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesAdapter2;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static volatile UserRepository instance;
    public static final String COLLECTION_LIKED = "restaurant liked";
    public static final String COLLECTION_USERS = "users";

    //--- GET THE COLLECTION REFERENCE ---
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    public CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(COLLECTION_LIKED);
    }

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }


    //--- CREATE ---
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            User userToCreate = new User(uid, username, urlPicture);
            getUsersCollection().document(uid).set(userToCreate);
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //--- GET ---
    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }


    public void setProfileUpdates(String userName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.");
                }
            }
        });
    }

    public Task<Void> addRestaurantLike(Restaurant restaurant) {
        return getLikedCollection().document(restaurant.getRestaurantId()).set(restaurant);
    }

    public Task<Void> removeRestaurantLiked(Restaurant restaurant) {
        return getLikedCollection().document(restaurant.getRestaurantId()).delete();
    }

    public void getLikeStatus(Restaurant restaurant) {
        getLikedCollection().document(restaurant.getRestaurantId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    document.getData();
                }
            }
        });
    }


    public void addPickedRestaurant(Restaurant restaurant) {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            if (user.getRestaurantPicked() != null) {
                RestaurantRepository.getInstance().getRestaurantCollection()
                        .document(user.getRestaurantPicked().getRestaurantId())
                        .collection(RestaurantRepository.USER_PICKED)
                        .document(user.uid).delete();
            }
            user.setRestaurantPicked(restaurant);
            RestaurantRepository.getInstance().getRestaurantCollection()
                    .document(restaurant.getRestaurantId())
                    .collection(RestaurantRepository.USER_PICKED)
                    .document(user.uid).set(user);
            getUsersCollection().document(user.getUid()).set(user);

        });
    }

    public void removePickedRestaurant() {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            RestaurantRepository.getInstance().getRestaurantCollection().document(user.getRestaurantPicked().getRestaurantId()).collection(RestaurantRepository.USER_PICKED).document(user.uid).delete();
            user.setRestaurantPicked(null);
            getUsersCollection().document(user.getUid()).set(user);
        });
    }

    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                users.postValue(userList);
            }
        });
        return users;
    }

    public boolean getCurrentUserPickedStatus() {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            if (user.getRestaurantPicked() != null) {
                RestaurantRepository.getInstance().getRestaurantCollection()
                        .document(user.getRestaurantPicked().getRestaurantId())
                        .collection(RestaurantRepository.USER_PICKED).get();
            }
        });
        return true;
    }
}
