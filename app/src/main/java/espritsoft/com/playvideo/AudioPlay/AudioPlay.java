package espritsoft.com.playvideo.AudioPlay;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.ActionPlaying;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.MediaButtonEventReceiver;
import espritsoft.com.playvideo.AudioPlay.ViewModel.AudiouPlayViewModel;
import espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList.PlayListFragment;
import espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.AudioBottomSheetDialog_Fragment;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.HistoryLibrary.historyItemModul;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;

import static android.content.Context.BIND_AUTO_CREATE;

import com.bumptech.glide.Glide;

public class AudioPlay extends Fragment implements ActionPlaying, ServiceConnection {


    String audioIsPlaying="playing";
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("audoOp",Home.getInstance().audiouOpen);
        outState.putString("audioIsPlaying",audioIsPlaying);
    }

    View view;
    public static DBManager_History dbManager;
    public static String RecordID;
    private static AudioPlay instance;
    SaveData saveData;
    public static AudioPlay getInstance() {
        return instance;
    }


    AudiouPlayViewModel audiouPlayViewModel;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.fragment_audio_play,container,false);
       instance=this;
        dbManager = new DBManager_History(getContext());
        saveData=new SaveData(getContext());

        initializeLayout();
//        if(savedInstanceState !=null){
//         String  audiouOpen=savedInstanceState.getString("audoOp");
//            if(audiouOpen.equals("Expended")){
//                isExpandAudiouView(true);
//            }
//           else if( audiouOpen.equals("down")) {
//                isExpandAudiouView(false);
//            }
//           if(savedInstanceState.getString("audioIsPlaying").equals("playing")){
//               if(AudioPlaySystem.getInstance().mediaPlayer!=null){
//                   new Handler().postDelayed(new Runnable() {
//                       @Override
//                       public void run() {
//                           AudioPlaySystem.getInstance().AudioPlay();
//                       }
//                   },400);
//
//                   Toast.makeText(getContext(),"not null",Toast.LENGTH_SHORT).show();
//               }else{
//                   AudioPlaySystem.getInstance().AudioPlay();
//                   Toast.makeText(getContext(),"null",Toast.LENGTH_SHORT).show();
//               }
//
//           }else if(!savedInstanceState.getString("audioIsPlaying").equals("playing")){
//               AudioPlaySystem.getInstance().pauseMedia();
//               audioIsPlaying="pause";
//           }
//        }else{
//
//        }


        audiouPlayViewModel= ViewModelProviders.of(getActivity()).get(AudiouPlayViewModel.class);



       return view;
    }
    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsminiView,paramsmSRootView;
    ConstraintLayout musicContainer,miniViewMusic,musicRootContainer;
    Guideline guidelineMiniView,guidelineSRootView;
    SeekBar musicSeekBar;
    Button videoShow;
    ImageView miniVuewMusicCover,musicCover;
    TextView titleMusic,miniTitleMusic,albamMusic,albamMiniMusic,audioDuration,audioDurationRunning;
    ImageButton btnMusicDown, btnMiniPlaypauseMusic,nextMusic,previousMusic,playPausMusic,playOptionMusic,likeMusic,saveMusicPlayList,listMusic,miniviewDismis;
    ImageButton musicViewMore;
    private void initializeLayout() {

        audioDuration=view.findViewById(R.id.durationMusic);
        audioDurationRunning=view.findViewById(R.id.durationMusicRning);

        videoShow=view.findViewById(R.id.videoShow);
        checkVideOrAudio();

        miniTitleMusic=view.findViewById(R.id.miniTitleMusic);
        titleMusic=view.findViewById(R.id.musicTitle);
        titleMusic.setSelected(true);  // Set focus to the textview


        albamMiniMusic=view.findViewById(R.id.miniAlbumeMusic);

        miniVuewMusicCover=view.findViewById(R.id.miniImageViewMusic);
        musicCover=view.findViewById(R.id.musicImage);

        musicRootContainer=view.findViewById(R.id.musicRootContainer);

        btnMiniPlaypauseMusic=view.findViewById(R.id.miniPlayPauseMusic);
        miniviewDismis=view.findViewById(R.id.miniViewDismisMusic);
        miniviewDismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   AudioPlaySystem.getInstance().pauseMedia();
                    Home.getInstance().audiouOpen="ideal";
                }catch (Exception e){
                    e.printStackTrace();
                }
                Home.getInstance().videoPlayIsActive="ideal";
                isExpandAudiouView(true);
                musicRootContainer.setVisibility(View.INVISIBLE);

                Dismis();
            }
        });

        btnMusicDown=view.findViewById(R.id.misuicViewDown);
        btnMusicDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audiouPlayViewModel.btnVideoViewMiniFram(getActivity());
                isExpandAudiouView(false);

            }
        });

        // bottom sheet dialog implement
        listMusic=view.findViewById(R.id.listMusic);
        listMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogSongQueList();
            }
        });

        musicSeekBar=view.findViewById(R.id.musicSeekbar);


        guidelineMiniView=view.findViewById(R.id.miniViewMarzin);
        paramsminiView = (ConstraintLayout.LayoutParams) guidelineMiniView.getLayoutParams();
        guidelineSRootView=view.findViewById(R.id.mainMusicMarzin);
        paramsmSRootView = (ConstraintLayout.LayoutParams) guidelineSRootView.getLayoutParams();

        musicContainer=view.findViewById(R.id.mainMusic);
        miniViewMusic=view.findViewById(R.id.miniView);

        playPausMusic=view.findViewById(R.id.playPauseAudio);
        playPausMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PlayPause();

            }
        });

       view.findViewById(R.id.musicViewMore).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String time=AudioPlaySystem.getInstance().SongDuration;
               String[] s=time.split(":");
               String ss=s[2];
               //  String hh="00";
               String mm=s[1];
               String hh=s[0];
               AudiouModel audiouModel=new AudiouModel(
                       AudioPlaySystem.getInstance().SongUri,
                       AudioPlaySystem.getInstance().SongName,
                       AudioPlaySystem.getInstance().SongAlbam,
                       AudioPlaySystem.getInstance().SongArtist,
                       AudioPlaySystem.getInstance().AlbumCover,
                       ss,
                       mm,
                       hh,
                       AudioPlaySystem.getInstance().SongFolder

               );
               audiouPlayViewModel.SongMenuMore(audiouModel,getContext(),getActivity(),v,getFragmentManager());
           }
       });

        btnMiniPlaypauseMusic=view.findViewById(R.id.miniPlayPauseMusic);
        btnMiniPlaypauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PlayPause();
            }
        });
        nextMusic=view.findViewById(R.id.musiNext);
        nextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               nextClick();
            }
        });
        previousMusic=view.findViewById(R.id.musicPrevious);
        previousMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               previousClick();
            }
        });


        likeMusic=view.findViewById(R.id.favorite_audiou);
        likeMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              songAddRemovePlayList();
            }
        });
        saveMusicPlayList=view.findViewById(R.id.savePlayList);
        saveMusicPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogPlayList();
            }
        });


        playOptionMusic=view.findViewById(R.id.playOptionAudiou);
        playOptionMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (saveData.LoadData()){
                    case 0:
                        saveData.SaveData(1);
                        break;
                    case 1:
                        saveData.SaveData(2);
                        break;
                    case 2:
                        saveData.SaveData(3);
                        break;
                    case 3:
                        saveData.SaveData(0);
                        break;
                }
                checkPlayOption();
            }
        });

        videoShow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                audiouPlayViewModel.ShowVideo();
            }
        });



        setIconPlayList();
        audioPlay();
        checkPlayOption();
    }

    private void checkVideOrAudio() {
        String pth=AudioPlaySystem.getInstance().SongUri;
        String ex=pth.substring(pth.indexOf("."));
        if(!ex.equals(".mp3"))videoShow.setVisibility(View.VISIBLE);else videoShow.setVisibility(View.INVISIBLE);
    }

    private void checkPlayOption() {
        if(saveData.LoadData()==0){
            playOptionMusic.setImageResource(R.drawable.ic_baseline_repeat_24);
        }else if(saveData.LoadData()==1){
            playOptionMusic.setImageResource(R.drawable.ic_baseline_repeat_one_24);
        }else if(saveData.LoadData()==2){
            playOptionMusic.setImageResource(R.drawable.ic_baseline_shuffle_24);
        }else if(saveData.LoadData()==3){
            playOptionMusic.setImageResource(R.drawable.ic_right_arrow_svg);
        }
    }


    String TAG;
    public void isExpandAudiouView(boolean isExpanded) {
        if (isExpanded) {
            paramsminiView.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);

            paramsmSRootView.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineSRootView.setLayoutParams(paramsmSRootView);

            constraintSet.applyTo(musicRootContainer);

        } else {
            paramsminiView.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);

            paramsmSRootView.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineSRootView.setLayoutParams(paramsmSRootView);

            constraintSet.applyTo(musicRootContainer);

        }
    }

//    public void ShowMiniViewMusic(){
//        paramsminiView.guidePercent = 0F; // 45% // range: 0 <-> 1
//        guidelineMiniView.setLayoutParams(paramsminiView);
//
//        paramsmSRootView.guidePercent = 1F; // 45% // range: 0 <-> 1
//        guidelineSRootView.setLayoutParams(paramsmSRootView);
//
//        constraintSet.applyTo(musicRootContainer);
//        constraintSet.applyTo(miniViewMusic);
//    }

    public void HideShowAudioView(float v){

        float vV= v - 0.5f;
        paramsminiView.guidePercent = vV; // 45% // range: 0 <-> 1
        guidelineMiniView.setLayoutParams(paramsminiView);

        paramsmSRootView.guidePercent = vV; // 45% // range: 0 <-> 1
        guidelineSRootView.setLayoutParams(paramsmSRootView);

        constraintSet.applyTo(musicRootContainer);
    }


    //=========================== music play===========
   public Handler handler;
    public void audioPlay() {

      //  mediaPlayer = new MediaPlayer();

      //  AudioPlaySystem.getInstance().AudioPlay();
        //if running stop it
        playPausMusic.setImageResource(R.drawable.ic_baseline_pause_black_44);
        btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_pause_black_24);
        handler=new Handler();
        updateSeekBar();
//        try {
//            mediaPlayer.setDataSource(String.valueOf(AudioPlaySystem.getInstance().audioSongUri));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();



        //setDetails
        String  recpintList=AudioPlaySystem.getInstance().SongUri;
        String extension = recpintList.substring(recpintList.lastIndexOf("."));
        if(extension.equals(".mp4")){
            Glide.with(getContext()).load("file://"+AudioPlaySystem.getInstance().AlbumCover)
                    .skipMemoryCache(false)
                    .into(musicCover);
            Glide.with(getContext()).load("file://"+AudioPlaySystem.getInstance().AlbumCover)
                    .skipMemoryCache(false)
                    .into(miniVuewMusicCover);
        }else{

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContext().getContentResolver(), Uri.parse(AudioPlaySystem.getInstance().AlbumCover));
                musicCover.setImageBitmap(bitmap);
                miniVuewMusicCover.setImageBitmap(bitmap);

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.music);
                musicCover.setImageBitmap(bitmap);
                miniVuewMusicCover.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        titleMusic.setText(String.valueOf(AudioPlaySystem.getInstance().SongName));
        miniTitleMusic.setText(AudioPlaySystem.getInstance().SongName);
        albamMiniMusic.setText(AudioPlaySystem.getInstance().SongAlbam);
        audioDuration.setText(AudioPlaySystem.getInstance().SongDuration);
        checkVideOrAudio();
    }
    //=========================== seekBar
    //=========================== SeekBar==========

    public void updateSeekBar() {

        handler.postDelayed(UpdateTimeTask,1000);

    }
    public Runnable UpdateTimeTask=new Runnable() {
        @Override
        public void run() {
            if (AudioPlaySystem.getInstance().mediaPlayer !=null) {
                musicSeekBar.setProgress(AudioPlaySystem.getInstance().mediaPlayer.getCurrentPosition());
                musicSeekBar.setMax(AudioPlaySystem.getInstance().mediaPlayer.getDuration());
                handler.postDelayed(this, 100);

                musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        handler.removeCallbacks(UpdateTimeTask);
                        AudioPlaySystem.getInstance().songProgress=progress;

                        /**
                         * video time collect
                         */
                        int seconds = (int) (progress / 1000) % 60 ;
                        int minutes = (int) ((progress / (1000*60)) % 60);
                        int hours=(int) ((progress / (1000*60*60)) % 24);
                        String second = String.valueOf(seconds);
                        String minute = String.valueOf(minutes);
                        String hour =String.valueOf(hours);
                        audioDurationRunning.setText(String.valueOf(hours+":"+minutes+":"+seconds));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        handler.removeCallbacks(UpdateTimeTask);
                        AudioPlaySystem.getInstance().mediaPlayer.seekTo(seekBar.getProgress());
                        updateSeekBar();
                    }
                });
            }
        }
    };

    // bottom Sheet dailog processs ====

    void bottomSheetDialogSongQueList(){
      //  View bottomView=getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog,null);
       // LinearLayout call=bottomView.findViewById(R.id.layout)

        AudioBottomSheetDialog_Fragment bottomSheetDialog_fragment=new AudioBottomSheetDialog_Fragment();
        bottomSheetDialog_fragment.show(getFragmentManager(),null);
    }

    void bottomSheetDialogPlayList(){
        //  View bottomView=getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog,null);
        // LinearLayout call=bottomView.findViewById(R.id.layout)

        PlayListDataForSave.PName=AudioPlaySystem.getInstance().SongName;
        PlayListDataForSave.PCover=AudioPlaySystem.getInstance().AlbumCover;
        PlayListDataForSave.PAlbum=AudioPlaySystem.getInstance().SongAlbam;
        PlayListDataForSave.PFolder=AudioPlaySystem.getInstance().SongFolder;
        PlayListDataForSave.PDuration=AudioPlaySystem.getInstance().SongDuration;
        PlayListDataForSave.PArtist=AudioPlaySystem.getInstance().SongArtist;
        PlayListDataForSave.PSongPath=AudioPlaySystem.getInstance().SongUri;


        PlayListFragment playListFragment=new PlayListFragment();
        playListFragment.show(getFragmentManager(),null);
    }


    @Override
    public void nextClick() {
        AudioPlaySystem.getInstance().nextClick();
        setIconPlayList();
    }

    @Override
    public void previousClick() {
        AudioPlaySystem.getInstance().previousClick();
        setIconPlayList();
    }

    @Override
    public void PlayPause() {
        if(AudioPlaySystem.getInstance().mediaPlayer.isPlaying()){
            AudioPlaySystem.getInstance().pauseMedia();
            playPausMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_24);
            audioIsPlaying="pause";
        }else{
            AudioPlaySystem.getInstance().playMedia();
            playPausMusic.setImageResource(R.drawable.ic_baseline_pause_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_pause_black_24);
            audioIsPlaying="playing";
        }

    }

    @Override
    public void Dismis() {
        AudioPlaySystem.getInstance().Dismis();
        audioIsPlaying="pause";
    }

    @Override
    public void Pause() {
        if(AudioPlaySystem.getInstance().mediaPlayer.isPlaying()) {
            AudioPlaySystem.getInstance().pauseMedia();
            playPausMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_24);
            audioIsPlaying = "pause";
        }
    }

    @Override
    public void Play() {
        if(!AudioPlaySystem.getInstance().mediaPlayer.isPlaying()){
            AudioPlaySystem.getInstance().playMedia();
            playPausMusic.setImageResource(R.drawable.ic_baseline_pause_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_pause_black_24);
            audioIsPlaying="playing";
        }
    }

    AudioPlaySystem audioPlaySystem;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        AudioPlaySystem.MyBinder binder=(AudioPlaySystem.MyBinder) service;
        audioPlaySystem = binder.getServices();
        audioPlaySystem.setCallBack(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        audioPlaySystem=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent=new Intent(getContext(),AudioPlaySystem.class);
        getContext().bindService(intent,this,BIND_AUTO_CREATE);

    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(AudioPlaySystem.getInstance().mediaPlayer!=null)
        if(AudioPlaySystem.getInstance().mediaPlayer.isPlaying()){
            playPausMusic.setImageResource(R.drawable.ic_baseline_pause_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_pause_black_24);
        }else{

            playPausMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_44);
            btnMiniPlaypauseMusic.setImageResource(R.drawable.ic_baseline_play_arrow_black_24);
        }
    }



    /** DBM  work procidure ===================================== **/

    void insertPlayListSongAtDBM(String playListName){
        ContentValues values = new ContentValues();
        values.put(DBManager_History.song_name, AudioPlaySystem.getInstance().SongName);
        values.put(DBManager_History.song_folder, AudioPlaySystem.getInstance().SongFolder);
        values.put(DBManager_History.song_album, AudioPlaySystem.getInstance().SongAlbam);
        values.put(DBManager_History.song_album_cover, AudioPlaySystem.getInstance().AlbumCover);
        values.put(DBManager_History.song_artist, AudioPlaySystem.getInstance().SongArtist);
        values.put(DBManager_History.song_duration, AudioPlaySystem.getInstance().SongDuration);
        values.put(DBManager_History.song_path, AudioPlaySystem.getInstance().SongUri);
        values.put(DBManager_History.playl_list_name,playListName);

        long id = dbManager.Insert_table2(values);
        if (id > 0) {
            Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_LONG).show();
            setIconPlayList();
        } else {
            Toast.makeText(getContext(), "Error Please clear app data", Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<historyItemModul> historyList=new ArrayList();
    public  void checkPlayList() {


        //sarch
        String[] SelectionArgs = {"%" + AudioPlaySystem.getInstance().SongName + "%"};



        //clear after load
      //  historyList.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {
            /*    tableData+=cursor.getString(cursor.getColumnIndex(DBManager.task_name))+","+
                       cursor.getString(cursor.getColumnIndex(DBManager.task_desc))+"::";
*/

                if (AudioPlaySystem.getInstance().SongName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))) {
                    String[] SelectionArgss = new String[]{AudioPlaySystem.getInstance().SongName};
                    int count = dbManager.Delet2("song_name=?", SelectionArgss);
                    //refresh Element
                    if (count > 0) {
                        //  checkPlayList();
                        setIconPlayList();
                    }
                } else{
                  //  insertFavoriteSongAtDBM();
                    setIconPlayList();

                }

                //adaptor
//                historyList.add(new historyItemModul(
//                        cursor.getString(cursor.getColumnIndex(DBManager_History.song_name))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_folder))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_path))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_artist))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_duration))
//                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album_cover))
//
//                ));

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }

    }

    boolean plaListSongadd=false;
    public  void setIconPlayList() {
        //sarch
        String[] SelectionArgs = {"%" +AudioPlaySystem.getInstance().SongName+ "%"};



        //clear after load
        //  historyList.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {

                if (AudioPlaySystem.getInstance().SongName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                && cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals("Favourite Audio")) {
                    likeMusic.setImageResource(R.drawable.ic_baseline_favorite_24);
                    RecordID=cursor.getString(cursor.getColumnIndex(DBManager_History.ColID));
                    plaListSongadd=true;
                    break;
                }else if (AudioPlaySystem.getInstance().SongName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                        && !cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals("Favourite Audio")) {
                    likeMusic.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    plaListSongadd=false;

                }
//                else{
//                    likeMusic.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                }

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }else{
            likeMusic.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            plaListSongadd=false;
        }

    }

    void songAddRemovePlayList(){
        if(plaListSongadd){
            String[] SelectionArgss = new String[]{RecordID};
            int count = dbManager.Delet2("ID=? ", SelectionArgss);
            //refresh Element
            if (count > 0) {
                setIconPlayList();
            }

        }else{
            insertPlayListSongAtDBM("Favourite Audio");
            setIconPlayList();
        }
    }




    /**
     *
     * Head Set , Airbud , Micro phone configuration
     *
     *
     */
    //Media button listner head set
    private AudioManager mAudioManager;
    private ComponentName mRemoteControlResponder;
    BroadcastReceiver broadcastReceiver,broadcastReceiver2;
    public void HeadSetConfiguration(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                int iii;
                if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                    iii = intent.getIntExtra("state", -1);
                    if (iii == 0) {
                        Toast.makeText(getContext(), "microphone not plugged in", Toast.LENGTH_LONG).show();
                    }
                    if (iii == 1) {
                        Toast.makeText(getContext(), "microphone plugged in",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }

        };
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        getContext().registerReceiver(broadcastReceiver, receiverFilter);


        //Media button listner head set
        mAudioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        mRemoteControlResponder = new ComponentName(getContext().getPackageName(),
                MediaButtonEventReceiver.class.getName());

    }


}
