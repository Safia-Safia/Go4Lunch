package com.safia.go4lunch.notification;


import static com.safia.go4lunch.repository.UserRepository.getInstance;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.safia.go4lunch.R;
import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.controller.fragment.listview.ListViewFragment;
import com.safia.go4lunch.controller.fragment.maps.MapsFragment;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WorkManager extends Worker {
    private final static String WORK_NAME = "WORK_NAME";
    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "GO4LUNCH_NOTIFICATION_TAG";
    private static final String NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL";
    UserRepository userRepository = UserRepository.getInstance();
    String restaurantName, restaurantAddress;
    public Restaurant mRestaurant;
    private SharedPreferences mPreferences;

    public WorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void createNotification(Restaurant restaurant) {

        if (mRestaurant != null){
            // Create an Intent that will be shown when user will click on the WorkManager
            Intent intent = new Intent(this.getApplicationContext(), DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else {
            Intent intent = new Intent(this.getApplicationContext(), ListViewFragment.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Toast.makeText(this.getApplicationContext(), "Où manger ce midi ?", Toast.LENGTH_SHORT).show();
        }


        String channelId = NOTIFICATION_CHANNEL;
        String message = getApplicationContext().getString(R.string.eatingPlace) + restaurantName + ", \n" + restaurantAddress+ ".";

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
            CharSequence channelName = "GO4LUNCH MESSAGES";
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
            if (user.getRestaurantPicked() != null) {
                // marchepasuserRepository.getUserRestaurant().observe((LifecycleOwner) this.getApplicationContext(), restaurant -> mRestaurant = restaurant);

                Log.e("Restaurant picked " ,mRestaurant +"" );
                restaurantName = user.getRestaurantPicked().getName();
                restaurantAddress = user.getRestaurantPicked().getAddress();
            } else restaurantName = getApplicationContext().getString(R.string.nowherePlace);
            createNotification(mRestaurant);
        });
    }

    //Dans le MainActivity appeler la methode scheduleWork
    public static void scheduleWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        Date currentTime = Calendar.getInstance().getTime();
      //  Date midday =
        WorkRequest request
                = new OneTimeWorkRequest
                .Builder(WorkManager.class)
                .setConstraints(constraints)
                .build();

        androidx.work.WorkManager.getInstance()
                .enqueue(request);
    }

    @NonNull
    @Override
    public Result doWork() {
        //fetchUser, les infos du restaurant choisit et lancer la notification
        fetchUsers();
        return Result.success();
    }
}