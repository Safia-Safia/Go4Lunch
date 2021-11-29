package com.safia.go4lunch.ui.listview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantRvAdapter extends RecyclerView.Adapter<RestaurantRvAdapter.RestaurantViewHolder> {

    private List <Restaurant> mRestaurants;

    public RestaurantRvAdapter(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
    }


    @NonNull
    @Override
    public RestaurantRvAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.display(restaurant.getName(), restaurant.getAddress(), restaurant.getOpeningHour());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView restaurantAddress;
        TextView openingHour;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName =itemView.findViewById(R.id.restaurantName_textView);
            restaurantAddress =itemView.findViewById(R.id.restaurantAddress_textView);
            openingHour =itemView.findViewById(R.id.restaurant_openings_hour_textView);
        }

        public void display (String restaurantName, String restaurantAddress,String openingHour){
            this.restaurantName.setText(restaurantName);
            this.restaurantAddress.setText(restaurantAddress);
            this.openingHour.setText(openingHour);
        }
    }


}
