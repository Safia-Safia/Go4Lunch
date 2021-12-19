package com.safia.go4lunch.ui.listview;

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
import com.google.gson.Gson;
import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.placeDetailResult.OpeningHours;
import com.safia.go4lunch.model.placeDetailResult.Period;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        TextView restaurantAddress;
        TextView restaurantOpeningHour;
        ImageView restaurantPhoto;
        onRestaurantClickListener mOnRestaurantClickListener;
        Calendar currentHour, currentDate;
        int hour, minute;
        Date closingHour;
        SimpleDateFormat formatter;

        public RestaurantViewHolder(View itemView, onRestaurantClickListener onRestaurantClickListener) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName_textView);
            restaurantAddress = itemView.findViewById(R.id.restaurantAddress_textView);
            restaurantOpeningHour = itemView.findViewById(R.id.restaurant_openings_hour_textView);
            restaurantPhoto = itemView.findViewById(R.id.restaurant_imageView);
            this.mOnRestaurantClickListener = onRestaurantClickListener;
            currentHour = Calendar.getInstance();
            currentDate = Calendar.getInstance();
            formatter = new SimpleDateFormat("kkmm", Locale.FRANCE);


        }

        public void display(Restaurant restaurant, OpeningHours openingHours) {
            this.restaurantName.setText(restaurant.getName());
            this.restaurantAddress.setText(restaurant.getAddress());

            Glide.with(restaurantPhoto.getContext())
                    .load(restaurant.getUrlPhoto())
                    .centerCrop()
                    .into(restaurantPhoto);

            for (Period period : openingHours.getPeriods()){
              if (currentDate.equals(period.getOpen().getDay()) && currentDate != null){
              if ( openingHours == null){
                    restaurantOpeningHour.setText(R.string.unavailable);
                } else if (!openingHours.getOpenNow()) {
                    restaurantOpeningHour.setText(R.string.close);
                    restaurantOpeningHour.setTextColor(Color.RED);
                } else {
                    getRestaurantOpeningHour(restaurant);
                }}
            }

            /*2-Si c'est openUntil, on doit parcourir la liste des perdiods et si le day = au jour actuel (au préalable récupérer le
            jour actuel) dans ce cas on entre dans cette période pour vérifier si l'heure qui est dans l'objet Open est superieur à l'heure actuelle
            et que l'heure de fermeture et avant l'heure actuelle*/
            /*3-Pour savoir si ça ferme bientôt (closingSoon) -> Dans la condition du openUntil, vérifier si
            l'heure actuelle est après l'heure de fermeture -Xminutes Dans ce cas là, on affiche ClosingSoon au lieu d'OpenUntil
             */
        }

        public void getRestaurantOpeningHour(Restaurant restaurant) {
            String str_date = restaurant.getOpeningHours().getPeriods().get(0).getOpen().getTime();
            try {
                hour = Objects.requireNonNull(formatter.parse(str_date)).getHours();
                minute = Objects.requireNonNull(formatter.parse(str_date)).getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            restaurantOpeningHour.setText(MessageFormat.format("Open until {0}:{1}", hour, minute));
        }

        public void getRestaurantClosingHour(Restaurant restaurant) {
            String str_date = restaurant.getOpeningHours().getPeriods().get(0).getClose().getTime();
            try {
                hour = Objects.requireNonNull(formatter.parse(str_date)).getHours();
                minute = Objects.requireNonNull(formatter.parse(str_date)).getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentHour.set(Calendar.HOUR_OF_DAY, hour);
            currentHour.set(Calendar.MINUTE, minute);
            currentHour.add(Calendar.MINUTE, -15);
            closingHour = currentHour.getTime();
            if (currentHour.after(closingHour)){
                restaurantOpeningHour.setText(R.string.closing_soon);
                restaurantOpeningHour.setTextColor(Color.RED);
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
