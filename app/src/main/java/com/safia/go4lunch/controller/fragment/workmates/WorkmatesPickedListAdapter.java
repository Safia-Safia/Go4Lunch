package com.safia.go4lunch.controller.fragment.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safia.go4lunch.R;
import com.safia.go4lunch.model.User;

import java.util.List;


public class WorkmatesPickedListAdapter extends RecyclerView.Adapter<WorkmatesPickedListAdapter.WorkmatesViewHolder> {

    private final List<User> mUser;
    Context context;

    public WorkmatesPickedListAdapter(List<User> mUser) {
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public WorkmatesPickedListAdapter.WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_user_list, parent, false);
        return new WorkmatesPickedListAdapter.WorkmatesViewHolder(view);
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
                        .load( user.getUrlPicture())
                        .circleCrop()
                        .into(userPicture); }
            isJoining.setText( isJoining.getContext().getString(R.string.isJoining, user.getUsername()));

        }
    }
}