package com.safia.go4lunch.controller.fragment.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

import java.util.List;
import java.util.ResourceBundle;


public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {

    private final List<User> mUser;

    public WorkmatesAdapter(List<User> mUser) {
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public WorkmatesAdapter.WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_user_list, parent, false);
        return new WorkmatesAdapter.WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
       holder.updateWithData(mUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        TextView isJoining;
        ImageView userPicture;

        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
            isJoining = itemView.findViewById(R.id.isJoining_textView);
            userPicture = itemView.findViewById(R.id.workmates_avatar);
        }

        public void updateWithData(User user) {
            if (!(user.getUrlPicture() == null)) {
                Glide.with(this.itemView)
                        .load( userPicture)
                        .circleCrop()
                        .into(userPicture); }
            isJoining.setText(String.format("is joining", user.getUsername()));

        }
    }
}