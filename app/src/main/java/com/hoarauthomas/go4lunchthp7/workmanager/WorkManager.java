package com.hoarauthomas.go4lunchthp7.workmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.PlaceDetailsFinal;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkManager extends Worker {

    String TAG = "[ALARM]";

    //detail object
    PlaceDetailsFinal myRestaurantDetail;

    //for restaurant UI
    String myRestaurantName = null;
    String myRestaurantAddress = null;
    String myRestaurantId = null;
    List<String> myRestaurantWorkmate = new ArrayList<>();

    public WorkManager(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);

        //get user uid ok
        String uid = getInputData().getString("uid");
        Log.i(TAG, "My user uid : " + uid);

        //repositories
        //repositories
        FirestoreRepository myFirestoreRepo = new FirestoreRepository();
        RestaurantsRepository myRestaurantsRepo = new RestaurantsRepository();

        //request user data from firestore
        Task<DocumentSnapshot> myFireUser = myFirestoreRepo.getMyUserByUIDFromFirestore(uid);

        try {

            DocumentSnapshot documentSnapshot = Tasks.await(myFireUser);

            if (documentSnapshot.exists()) {

                Log.i(TAG, "My user name : " + documentSnapshot.get("username"));
                Log.i(TAG, "My user favorite restaurant : " + documentSnapshot.get("favoriteRestaurant"));

                //get restaurant detail with  id ok
                this.myRestaurantDetail = myRestaurantsRepo.getPlaceDetail(Objects.requireNonNull(documentSnapshot.get("favoriteRestaurant")).toString());

                myRestaurantId = myRestaurantDetail.getResult().getPlaceId();
                myRestaurantName = myRestaurantDetail.getResult().getName();
                myRestaurantAddress = myRestaurantDetail.getResult().getFormattedAddress();
                Log.i(TAG, "My restaurant name : " + myRestaurantDetail.getResult().getName());
                Log.i(TAG, "My restaurant address : " + myRestaurantDetail.getResult().getFormattedAddress());

            }


        } catch (Exception e) {
            Log.i(TAG, "no return from firestore : " + e.getMessage());
        }

        //get all workmates by place id
        Task<QuerySnapshot> myAllUser = myFirestoreRepo.getAllUsersByPlaceIdFromFirestore(myRestaurantId);

        try {

            QuerySnapshot queryDocumentSnapshots = Tasks.await(myAllUser);

            for (QueryDocumentSnapshot myItem : queryDocumentSnapshots) {
                Log.i(TAG, "My restaurant workmate(s) : " + myItem.get("username"));
                myRestaurantWorkmate.add(Objects.requireNonNull(myItem.get("username")).toString());
            }

            Log.i(TAG, "My workmate size : " + myRestaurantWorkmate.size());

        } catch (Exception e) {
            Log.i(TAG, "Error on synchronous task for workmate list alarm " + e.getMessage());
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @NotNull
    @Override
    public Result doWork() {

        Log.i("[ALARM]", "Alarm is started ...");

        Context applicationContext = getApplicationContext();


        if (myRestaurantName.isEmpty()){
            return Result.failure();
        }else
        {
            //get data from ui
            String restaurant_title = myRestaurantName;
            String restaurant_address = myRestaurantAddress;


            String[] myWorkmates = new String[myRestaurantWorkmate.size()];

            String delimiter = "\n";
            String result = String.join(delimiter, myRestaurantWorkmate);

            myRestaurantWorkmate.toArray(myWorkmates);

            Log.i(TAG, "my list : " + result);


            try {
                Log.i("[JOB]", "doWork enable...");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create the NotificationChannel, but only on API 26+ because
                    // the NotificationChannel class is new and not in the support library
                    CharSequence name = "go4lunch";
                    String description = "go4lunchdatachannel";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel =
                            new NotificationChannel("go4lunch", "go4lunch", importance);
                    channel.setDescription(description);

                    // Add the channel
                    NotificationManager notificationManager =
                            (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(channel);
                    }
                }

                // Create the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext, "go4lunch")
                        .setSmallIcon(android.R.drawable.star_big_on)
                        .setContentTitle("Go4Lunch It's time to lunch")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(restaurant_title + "\n" + restaurant_address + "\n" + result))
                        .setContentText(restaurant_title + " \n" + restaurant_address + " \n")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[0]);

                // Show the notification
                NotificationManagerCompat.from(applicationContext).notify(1, builder.build());


                return Result.success();

            } catch (Throwable throwable) {

                return Result.failure();

            }
        }
    }
}
