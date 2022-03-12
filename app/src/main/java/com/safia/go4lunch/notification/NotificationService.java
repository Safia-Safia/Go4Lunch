package com.safia.go4lunch.notification;

import static com.safia.go4lunch.repository.UserRepository.getInstance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationService extends FirebaseMessagingService {

    public static final int  NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "GO4LUNCH_NOTIFICATION_TAG";
    private Context context;
    UserRepository userRepository = UserRepository.getInstance();
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    private final RestaurantViewModel restaurantViewModel = RestaurantViewModel.getInstance();
    private User currentUser;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Get message sent by Firebase
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Log.e("TAG" , notification.getBody());
             sendVisualNotification(notification);
        }
    }
    String restaurantName;
    private String currentUserId;

    private void fetchUsers() {
        userRepository.  getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
                        if(user != null && user.getRestaurantPicked().getName() != null) {
                                currentUser = user;
                                restaurantName = currentUser.getRestaurantPicked().getName();
                        }

                });

    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {


        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Create a Channel (Android 8)
        String channelId = getString(R.string.notificcationChannel);
        String message;
        String messageBody;
        message ="You are eating at "+ restaurantName + "." ;
        messageBody = String.format(message, restaurantName);

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_dining_24)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Firebase Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}