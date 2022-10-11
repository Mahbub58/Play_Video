package espritsoft.com.playvideo.NotiFication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;


public class NotificationRecever extends BroadcastReceiver {

    public static final String ACTION_PLAY_PAUSE="PLAY_PAUSE";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_DISMIS="DISMIS";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PAUSE="PAUSE";
    public static final String ACTION_PLAY="PLAY";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AudioPlaySystem.class);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_PLAY_PAUSE:
                    Toast.makeText(context, "Play_Pause", Toast.LENGTH_SHORT).show();
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_NEXT:
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PREV:
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_DISMIS:
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PAUSE:
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PLAY:
                    intent1.putExtra("myActionName", intent.getAction());
                    context.startService(intent1);
                    break;
            }
        }

    }

}
