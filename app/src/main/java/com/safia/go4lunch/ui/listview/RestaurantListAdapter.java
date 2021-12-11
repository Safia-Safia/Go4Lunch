package com.safia.go4lunch.ui.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private final List <Restaurant> mRestaurants;
    private onRestaurantClickListener mOnRestaurantClickListener;

    public RestaurantListAdapter(List<Restaurant> restaurants, onRestaurantClickListener onRestaurantClickListener) {
        mRestaurants = restaurants;
        this.mOnRestaurantClickListener = onRestaurantClickListener;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_recyclerview, parent, false);
        return new RestaurantViewHolder(view, mOnRestaurantClickListener);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.display(restaurant.getName(), restaurant.getAddress(), restaurant.getOpeningHour());
        Glide.with(holder.restaurantPhoto.getContext())
                .load(restaurant.getUrlPhoto())
                .into(holder.restaurantPhoto);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView restaurantName;
        TextView restaurantAddress;
        TextView openingHour;
        ImageView restaurantPhoto;
        onRestaurantClickListener mOnRestaurantClickListener;


        public RestaurantViewHolder(View itemView,onRestaurantClickListener onRestaurantClickListener) {
            super(itemView);
            restaurantName =itemView.findViewById(R.id.restaurantName_textView);
            restaurantAddress =itemView.findViewById(R.id.restaurantAddress_textView);
            openingHour =itemView.findViewById(R.id.restaurant_openings_hour_textView);
            restaurantPhoto = itemView.findViewById(R.id.restaurant_imageView);
            this.mOnRestaurantClickListener = onRestaurantClickListener;
        }

        public void display (String restaurantName, String restaurantAddress,String openingHour){
            this.restaurantName.setText(restaurantName);
            this.restaurantAddress.setText(restaurantAddress);
            this.openingHour.setText(openingHour);
           }


        @Override
        public void onClick(View view) {
            mOnRestaurantClickListener.onRestaurantClick(getAbsoluteAdapterPosition());
        }
    }

    public interface onRestaurantClickListener {
        void onRestaurantClick(int position);
    }


}
