package com.safia.go4lunch.Injection;

import android.content.Context;

import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;

public class Injection {

    public static RestaurantRepository provideRestaurantDataSource(Context context) {
        return new RestaurantRepository();
    }

    public static UserRepository provideUserDataSource(Context context) {
        return new UserRepository();
    }


    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantRepository restaurantDataSource= provideRestaurantDataSource(context);
        UserRepository userDataSource = provideUserDataSource(context);
        return new ViewModelFactory(restaurantDataSource, userDataSource);
    }
}
