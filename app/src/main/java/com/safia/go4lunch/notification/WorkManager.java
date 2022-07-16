package com.safia.go4lunch.notification;


import static android.content.Context.MODE_PRIVATE;
import static com.safia.go4lunch.repository.RestaurantRepository.USER_PICKED;
import static com.safia.go4lunch.repository.UserRepository.getInstance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.DocumentSnapshot;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.controller.activity.HomeActivity;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WorkManager extends Worker {
    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "GO4LUNCH_NOTIFICATION_TAG";
    private static final String NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL";
    public static final String GO_4_LUNCH_MESSAGES = "GO4LUNCH MESSAGES";

    UserRepository userRepository = UserRepository.getInstance();
    RestaurantRepository restaurantRepository = RestaurantRepository.getInstance();


    public WorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public void createNotification(String message, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);


        String channelId = NOTIFICATION_CHANNEL;

        // Build a WorkManager object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), channelId)
                        .setSmallIcon(R.drawable.ic_baseline_dining_24)
                        .setContentTitle(getApplicationContext().getString(R.string.notification_title))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
        Log.e("Notification message ", message);


        // Support Version >= Android 8
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = GO_4_LUNCH_MESSAGES;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    private void fetchUsers() {
        userRepository.getUsersCollection().document(Objects.requireNonNull(getInstance()
                .getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            if (user.getRestaurantPicked() == null) {
                Intent intent = new Intent(this.getApplicationContext(), HomeActivity.class);
                String message = getApplicationContext().getString(R.string.nowherePlace);
                createNotification(message, intent);
            } else {
                if (user.getRestaurantPicked() != null) {
                    restaurantRepository.getRestaurantCollection().document(user.getRestaurantPicked().getRestaurantId()).collection(USER_PICKED).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<String> userList = new ArrayList<>();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    User userJoining = documentSnapshot.toObject(User.class);
                                    userList.add(userJoining.getUsername());
                                }
                                userList.remove(userRepository.getCurrentUser().getDisplayName());
                                String users = TextUtils.join(", ", userList.toArray());
                                middayNotification(user.getRestaurantPicked(), users);
                            });
                }
            }
        });
    }

    private void middayNotification(Restaurant restaurant, String usersJoining) {
        // Create an Intent that will be shown when user will click on the WorkManager
        Intent intent = new Intent(this.getApplicationContext(), DetailActivity.class);
        intent.putExtra(MapsFragment.KEY_RESTAURANT, restaurant);
        String message;

        if (usersJoining.length() == 0) {
            message =  String.format(getApplicationContext().getString(R.string.eatingWithNobody),restaurant.getName(), restaurant.getAddress());
        } else {
            message = String.format(getApplicationContext().getString(R.string.eatingWithSomebody), restaurant.getName(),restaurant.getAddress(),usersJoining);
        }
        createNotification(message, intent);
    }

    public static void scheduleWork() {
        // -- Time now --
        Calendar timeNow = Calendar.getInstance();
        int hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = Calendar.getInstance().get(Calendar.MINUTE);
        long scheduleTime;
        Calendar midday = Calendar.getInstance();
        //-- Set time needed --
        midday.set(Calendar.HOUR_OF_DAY, 14);
        midday.set(Calendar.MINUTE, 34);
        midday.set(Calendar.SECOND, 0);
        //-- If it's midday start notification --
        if (hourNow >= 23 && minuteNow > 50) {
            midday.add(Calendar.DATE, 1);
        }
        //-- Else calculate the delay between currentHour and next midday --
        scheduleTime = midday.getTimeInMillis() - timeNow.getTimeInMillis();

            //-- Delay work request --
            WorkRequest request
                    = new OneTimeWorkRequest
                    .Builder(WorkManager.class)
                    .setInitialDelay(scheduleTime, TimeUnit.MILLISECONDS)
                    .build();

        androidx.work.WorkManager.getInstance()
                .enqueue(request);
    }

    @NonNull
    @Override
    public Result doWork() {
        fetchUsers();
        return Result.success();
    }
}