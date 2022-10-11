package espritsoft.com.playvideo.AudioPlay.AudiouServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonEventReceiver extends BroadcastReceiver {

    public static final String ACTION_PLAY_PAUSE="PLAY_PAUSE";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_DISMIS="DISMIS";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PAUSE="PAUSE";
    public static final String ACTION_PLAY="PLAY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("TestApp", "Button press received");
        abortBroadcast();
        Intent intent1 = new Intent(context, AudioPlaySystem.class);
        KeyEvent key = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        try{
        if(key.getAction() == KeyEvent.ACTION_UP) {
            int keycode = key.getKeyCode();
            Toast.makeText(context, "Media="+keycode, Toast.LENGTH_LONG).show();
            if(keycode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                intent1.putExtra("myActionName", ACTION_NEXT);
                context.startService(intent1);
            } else if(keycode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                intent1.putExtra("myActionName", ACTION_PREV);
                context.startService(intent1);
            } else if(keycode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                intent1.putExtra("myActionName", ACTION_PLAY);
                context.startService(intent1);
            }else if(keycode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                intent1.putExtra("myActionName", ACTION_PAUSE);
                context.startService(intent1);
            }
            // head phone
            else if(keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
                intent1.putExtra("myActionName", ACTION_PLAY_PAUSE);
                context.startService(intent1);
            }
        }
        }catch (Exception e){}



    }

}
