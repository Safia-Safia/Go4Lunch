package com.safia.go4lunch.repository;


import static com.safia.go4lunch.controller.activity.DetailActivity.fab;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesAdapter;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.model.placeDetailResult.OpeningHours;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;
import com.safia.go4lunch.controller.fragment.maps.MapService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        Retrofit retrofit = createRetrofit();

        // Get a Retrofit instance and the related endpoints
        MapService mapService = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        String locationStr = location.latitude + "," + location.longitude;
        Call<NearbyPlace> call = mapService.getNearbyPlaces(locationStr);
        List<Restaurant> restaurantList = new ArrayList<>();
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
                                document.getData();
                                Gson gson = new Gson();

                                String documentGson = gson.toJson(document.getData());

                                Restaurant restaurant = new Restaurant();
                                restaurant.setRestaurantId((String) document.getData().get("restaurantId"));
                                restaurant.setName((String) document.getData().get("name"));
                                restaurant.setLatitude((double) document.getData().get("latitude"));
                                restaurant.setLongitude((double) document.getData().get("longitude"));
                                restaurant.setAddress((String) document.getData().get("address"));
                                restaurant.setRating((double) document.getData().get("rating"));
                                restaurant.setPhoneNumber((String) document.getData().get("phoneNumber"));
                                restaurant.setWebsite((String) document.getData().get("website"));
                                restaurant.setTypes((String) document.getData().get("types"));
                                if (document.getData().get("urlPhoto") != null) {
                                    restaurant.setUrlPhoto((String) document.getData().get("urlPhoto"));
                                }

                                JsonElement jsonElement = gson.toJsonTree(document.getData().get("openingHours"));
                                OpeningHours openingHours = gson.fromJson(jsonElement, OpeningHours.class);
                                restaurant.setOpeningHours(openingHours);

                                restaurant.setDistance((long) document.getData().get("distance"));

                                restaurantList.add(restaurant);
                            }
                            result.postValue(restaurantList);
                        }

                    }
                });

        return result;
    }


    public Task<QuerySnapshot> getAllUsersForThisRestaurant(Restaurant restaurant, List<User> userList, WorkmatesAdapter adapter) {
        getRestaurantCollection().document(restaurant.getRestaurantId()).collection(USER_PICKED).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.clear();
                        userList.add(user);
                        adapter.notifyDataSetChanged();
                    }
                });
        return getRestaurantCollection().document(restaurant.getRestaurantId())
                .collection(USER_PICKED).get();
    }

    public void setRestaurantStatus(Restaurant restaurant){
        RestaurantRepository.getInstance().getRestaurantCollection().document(restaurant.getRestaurantId())
                .collection(RestaurantRepository.USER_PICKED).document(UserRepository.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
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
}
