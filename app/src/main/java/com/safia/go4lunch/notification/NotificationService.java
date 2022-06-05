package com.safia.go4lunch.notification;

import static com.safia.go4lunch.repository.UserRepository.getInstance;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

import java.util.Objects;

public class NotificationService extends FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "GO4LUNCH_NOTIFICATION_TAG";
    UserRepository userRepository = UserRepository.getInstance();
    String restaurantName;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Get message sent by Firebase
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Log.e("TAG", notification.getBody());
            sendVisualNotification(notification);
        }
    }


    private void fetchUsers(RemoteMessage.Notification notification) {
        userRepository.getUsersCollection().document(Objects.requireNonNull(getInstance()
                .getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            if (user.getRestaurantPicked() != null) {
                restaurantName = user.getRestaurantPicked().getName();
            } else restaurantName = getString(R.string.nowherePlace);
            createNotification(notification);
        });
    }

    private void createNotification(RemoteMessage.Notification notification){
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.notificationChannel);
        String message = getString(R.string.eatingPlace) + restaurantName + ".";

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_dining_24)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
        Log.e("TAG", message );

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {
        fetchUsers(notification);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}