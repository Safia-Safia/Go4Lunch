package com.safia.go4lunch.controller.fragment.workmates;

import android.content.res.Resources;
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
import com.safia.go4lunch.model.User;

import java.util.List;

public class WorkmatesAdapter2 extends RecyclerView.Adapter<WorkmatesAdapter2.WorkmatesViewHolder2> {

    private final List<User> mUser;

    public WorkmatesAdapter2(List<User> mUser) {
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public WorkmatesAdapter2.WorkmatesViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_user_list, parent, false);
        return new WorkmatesAdapter2.WorkmatesViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter2.WorkmatesViewHolder2 holder, int position) {
        holder.updateWithData(mUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public static class WorkmatesViewHolder2 extends RecyclerView.ViewHolder {
        TextView isJoining;
        ImageView userPicture;
        Resources res;


        public WorkmatesViewHolder2(@NonNull View itemView) {
            super(itemView);
            isJoining = itemView.findViewById(R.id.isJoining_textView);
            userPicture = itemView.findViewById(R.id.workmates_avatar);
        }

        public void updateWithData(User user) {
            String textToDisplay;
            if (user.getRestaurantPicked().getName() != null) {
                textToDisplay = String.format(res.getString(R.string.display_text_user_list),
                        user.getUsername(),
                        user.getRestaurantPicked().getName());
            } else {
                textToDisplay = String.format(res.getString(R.string.display_text_user_list_not_decided), user.getUsername());
            }

            isJoining.setText(textToDisplay);

            if (!(user.getUrlPicture() == null)) {
                Glide.with(this.itemView).load(user.getUrlPicture()).circleCrop().into(userPicture);
            }
        }
    }
}


