package me.tuong.chodinh;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import me.tuong.chodinh.room.News;
import me.tuong.chodinh.room.NewsDatabase;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

//        if (remoteMessage.getNotification() != null){
//            String title = remoteMessage.getNotification().getTitle();
//            String text = remoteMessage.getNotification().getBody();
//
//        }

        if (remoteMessage.getData() != null){
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String text = data.get("body");

            //send notification to mysql
            News news = new News(title,text);
            NewsDatabase.getInstance(this).newsDAO().insertNews(news);

            //calling method to display notification
            NotificationHelper.displayNotification(getApplicationContext(), title, text);
        }
    }
}
