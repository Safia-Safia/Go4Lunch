package com.safia.go4lunch.Injection;

import android.content.Context;

import com.safia.go4lunch.repository.MapsRepository;

public class Injection {

    public static MapsRepository provideProjectDataSource(Context context) {
        return new MapsRepository();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        MapsRepository mapsSourceItem = provideProjectDataSource(context);
        return new ViewModelFactory(mapsSourceItem);
    }
}
