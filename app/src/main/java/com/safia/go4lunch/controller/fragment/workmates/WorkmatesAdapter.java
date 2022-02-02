package com.safia.go4lunch.controller.fragment.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.User;

import java.util.List;


public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {

    private final List<User> mUser;

    public WorkmatesAdapter(List<User> mUser) {
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public WorkmatesAdapter.WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workmates, parent, false);
        return new WorkmatesAdapter.WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        //holder.updateWithUser();

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        TextView isJoining;
        ImageView userPicture;
       // String userPhotoUrl = (UserViewModel.getCurrentUser().getPhotoUrl() != null) ? UserViewModel.getCurrentUser().getPhotoUrl().toString() : null;

        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
            isJoining = itemView.findViewById(R.id.isJoining_textView);
            userPicture = itemView.findViewById(R.id.workmates_avatar);
        }

        void updateWithUser(User user){
            //isJoining.setText(R.string.isEatingHere, user.username,  );

        }

    }

}
