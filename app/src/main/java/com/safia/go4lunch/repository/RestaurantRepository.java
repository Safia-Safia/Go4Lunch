package com.safia.go4lunch.repository;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;
import com.safia.go4lunch.controller.fragment.maps.MapService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {
    private static final String COLLECTION_RESTAURANT = "restaurants";
    private static volatile RestaurantRepository instance;
    public static final String USER_PICKED = "user picked";

    public CollectionReference getRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT);
    }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public Retrofit createRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(httpClient.build())
                .build();
    }

    public LiveData<List<Restaurant>> getRestaurant(LatLng location) {
        final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        String locationStr = location.latitude + "," + location.longitude;
        List<Restaurant> restaurantList = new ArrayList<>();
        Retrofit retrofit = createRetrofit();

        // Get a Retrofit instance and the related endpoints
        MapService mapService = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        Call<NearbyPlace> call = mapService.getNearbyPlaces(locationStr);
        getRestaurantCollection().get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            call.enqueue(new Callback<NearbyPlace>() {

                                @Override
                                public void onResponse(@NonNull Call<NearbyPlace> call1, @NonNull Response<NearbyPlace> response) {
                                    if (response.body() != null) {
                                        for (Result result1 : response.body().getResults()) {
                                            PlaceDetail placeDetail = getRestaurantDetail(result1);
                                            Restaurant restaurant = new Restaurant();
                                            restaurant.setRestaurantId(result1.getPlaceId());
                                            restaurant.setName(result1.getName());
                                            restaurant.setLatitude(result1.getGeometry().getLocation().getLat());
                                            restaurant.setLongitude(result1.getGeometry().getLocation().getLng());
                                            restaurant.setAddress(placeDetail.getResult().getFormattedAddress());
                                            restaurant.setRating(placeDetail.getResult().getRating().floatValue());
                                            restaurant.setPhoneNumber(placeDetail.getResult().getFormattedPhoneNumber());
                                            restaurant.setWebsite(placeDetail.getResult().getWebsite());
                                            restaurant.setTypes(placeDetail.getResult().getTypes().get(0));
                                            if (placeDetail.getResult().getPhotos() != null) {
                                                restaurant.setUrlPhotoForRetrofit(placeDetail.getResult().getPhotos().get(0).getPhotoReference());
                                            }
                                            restaurant.setOpeningHours(placeDetail.getResult().getOpeningHours());

                                            float[] results = new float[1];
                                            Location.distanceBetween(restaurant.getLatitude(), restaurant.getLongitude(), location.latitude, location.longitude, results);
                                            restaurant.setDistance((int) results[0]);

                                            restaurantList.add(restaurant);

                                            RestaurantRepository.this.getRestaurantCollection().document(restaurant.getRestaurantId()).set(restaurant);

                                        }
                                        result.postValue(restaurantList);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<NearbyPlace> call1, @NonNull Throwable t) {
                                    Log.e("onFailure", "true" + t.getMessage());
                                }
                            });
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Restaurant restaurant = document.toObject(Restaurant.class);
                                getRestaurantCollection().document(restaurant.getRestaurantId()).collection(USER_PICKED).get().addOnCompleteListener(task2 -> {
                                    List<User> users = task2.getResult().toObjects(User.class);
                                    restaurant.setUsers(users);
                                    restaurantList.add(restaurant);
                                    result.postValue(restaurantList);
                                });
                            }
                        }
                    }
                });
        return result;
    }

    public LiveData<List<User>> getAllUsersForThisRestaurant(Restaurant restaurant) {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        getRestaurantCollection().document(restaurant.getRestaurantId()).collection(USER_PICKED).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> userList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                    }
                    users.postValue(userList);
                });
        return users;
    }

    public PlaceDetail getRestaurantDetail(Result result) {
        Retrofit retrofit = createRetrofit();
        // Get a Retrofit instance and the related endpoints
        MapService mapService2 = retrofit.create(MapService.class);
        // Create the call on NearbyPlace API
        Call<PlaceDetail> call = mapService2.getRestaurantDetails(result.getPlaceId());
        // Start the call
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<Boolean> getCurrentUserPickedStatus(Restaurant restaurant) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getRestaurantCollection().document(restaurant.getRestaurantId())
                .collection(RestaurantRepository.USER_PICKED).get().addOnCompleteListener(task -> {
                    List<User> users = task.getResult().toObjects(User.class);
                    boolean isUserFound = false;
                    for ( User user : users){
                        if (user.getUid().equals(UserRepository.getInstance().getCurrentUser().getUid())){
                            isUserFound = true;
                        }
                    }
                    result.postValue(isUserFound);
                });
        return result;
    }
}
