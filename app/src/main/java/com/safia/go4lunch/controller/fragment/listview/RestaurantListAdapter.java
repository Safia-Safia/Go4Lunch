package com.safia.go4lunch.controller.fragment.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements Filterable {

    private  List<Restaurant> mRestaurants;
    private  List<Restaurant> restaurantsAll;
    private onRestaurantClickListener mOnRestaurantClickListener;
    public static final double MAX_STAR = 3;
    public static final double MAX_RATING = 5;

    public RestaurantListAdapter(List<Restaurant> restaurants, onRestaurantClickListener onRestaurantClickListener) {
        mRestaurants = restaurants;
        restaurantsAll = restaurants;
        this.mOnRestaurantClickListener = onRestaurantClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_recyclerview, parent, false);
        return new RestaurantViewHolder(view, mOnRestaurantClickListener);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.display(restaurant, restaurant.getOpeningHours());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = restaurantsAll.size();
                    filterResults.values = restaurantsAll;
                } else {

                    List<Restaurant> result = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for (Restaurant item : restaurantsAll) {
                        if (item.getName().toLowerCase().contains(searchStr)) {
                            result.add(item);

                        }

                    }
                    filterResults.count = result.size();
                    filterResults.values = result;
                }

                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRestaurants = (List<Restaurant>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface onRestaurantClickListener {
        void onRestaurantClick(int position);
    }

}
