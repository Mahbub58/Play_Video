package espritsoft.com.playvideo.AudioPlay.AudiouServices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudioPlay;
import espritsoft.com.playvideo.AudioPlay.ViewModel.AudiouPlayViewModel;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.NotiFication.AppNotify;
import espritsoft.com.playvideo.NotiFication.NotificationRecever;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;
import espritsoft.com.playvideo.Video.VideoModel;

public class AudioPlaySystem extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener,ActionPlaying {


    public MediaPlayer   mediaPlayer =new MediaPlayer() ;
    AudioManager audioManager;
   String mediaFile;
   List<AudiouModel> currentList;
   static Context context;
  // boolean musicPlayerIsPlaing=false;

    public static boolean audioService=false;




    // Binder given to clients
    private final IBinder iBinder = new MyBinder();




    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void nextClick() {
        audioPlayNext();
    }

    @Override
    public void previousClick() {
        audioPlayPrevious();
    }

    @Override
    public void PlayPause() {
        if(mediaPlayer.isPlaying()){
            Pause();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_play_arrow_black_24,true);
            }
            //musicPlayerIsPlaing=false;

        }else{
            AudioPlaySystem.getInstance().playMedia();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_pause_black_24,true);
            }
            // musicPlayerIsPlaing=true;
        }
    }

    @Override
    public void Dismis() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            pauseMedia();
            showNotification(R.drawable.ic_baseline_play_arrow_black_24,false);
        }
        audioService=false;
        if (mediaPlayer != null ) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

    @Override
    public void Pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            //ext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_play_arrow_24,true);
            }
            removeAudioFocus();
            //  musicPlayerIsPlaing=false;

        }
    }

    @Override
    public void Play() {
       playMedia();
    }

    public class MyBinder extends Binder{
        public AudioPlaySystem getServices(){
            return AudioPlaySystem.this;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
        //Invoked when playback of a media source has completed.
       // stopMedia();
        ditectPlayOption();
        //stop the service
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation.
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        //Invoked when the media source is ready for playback.
        Play();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        //Invoked when the audio focus of the system is updated.
        //Invoked when the audio focus of the system is updated.

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                else if (!mediaPlayer.isPlaying()){
                     mediaPlayer.start();
                }
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
//                mediaPlayer.release();
//                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }

    }
    //=====================Audio Fucas==============
    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
          //  if(musicPlayerIsPlaing)
            mediaPlayer.start();
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }
//============
    public class LocalBinder extends Binder {
        public AudioPlaySystem getService() {
            return AudioPlaySystem.this;
        }
    }



//=======================================
    private  void initMediaPlayer() {
      //  mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(String.valueOf(AudioPlaySystem.getInstance().SongUri));
            mediaPlayer.prepare();
            mediaPlayer.seekTo(songProgress);
            mediaPlayer.start();
            audioService=true;
         //   musicPlayerIsPlaing=true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_pause_24,true);
            }
//            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MediaButtonEventReceiver buttonReceiver = new MediaButtonEventReceiver();
        IntentFilter iF = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        iF.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        iF.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(buttonReceiver, iF);


//        registerBecomingNoisyReceiver();
        callStateListener();
        insertDAtaDBM();

    }







    //==============================================

    public  void playMedia( ) {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            //ext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_pause_24,true);
            }
            requestAudioFocus();
        }
    }

    public  void stopMedia() {

        if (mBinder != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (mediaPlayer == null) return;
    }

    public  void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            //ext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_play_arrow_24,true);
            }
            removeAudioFocus();
          //  musicPlayerIsPlaing=false;
        }
    }

    public  void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();

            //ext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(R.drawable.ic_baseline_pause_24,true);
            }
            requestAudioFocus();
        }
    }
    static int resumePosition;
//===========================================

//The system calls this method when an activity, requests the service be started
    private static AudioPlaySystem instance;

    public static AudioPlaySystem getInstance() {
        return instance;
    }
    private   IBinder mBinder=new MyBinder();
    public static final String ACTION_PLAY_PAUSE="PLAY_PAUSE";
    public static final String ACTION_NEXT="NEXT";
    public static final String ACTION_DISMIS="DISMIS";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PAUSE="PAUSE";
    public static final String ACTION_PLAY="PLAY";

    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSession;
    public static DBManager_History dbManager;
    public static String RecordID;
    SaveData saveData;
    private AudiouPlayViewModel audiouPlayViewModel;



    @Override
public int onStartCommand(Intent intent, int flags, int startId) {
             instance=this;
            context=this;/**...**/
        dbManager = new DBManager_History(context);
        saveData=new SaveData(context);

        //audiouPlayViewModel= new ViewModelProvider(getApplication()).get(AudiouPlayViewModel.class);

       // setCallBack(this);
       // Home.getInstance().AudioFolder();
        mediaSession=new MediaSessionCompat(this,"Player Aidio");
        String actionName=intent.getStringExtra("myActionName");
        if(actionName!=null) {
            switch (actionName) {
                case ACTION_PLAY_PAUSE:
                    if(actionPlaying !=null){
                        actionPlaying.PlayPause();
                    }
                    break;
                case ACTION_PLAY:
                    if(actionPlaying !=null){
                        actionPlaying.Play();
                    }
                    break;
                case ACTION_PAUSE:
                    if(actionPlaying !=null){
                        actionPlaying.Pause();
                    }

                    break;
                case ACTION_NEXT:
                    if(actionPlaying !=null){
                        actionPlaying.nextClick();
                        Toast.makeText(getApplicationContext(),"next",Toast.LENGTH_SHORT).show();
                        Log.d("msg","name=Click= "+actionName);
                    }
                    break;
                case ACTION_PREV:
                    if(actionPlaying !=null){
                        actionPlaying.previousClick();
                    }
                    break;
                case ACTION_DISMIS:
                    if(actionPlaying !=null){
                        actionPlaying.Dismis();
                    }
                    break;
            }
        }



        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.isWiredHeadsetOn();





        return START_STICKY;


 //   return  super.onStartCommand(intent, flags, startId);


    // startForeground(1,notification);  //this is hiden or auto service starting when coment out this line

}



        public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying=actionPlaying;
    }


String TAG;
//================================= Build Nptification

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void showNotification(int playPauseButton,boolean action){

        Intent intent=new Intent(this,Home.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);

        Intent prevInt=new Intent(this, NotificationRecever.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent= PendingIntent.getBroadcast(this,0,prevInt,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playInt=new Intent(this,NotificationRecever.class).setAction(ACTION_PLAY_PAUSE);
        PendingIntent playPendingIntent= PendingIntent.getBroadcast(this,0,playInt,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextInt=new Intent(this,NotificationRecever.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent= PendingIntent.getBroadcast(this,0,nextInt,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismisInt=new Intent(this,NotificationRecever.class).setAction(ACTION_DISMIS);
        PendingIntent dismisPendingIntent= PendingIntent.getBroadcast(this,0,dismisInt,PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap picture = null;
        String  recpintList=SongUri;
        String extension = recpintList.substring(recpintList.lastIndexOf("."));
        if(extension.equals(".mp4")){
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(SongUri);
            Bitmap extractedImage = media.getFrameAtTime();
            picture=extractedImage;

        }else{
            Uri imageUri = Uri.parse(AlbumCover);
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        // Bitmap =BitmapFactory.decodeResource();

        Notification notification=new NotificationCompat.Builder(this,AppNotify.CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(picture)
                .setContentTitle(SongName)
                .setContentText(SongAlbam)
                .addAction(R.drawable.ic_baseline_skip_previous_24,"Previous",prevPendingIntent)
                .addAction(playPauseButton,"Play",playPendingIntent)
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next",nextPendingIntent)
                .addAction(R.drawable.ic_baseline_clear_24,"DISMIS",dismisPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        startForeground(1, notification);
//        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0,notification);




        if(action){ }else{ stopForeground(true);}
    }
//===================BroadCust Recever =====================
//Becoming noisy
private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        //pause audio on ACTION_AUDIO_BECOMING_NOISY
        Pause();
       // buildNotification(PlaybackStatus.PAUSED);

        Toast.makeText(context, "inside reciver", Toast.LENGTH_SHORT).show();

        final KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event.getAction() != KeyEvent.ACTION_DOWN) return;



        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_STOP:
                // stop music
                break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                // pause music
                Pause();
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                // next track
                nextClick();
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                // previous track
                previousClick();
                break;
        }
    }
};



    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
        Toast.makeText(context, "inside", Toast.LENGTH_SHORT).show();
    }


    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            Pause();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

    }

    //  Video Part play==============
    public ArrayList<VideoModel> VideoQueList=new ArrayList<VideoModel>();
    public int videoFromPlayList=0;
    public String PlayListName;
    //room db
    public int db_item_id;

    //=============================== Play ==============================
    public     String AlbumCover="Unknown";
    public     String SongUri="Unknown";
    public     String SongName="Unknown";
    public     String SongDuration="00";
    public     String SongAlbam="Unknown";
    public     String SongArtist="Unknown";
    public     String SongFolder="Unknown";
    public int songProgress=0;

  public List<AudiouModel> queueList=new ArrayList<>();
    public void AudioPlay(){
       // stopMedia();

        //Request audio focus
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }

        if (SongUri != null && SongUri != "")
            initMediaPlayer();

        songPosition();
    }
   public int songCurentPosition=0;
    //update SongPosition
    void songPosition(){
        if(queueList!=null)
        for (int i=0; i<queueList.size();i++){
            if(SongName.equals(queueList.get(i).getSong_name())){
                songCurentPosition=i;
                break;
            }
        }
    }
    public void audioPlayNext(){
        songProgress=0;
        if(songCurentPosition<queueList.size()-1) {
            songCurentPosition++;
            AudioPlaySystem.getInstance().SongUri = queueList.get(songCurentPosition).getPath();
            AudioPlaySystem.getInstance().AlbumCover = queueList.get(songCurentPosition).getAlbumArtUriImage();
            AudioPlaySystem.getInstance().SongName = queueList.get(songCurentPosition).getSong_name();
            AudioPlaySystem.getInstance().SongAlbam = queueList.get(songCurentPosition).getAlbam_name();
            AudioPlaySystem.getInstance().SongDuration =  queueList.get(songCurentPosition).getHour() + ":" +queueList.get(songCurentPosition).getMunite() + ":" +
                    queueList.get(songCurentPosition).getSecond();
           AudioPlay();
           AudioPlay.getInstance().audioPlay();
        }
    }
    public void audioPlayPrevious(){
        songProgress=0;
        if(songCurentPosition>0) {
            songCurentPosition--;
            AudioPlaySystem.getInstance().SongUri = queueList.get(songCurentPosition).getPath();
            AudioPlaySystem.getInstance().AlbumCover = queueList.get(songCurentPosition).getAlbumArtUriImage();
            AudioPlaySystem.getInstance().SongName = queueList.get(songCurentPosition).getSong_name();
            AudioPlaySystem.getInstance().SongAlbam = queueList.get(songCurentPosition).getAlbam_name();
            AudioPlaySystem.getInstance().SongDuration =  queueList.get(songCurentPosition).getHour() + ":" +queueList.get(songCurentPosition).getMunite() + ":" +
                    queueList.get(songCurentPosition).getSecond();
            AudioPlay();
            AudioPlay.getInstance().audioPlay();
        }
    }

    /** play continue suffle repet ====== **/
    public void ditectPlayOption(){
        playRepetAll();
    }


    public void playRepetAll() {
        songProgress=0;
        if (saveData.LoadData() == 0) {
            if (songCurentPosition < queueList.size() - 1) {
                songCurentPosition++;
                AudioPlaySystem.getInstance().SongUri = queueList.get(songCurentPosition).getPath();
                AudioPlaySystem.getInstance().AlbumCover = queueList.get(songCurentPosition).getAlbumArtUriImage();
                AudioPlaySystem.getInstance().SongName = queueList.get(songCurentPosition).getSong_name();
                AudioPlaySystem.getInstance().SongAlbam = queueList.get(songCurentPosition).getAlbam_name();
                AudioPlaySystem.getInstance().SongDuration = queueList.get(songCurentPosition).getHour() + ":" +queueList.get(songCurentPosition).getMunite() + ":" +
                        queueList.get(songCurentPosition).getSecond();
                AudioPlay();
                AudioPlay.getInstance().audioPlay();
            } else {
                songCurentPosition = 0;
                AudioPlaySystem.getInstance().SongUri = queueList.get(songCurentPosition).getPath();
                AudioPlaySystem.getInstance().AlbumCover = queueList.get(songCurentPosition).getAlbumArtUriImage();
                AudioPlaySystem.getInstance().SongName = queueList.get(songCurentPosition).getSong_name();
                AudioPlaySystem.getInstance().SongAlbam = queueList.get(songCurentPosition).getAlbam_name();
                AudioPlaySystem.getInstance().SongDuration =  queueList.get(songCurentPosition).getHour() + ":" +queueList.get(songCurentPosition).getMunite() + ":" +
                        queueList.get(songCurentPosition).getSecond();
                AudioPlay();
                AudioPlay.getInstance().audioPlay();
            }
        }else if(saveData.LoadData() == 1){
            AudioPlay();
            AudioPlay.getInstance().audioPlay();
        }else if(saveData.LoadData() == 2){
            final int min = 0;
            final int max = queueList.size();
            final int random = new Random().nextInt((max - min) + 1) + min;
            AudioPlaySystem.getInstance().SongUri = queueList.get(random).getPath();
            AudioPlaySystem.getInstance().AlbumCover = queueList.get(random).getAlbumArtUriImage();
            AudioPlaySystem.getInstance().SongName = queueList.get(random).getSong_name();
            AudioPlaySystem.getInstance().SongAlbam = queueList.get(random).getAlbam_name();
            AudioPlaySystem.getInstance().SongDuration =  queueList.get(random).getHour() + ":" +queueList.get(random).getMunite() + ":" +
                    queueList.get(random).getSecond();
            AudioPlay();
            AudioPlay.getInstance().audioPlay();

        }else if(saveData.LoadData() == 3){
            if (songCurentPosition < queueList.size() - 1) {
                songCurentPosition++;
                AudioPlaySystem.getInstance().SongUri = queueList.get(songCurentPosition).getPath();
                AudioPlaySystem.getInstance().AlbumCover = queueList.get(songCurentPosition).getAlbumArtUriImage();
                AudioPlaySystem.getInstance().SongName = queueList.get(songCurentPosition).getSong_name();
                AudioPlaySystem.getInstance().SongAlbam = queueList.get(songCurentPosition).getAlbam_name();
                AudioPlaySystem.getInstance().SongDuration =  queueList.get(songCurentPosition).getHour() + ":" +queueList.get(songCurentPosition).getMunite() + ":" +
                        queueList.get(songCurentPosition).getSecond();
                AudioPlay();
//                AudioPlay.getInstance().audioPlay();
                Play();
            } else {
                Dismis();
            }
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            Dismis();
            mediaPlayer.release();
        }
        //removeAudioFocus();
    }







    /** -===================== **/
    //================================== insert Data in DBM==================================
    void insertDAtaDBM(){
//        ContentValues values = new ContentValues();
//        values.put(DBManager_History.song_name, audioSongName);
//        values.put(DBManager_History.song_folder, audioSongFolder);
//        values.put(DBManager_History.song_album, audioSongAlbam);
//        values.put(DBManager_History.song_album_cover, audioAlbumCover);
//        values.put(DBManager_History.song_artist, audioSongArtist);
//        values.put(DBManager_History.song_duration, audioSongDuration);
//        values.put(DBManager_History.song_path, audioSongUri);
//
//        long id = dbManager.Insert(values);
//        if (id > 0) {
//            Toast.makeText(context, "Data Inserted", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(context, "Error Please clear app data", Toast.LENGTH_LONG).show();
//        }
        History dataModelHistiory=new History(SongName,SongAlbam,SongFolder,SongArtist,AlbumCover,SongUri,SongDuration,"0");
        AudiouPlayViewModel audiouPlayViewModel=new AudiouPlayViewModel(getApplication());
        audiouPlayViewModel.insertHistory(dataModelHistiory);
    }

    //======================== eidit ===============
//    public void LoadeArray() {
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task", null);
//        Type type = new TypeToken<ArrayList<customItem>>() {
//        }.getType();
//        currentList = gson.fromJson(json, type);
//    }
//    public void Play_pause_Media(){
//        pauseMedia();
////        if(!mediaPlayer.isPlaying()){
////            playMedia();
////        }else{
////            pauseMedia();
////        }
//    }

}
