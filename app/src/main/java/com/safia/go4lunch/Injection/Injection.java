package com.safia.go4lunch.Injection;

import android.content.Context;

import com.safia.go4lunch.repository.RestaurantRepository;

public class Injection {

    public static RestaurantRepository provideProjectDataSource(Context context) {
        return new RestaurantRepository();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository mapsSourceItem = provideProjectDataSource(context);
        return new ViewModelFactory(mapsSourceItem);
    }
}
