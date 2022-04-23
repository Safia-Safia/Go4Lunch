package com.safia.go4lunch.controller.fragment.workmates;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.fragment.listview.RestaurantListAdapter;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class WorkmatesAdapter2 extends RecyclerView.Adapter<WorkmatesAdapter2.WorkmatesViewHolder2> {

    private final List<User> mUser;
    private final Context context;
    private final onWorkmatesClickListener mOnWorkmatesClickListener;

    public WorkmatesAdapter2(List<User> mUser, Context context, onWorkmatesClickListener onWorkmatesClickListener) {
        this.mUser = mUser;
        this.context = context;
        this.mOnWorkmatesClickListener = onWorkmatesClickListener;
    }

    @NonNull
    @Override
    public WorkmatesAdapter2.WorkmatesViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_user_list, parent, false);
        return new WorkmatesAdapter2.WorkmatesViewHolder2(view, context, mOnWorkmatesClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter2.WorkmatesViewHolder2 holder, int position) {
        holder.updateWithData(mUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public static class WorkmatesViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView isJoining;
        ImageView userPicture;
        Context context;
        onWorkmatesClickListener mOnWorkmatesClickListener;
        User user;

        public WorkmatesViewHolder2(@NonNull View itemView, Context context, onWorkmatesClickListener onWorkmatesClickListener) {
            super(itemView);
            isJoining = itemView.findViewById(R.id.isJoining_textView);
            userPicture = itemView.findViewById(R.id.workmates_avatar);
            itemView.setOnClickListener(this);
            this.context = context;
            this.mOnWorkmatesClickListener = onWorkmatesClickListener;
        }

        public void updateWithData(User user) {
            this.user =user;
            String textToDisplay;
            if (user.getRestaurantPicked() != null) {
                textToDisplay = String.format(context.getString(R.string.display_text_user_list),
                        user.getUsername(), user.getRestaurantPicked().getName());
                isJoining.setTextColor(Color.BLACK);
            } else {
                textToDisplay = String.format(context.getString(R.string.display_text_user_list_not_decided), user.getUsername());
                isJoining.setTextColor(Color.GRAY);
            }

            isJoining.setText(textToDisplay);

            if (!(user.getUrlPicture() == null)) {
                Glide.with(this.itemView).load(user.getUrlPicture()).circleCrop().into(userPicture);
            }
        }

        @Override
        public void onClick(View v) {
            mOnWorkmatesClickListener.onWorkmatesClick(user.getRestaurantPicked());
        }
    }

    public interface onWorkmatesClickListener {
        void onWorkmatesClick(Restaurant restaurant);
    }
}


