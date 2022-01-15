package com.safia.go4lunch.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RestaurantRepository {
    private static final String COLLECTION_LIKED_NAME = "RESTAURANT_LIKED_NAME";
    public static CollectionReference getLikedCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED_NAME);
    }

    public static Task<Void> createLike(String restaurantId, String userId){
        Map<String,Object> user = new HashMap<>();
        user.put(userId,true);
        return RestaurantRepository.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String restaurantId){
        return RestaurantRepository.getLikedCollection().document(restaurantId).get();
    }


    public static Boolean deleteLike(String restaurantId, String userId){
        RestaurantRepository.getLikeForThisRestaurant(restaurantId).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()){
                Map<String,Object> update = new HashMap<>();
                update.put(userId, FieldValue.delete());
                RestaurantRepository.getLikedCollection().document(restaurantId).update(update);
            }
        });
        return true;
    }
}
