package espritsoft.com.playvideo.AudioPlay.AudiouServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class HeadsetPlugInRecever extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        int iii;
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            iii = intent.getIntExtra("state", -1);
            Toast.makeText(context, "Home="+iii, Toast.LENGTH_LONG).show();
            if (iii == 0) {
//                        actionPlaying.PlayPause();
                Toast.makeText(context, "microphone not plugged in", Toast.LENGTH_LONG).show();
            }
            if (iii == 1) {
                Toast.makeText(context, "microphone plugged in",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

}
