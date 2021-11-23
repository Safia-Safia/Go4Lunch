package com.safia.go4lunch.repository;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;
import com.safia.go4lunch.model.placeDetailResult.PlaceDetailResult;
import com.safia.go4lunch.ui.maps.MapService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsRepository {
    public LiveData<List<Restaurant>> getRestaurant(String location) {
        final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors â€¦

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
        Call<NearbyPlace> call = mapService.getNearbyPlaces(location);
        // Start the call
        call.enqueue(new Callback<NearbyPlace>() {

            @Override
            public void onResponse(@NonNull Call<NearbyPlace> call, @NonNull Response<NearbyPlace> response) {
                List<Restaurant> restaurantList = new ArrayList<>();
                if (response.body() != null) {
                    for (Result result1 : response.body().getResults()) {
                        getRestaurantDetail(result1, result);
                        Restaurant restaurant = new Restaurant();
                        restaurant.setRestaurantId(result1.getPlaceId());
                        restaurant.setName(result1.getName());
                        restaurant.setLatitude(result1.getGeometry().getLocation().getLat());
                        restaurant.setLongitude(result1.getGeometry().getLocation().getLng());
                        restaurantList.add(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyPlace> call, @NonNull Throwable t) {
                Log.e("onFailure", "true" + t.getMessage());
            }
        });
        return result;
    }
    public LiveData<List<Restaurant>> getRestaurantDetail(Result result, LiveData<List<Restaurant>> resultRestaurants) {
        final MutableLiveData<List<Restaurant>> resultDetail = new MutableLiveData<>();

        HttpLoggingInterceptor detailLogging = new HttpLoggingInterceptor();
        // set your desired log level
        detailLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors â€¦

        // add logging as last interceptor
        httpClient.addInterceptor(detailLogging);  // <-- this is the important line!


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/details/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        // Get a Retrofit instance and the related endpoints
        MapService mapService2 = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        Call<PlaceDetail> call = mapService2.getRestaurantDetails(result);
        // Start the call
        call.enqueue(new Callback<PlaceDetail>() {

            @Override
            public void onResponse(@NonNull Call<PlaceDetail> call, @NonNull Response<PlaceDetail> response) {
                //TODO faire le mapping
                //TODO rajouter le nouvel objet restaurant dans une liste
                //TODO Lorsque cette liste aura la mÃªme taille que la liste de
                // getResult de la ligne 60, on fera le post value
                List<Restaurant> restaurantList1 = new ArrayList<>();
                if (response.body() != null) {
                    for (PlaceDetail results : response.body().getResult()) {
                        Restaurant restaurant = new Restaurant();
                        restaurant.setAddress(results.getAdrAddress());
                        restaurant.setRating((int) results.getRating());
                        restaurant.setPhoneNumber(results.getFormattedPhoneNumber());
                        restaurant.setOpeningHours(results.getOpeningHours().getOpenNow().toString());
                        restaurantList1.add(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetail> call, @NonNull Throwable t) {
                Log.e("onFailure", "true" + t.getMessage());
            }
        });
        return resultDetail;
    }
}
