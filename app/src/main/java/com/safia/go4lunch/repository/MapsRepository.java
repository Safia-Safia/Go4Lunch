package com.safia.go4lunch.repository;


import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;
import com.safia.go4lunch.ui.maps.MapService;
import com.safia.go4lunch.ui.maps.MapsFragment;

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

public class MapsRepository {
    private static final String COLLECTION_RESTAURANT = "restaurants";

    public  CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT);
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
        // Start the call
        call.enqueue(new Callback<NearbyPlace>() {

            @Override
            public void onResponse(@NonNull Call<NearbyPlace> call, @NonNull Response<NearbyPlace> response) {
                List<Restaurant> restaurantList = new ArrayList<>();
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
                            restaurant.setUrlPhoto(placeDetail.getResult().getPhotos().get(0).getPhotoReference());
                        }
                        restaurant.setOpeningHours(placeDetail.getResult().getOpeningHours());

                        float [] results = new float[1];
                        Location.distanceBetween(restaurant.getLatitude(), restaurant.getLongitude(), location.latitude, location.longitude, results );
                        restaurant.setDistance((int)results[0]);
                        restaurantList.add(restaurant);
                        MapsRepository.this.getRestaurantCollection().document(restaurant.getRestaurantId()).set(restaurant);
                    }
                    result.postValue(restaurantList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyPlace> call, @NonNull Throwable t) {
                Log.e("onFailure", "true" + t.getMessage());
            }
        });
        return result;
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
