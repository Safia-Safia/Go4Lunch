package com.safia.go4lunch.repository;


import static java.security.AccessController.getContext;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.safia.go4lunch.model.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapService;

import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsRepository {

    public LiveData<List<Restaurant>> getRestaurant(String location, int radius) {
        final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        // Get a Retrofit instance and the related endpoints
        MapService mapService = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        Call<NearbyPlace> call = mapService.getNearbyPlaces(location, radius);
        // Start the call
        call.enqueue(new Callback<NearbyPlace>() {

            @Override
            public void onResponse(@NonNull Call<NearbyPlace> call, @NonNull Response<NearbyPlace> response) {
                Gson gson = new Gson();
                NearbyPlace restaurantList = response.body();
                Log.e("onResponse()", "true" + gson.toJson(restaurantList));
                // result.postValue(restaurantList);
            }

            @Override
            public void onFailure(@NonNull Call<NearbyPlace> call, @NonNull Throwable t) {
                Log.e("onFailure", "true" + t.getMessage());
            }
        });
        return result;
    }
}
