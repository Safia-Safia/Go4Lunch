package com.safia.go4lunch.repository;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetailResult;
import com.safia.go4lunch.ui.maps.MapService;

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

    public Retrofit createRetrofit(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors â€¦

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
    public LiveData<List<Restaurant>> getRestaurant(String location) {
        final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        Retrofit retrofit = createRetrofit();


        // Get a Retrofit instance and the related endpoints
        MapService mapService = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        Call<NearbyPlace> call = mapService.getNearbyPlaces(location);
        // Start the call
        call.enqueue(new Callback<NearbyPlace>() {

            @Override
            public void onResponse(@NonNull Call<NearbyPlace> call, @NonNull Response<NearbyPlace> response) {
                List<Restaurant> restaurantList = new ArrayList<>();
                if (response.body() != null) {
                    for (Result result1 : response.body().getResults()) {
                        PlaceDetail placeDetail= getRestaurantDetail(result1);
                        Restaurant restaurant = new Restaurant();
                        restaurant.setRestaurantId(result1.getPlaceId());
                        restaurant.setName(result1.getName());
                        restaurant.setLatitude(result1.getGeometry().getLocation().getLat());
                        restaurant.setLongitude(result1.getGeometry().getLocation().getLng());
                        restaurant.setAddress(placeDetail.getResult().getAdrAddress());
                        restaurant.setRating(placeDetail.getResult().getRating());
                        restaurant.setPhoneNumber(placeDetail.getResult().getFormattedPhoneNumber());
                        //restaurant.setOpeningHour(result1.getOpeningHours().getOpenNow().toString());
                        restaurantList.add(restaurant);
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
