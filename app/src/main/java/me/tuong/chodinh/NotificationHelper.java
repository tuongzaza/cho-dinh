package me.tuong.chodinh;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class NotificationHelper {
    public static void displayNotification(Context context, String title, String text){
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
//                .setContentTitle(title)
//                .setContentText(text)
//                .setSmallIcon(R.drawable.ic_baseline_account_circle_24);
//        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
//        notificationCompat.notify(1,mBuilder.build());

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setSound(uri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(getNotificatonId(), notification);
    }

    private static int getNotificatonId() {
        return (int) new Date().getTime();
    }
}
