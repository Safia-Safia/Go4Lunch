package com.safia.go4lunch.controller.fragment.listview;

import android.graphics.Color;
import android.util.Log;
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
import com.safia.go4lunch.model.placeDetailResult.OpeningHours;
import com.safia.go4lunch.model.placeDetailResult.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private final List<Restaurant> mRestaurants;
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
        holder.display(restaurant, restaurant.getOpeningHours());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantName;
        TextView restaurantAddress, restaurantDistance;
        TextView restaurantOpeningHour;
        ImageView restaurantPhoto;
        onRestaurantClickListener mOnRestaurantClickListener;
        Calendar currentTime;
        SimpleDateFormat formatter;

        public RestaurantViewHolder(View itemView, onRestaurantClickListener onRestaurantClickListener) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName_textView);
            restaurantDistance = itemView.findViewById(R.id.distance_textView);
            restaurantAddress = itemView.findViewById(R.id.restaurantAddress_textView);
            restaurantOpeningHour = itemView.findViewById(R.id.restaurant_openings_hour_textView);
            restaurantPhoto = itemView.findViewById(R.id.restaurant_imageView);
            this.mOnRestaurantClickListener = onRestaurantClickListener;
            currentTime = Calendar.getInstance();
            formatter = new SimpleDateFormat("kkmm", Locale.FRANCE);
        }

        public void display(Restaurant restaurant, OpeningHours openingHours) {
            this.restaurantName.setText(restaurant.getName());
            this.restaurantAddress.setText(restaurant.getAddress());
            this.restaurantDistance.setText(restaurant.getDistance() + " m");

            Glide.with(restaurantPhoto.getContext())
                    .load(restaurant.getUrlPhoto())
                    .centerCrop()
                    .into(restaurantPhoto);

            if (openingHours == null) {
                restaurantOpeningHour.setText(R.string.unavailable);
                restaurantOpeningHour.setTextColor(Color.DKGRAY);

            } else if (!openingHours.getOpenNow()) {
                restaurantOpeningHour.setText(R.string.close);
                restaurantOpeningHour.setTextColor(Color.RED);

                for (Period period : openingHours.getPeriods()) {
                    if (currentTime.get(Calendar.DAY_OF_WEEK) - 1 == period.getOpen().getDay()) {
                        Log.e("restaurantName", restaurant.getName());
                        Calendar openingSoon = Calendar.getInstance();
                        Calendar openingTime = Calendar.getInstance();
                        String openingTimeStr = period.getOpen().getTime();
                        String closingTimeStr = period.getClose().getTime();
                        int openingHour, openingMinute;
                        try {
                            openingHour = Objects.requireNonNull(formatter.parse(openingTimeStr)).getHours();
                            openingMinute = Objects.requireNonNull(formatter.parse(openingTimeStr)).getMinutes();
                            openingSoon.set(Calendar.HOUR_OF_DAY, openingHour);
                            openingSoon.set(Calendar.MINUTE, openingMinute);
                            openingSoon.add(Calendar.MINUTE, -30);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }

                        if (currentTime.after(openingSoon) && currentTime.before(openingTime)) {
                            restaurantOpeningHour.setText("opening soon");
                            restaurantOpeningHour.setTextColor(Color.MAGENTA);
                        }
                    }
                }

            } else if (openingHours.getPeriods().size() == 1) {
                restaurantOpeningHour.setText("Ouvert 24/24h");
                restaurantOpeningHour.setTextColor(Color.GREEN);
            } else {
                for (Period period : openingHours.getPeriods()) {
                    if (currentTime.get(Calendar.DAY_OF_WEEK) - 1 == period.getOpen().getDay()) {
                        Log.e("restaurantName", restaurant.getName());
                        getRestaurantOpeningHour(period);
                    }
                }
            }
        }


        public void getRestaurantOpeningHour(Period period) {
            String openingTimeStr = period.getOpen().getTime();
            String closingTimeStr = period.getClose().getTime();
            int openingHour, openingMinute, closingHour, closingMinute;

            try {
                openingHour = Objects.requireNonNull(formatter.parse(openingTimeStr)).getHours();
                openingMinute = Objects.requireNonNull(formatter.parse(openingTimeStr)).getMinutes();
                closingHour = Objects.requireNonNull(formatter.parse(closingTimeStr)).getHours();
                closingMinute = Objects.requireNonNull(formatter.parse(closingTimeStr)).getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Calendar closingTime = Calendar.getInstance();
            Calendar openingTime = Calendar.getInstance();
            Calendar closingSoon = Calendar.getInstance();

            closingTime.set(Calendar.HOUR_OF_DAY, closingHour);
            closingTime.set(Calendar.MINUTE, closingMinute);

            closingSoon.set(Calendar.HOUR_OF_DAY, closingHour);
            closingSoon.set(Calendar.MINUTE, closingMinute);
            closingSoon.add(Calendar.MINUTE, -30);

            openingTime.set(Calendar.HOUR_OF_DAY, openingHour);
            openingTime.set(Calendar.MINUTE, openingMinute);


            if (currentTime.before(closingTime) && currentTime.after(closingSoon)) {
                restaurantOpeningHour.setText(R.string.closing_soon);
                restaurantOpeningHour.setTextColor(Color.RED);
            } else {
                restaurantOpeningHour.setText(String.format("Open Until %02d:%02d ", closingHour, closingMinute));
                restaurantOpeningHour.setTextColor(Color.GREEN);
            }
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
