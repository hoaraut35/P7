package com.hoarauthomas.go4lunchthp7.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class NotificationService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getNotification() != null) {

            RemoteMessage.Notification notification = remoteMessage.getNotification();

            Log.i("[NOTIF]",notification.getBody());
        }



    }
}
