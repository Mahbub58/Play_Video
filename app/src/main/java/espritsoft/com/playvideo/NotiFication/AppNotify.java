package espritsoft.com.playvideo.NotiFication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppNotify extends Application {
    public static final String CHANNEL_1_ID="channel1";
    public static final String CHANNEL_2_ID="channel2";
    public static final String CHANNEL_3_ID="channel3";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PLAY="PLAY";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1= new NotificationChannel(
                    CHANNEL_1_ID,
                    "Chaannel1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("This is channel1");

            NotificationChannel channel2= new NotificationChannel(
                    CHANNEL_2_ID,
                    "Chaannel2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is channel2");

            NotificationChannel channel3= new NotificationChannel(
                    CHANNEL_3_ID,
                    "Chaannel2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel3.setDescription("This is channel3");


            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);

        }
    }
}
