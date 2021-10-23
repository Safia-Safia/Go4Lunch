package com.safia.go4lunch.ui.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List <Restaurant> mRestaurants;

    public RestaurantRvAdapter(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return null;//new RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
