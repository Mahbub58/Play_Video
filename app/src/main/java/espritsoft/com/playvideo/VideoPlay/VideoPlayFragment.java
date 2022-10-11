package espritsoft.com.playvideo.VideoPlay;

import static android.content.Context.WINDOW_SERVICE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.DBManager.RepositoryHistoryDB.RoomDbHistoryRepository;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.MainActivity;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;
import espritsoft.com.playvideo.Video.VideoFragment;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsFragment;
import espritsoft.com.playvideo.VideoPlay.ViewModel.VideoPlayViewModel;
import espritsoft.com.playvideo.VideoPlayActivity.VideoPlayActivity;

public class VideoPlayFragment extends Fragment implements PlayAction {


    public VideoPlayFragment() {
        // Required empty public constructor
    }


    String videoIsPlaying="playing";
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("videoOption", Home.getInstance().videoPlayIsActive);
        outState.putString("videoIsPlaying",videoIsPlaying);



        //outState.putSerializable(STATE_ITEMS, ((Home) getActivity()).VideoList);
    }

   public static VideoPlayFragment instance;
    public static   VideoPlayFragment getInstance(){
        return instance;
    }
    View view;
    public static DBManager_History dbManager;
    public static String RecordID;
    SaveData saveData;
    VideoPlayViewModel videoPlayViewModel;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_play, container, false);
        instance=this;
        dbManager = new DBManager_History(getContext());
        saveData=new SaveData(getContext());

        iniTializeLayout();

        videoPlayViewModel= ViewModelProviders.of(getActivity()).get(VideoPlayViewModel.class);

        //restore session acivity recreate
//        if (savedInstanceState != null) {
//            Home.getInstance().videoPlayIsActive=savedInstanceState.getInt("videoOption");
//            if(Home.getInstance().videoPlayIsActive==1 )
//                initilaizVideoPlay();
//
//        } else {
//           // ((Home) getActivity()).VideoList = new ArrayList<>();
//             initilaizVideoPlayNull();
//            Home.getInstance().videoProgress=0;
//            insertDAtaDBM();
//        }


        if (savedInstanceState == null) {
            init_videoPlay();
        }else {
           if(!Home.getInstance().videoPlayIsActive.equals("ideal"))
            if(savedInstanceState.getString("videoIsPlaying").equals("playing")){
                init_videoPlay();
                videoView.start();

            }else if(!savedInstanceState.getString("videoIsPlaying").equals("playing")){
                init_videoPlay();
                videoView.pause();
                videoIsPlaying="pause";
                Toast.makeText(getContext(),"="+savedInstanceState.getString("videoIsPlaying"),Toast.LENGTH_SHORT).show();
            }

            if(Home.getInstance().forcelyActivetedLandspeceMood){

            }else{
                videoBackHide();
            }

        }



        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void init_videoPlay() {
        videoDuretion=view.findViewById(R.id.duration);
        videoDuretion.setText(AudioPlaySystem.getInstance().SongDuration);
        videoDuration_running=view.findViewById(R.id.duration_running);


        videoPlayViewModel.initilaizVideoPlay(videoView,0,false);

        videoView.start();
        videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);

        handler=new Handler();
        updateSeekBar();

//        view.findViewById(R.id.skeep).setOnTouchListener(new View.OnTouchListener() {
//            private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onDoubleTap(MotionEvent e) {
//                    Log.d("TEST", "onDoubleTap======================");
//                    return super.onDoubleTap(e);
//                }
//            });
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("TEST====", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
//                gestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onResume() {
        super.onResume();
        videoPlayViewModel.initilaizVideoPlay(videoView,0,false);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               checkplaypauseVideoBtn();
           }
       },800);
    }

    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsRight;
    ConstraintLayout.LayoutParams paramsLeft;
    ConstraintLayout.LayoutParams paramsTop;
    ConstraintLayout.LayoutParams paramsBottom;

    public VideoView videoView;ImageButton videoPausePlayBtn;
    SeekBar seekBar;Handler handler;ConstraintLayout videoBack;
    Guideline guidLine_btnView;String TAG; boolean backMove=false;
    ImageButton btnVideoNext,btnVideoPrevious,btnVideoDown,btnVideoMore,btnVideoView,btnVideoMiniView;
    TextView videoDuretion,videoSongName,videoDuration_running;
    ConstraintLayout viBack,rootVideoContiner;
    Guideline guidelineBootom,guidelineTop,guidelineRight,guidelineLeft;
    ImageButton skep10sec,previous10sec;
    private void iniTializeLayout() {
         videoView=view.findViewById(R.id.videoView);


         videoSongName=view.findViewById(R.id.songName);
         videoSongName.setText(AudioPlaySystem.getInstance().SongName);

         rootVideoContiner=view.findViewById(R.id.videoRootContainer);


         //video Back function
         viBack=view.findViewById(R.id.videoBack);


         seekBar=view.findViewById(R.id.seekBarVideo);
         btnVideoDown=view.findViewById(R.id.videoDown);
        btnVideoMore=view.findViewById(R.id.video_more);
        btnVideoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time=AudioPlaySystem.getInstance().SongDuration;
                String[] s=time.split(":");
                String hh=s[2];
              //  String hh="00";
                String mm=s[1];
                String ss=s[0];
               VideoModel videoModel=new VideoModel(
                       AudioPlaySystem.getInstance().SongUri,
                       AudioPlaySystem.getInstance().SongName,
                       AudioPlaySystem.getInstance().SongAlbam,
                       AudioPlaySystem.getInstance().SongFolder,
                       AudioPlaySystem.getInstance().SongArtist,
                       AudioPlaySystem.getInstance().AlbumCover,
                       hh,
                       mm,
                       ss
               );

                videoPlayViewModel.SongMenuMore(videoModel,getContext(),getActivity(),v,getFragmentManager());
            }
        });
        btnVideoNext=view.findViewById(R.id.video_Next);
        btnVideoPrevious=view.findViewById(R.id.video_privious);
        btnVideoView=view.findViewById(R.id.videoFullscreen);
        btnVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              videoPlayViewModel.btnVideoView(getActivity());
              //  videoPlayViewModel.ScreenRotation();
            }
        });


        videoPausePlayBtn=view.findViewById(R.id.videoPausePlay);
        videoPausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playClick();
            }
        });

        btnVideoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextClick();
            }
        });
        btnVideoPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               previousClick();
            }
        });
        btnVideoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoPlayViewModel.btnVideoViewMiniFram(getActivity());
//              new Handler().postDelayed(new Runnable() {
//                  @Override
//                  public void run() {
//                      Home.getInstance().DefaultScreenRatio();
//                  }
//              },1000);

            }
        });

        ImageButton screenRptation=view.findViewById(R.id.videoScreenRotation);
        screenRptation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // videoPlayViewModel.PotraitScreen(getActivity());
                Intent intent=new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("screen",0);
                startActivity(intent);
            }
        });

        //fram size
        guidelineLeft=view.findViewById(R.id.guidLineLeft);
        guidelineRight=view.findViewById(R.id.guidLineRight);
        guidelineTop=view.findViewById(R.id.guidLineTop);
        guidelineBootom=view.findViewById(R.id.guidLineBottom);

        paramsLeft=(ConstraintLayout.LayoutParams)guidelineLeft.getLayoutParams();
        paramsRight=(ConstraintLayout.LayoutParams)guidelineRight.getLayoutParams();
        paramsTop=(ConstraintLayout.LayoutParams)guidelineTop.getLayoutParams();
        paramsBottom=(ConstraintLayout.LayoutParams)guidelineBootom.getLayoutParams();

        ImageButton videoScreenSize = view.findViewById(R.id.videoScreensize);
        videoScreenSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ChangeVideoFrame();
            }
        });


        skep10sec=view.findViewById(R.id.forwordSkeep);
        skep10sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoBackShow();
                videoView.seekTo(AudioPlaySystem.getInstance().songProgress+10000);
            }
        });

        previous10sec=view.findViewById(R.id.backwordSkeep);
        previous10sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoBackShow();
                videoView.seekTo(AudioPlaySystem.getInstance().songProgress-10000);
            }
        });

        ConstraintLayout fullLockedLayout=view.findViewById(R.id.fulLockedLayout);
        fullLockedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageButton LockedScreen=view.findViewById(R.id.videoScreenLocked);
        LockedScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullLockedLayout.setVisibility(View.INVISIBLE);
                videoBackShow();
            }
        });
        ImageButton LockScreen=view.findViewById(R.id.videoScreenLock);
        LockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullLockedLayout.setVisibility(View.VISIBLE);
                videoBackHide();
            }
        });
        VideoViewScrollButton();

    }


    int i_state=1;
// video view reles=====
    public void DestroyVideoView(){
        videoView.stopPlayback();
    }

    int vibackDismisTime;boolean videoCatchTime=true,videoWatchTimeUpdate=false;
    public void videoBackShow(){
        viBack.setVisibility(View.VISIBLE);
       // Home.getInstance().videoBackOpen=false;
        videoCatchTime=true;

    }
    public void videoBackHide(){
        viBack.setVisibility(View.INVISIBLE);
      //  Home.getInstance().videoBackOpen=true;
      // Toast.makeText(getContext(), "Hide", Toast.LENGTH_SHORT).show();
    }

    int dbupdate_delay=3000;
    //=========================== SeekBar==========
    public void updateSeekBar() {
        handler.postDelayed(VideoPlayFragment.getInstance().UpdateTimeTask, 10);
    }
    public Runnable UpdateTimeTask=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(videoView.getCurrentPosition());
            seekBar.setMax(videoView.getDuration());
           handler.postDelayed(this,100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    handler.removeCallbacks(UpdateTimeTask);
                    if(Home.getInstance().videoBackOpen){ }
                    else{
                        if(videoCatchTime||videoWatchTimeUpdate){
                            vibackDismisTime=progress+3000;
                            videoCatchTime=false;

                        }
                        if(progress>vibackDismisTime){
                            videoBackHide();
                            Home.getInstance().videoBackOpen=true;
                        }
                    }
                    //update db
                    if(dbupdate_delay<progress){
                        dbupdate_delay+=3000+AudioPlaySystem.getInstance().songProgress;
                        History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,String.valueOf(progress));
                        history.setId(AudioPlaySystem.getInstance().db_item_id);
                        videoPlayViewModel.UpdateHistory(history);
                        Log.e("vProgresss","=uid="+AudioPlaySystem.getInstance().db_item_id);
                    }
                    if(progress!=0)
                    AudioPlaySystem.getInstance().songProgress=progress;
                  //  Log.d("vProgresss","=="+progress);
                    /**
                     * video progress
                     */
                    int seconds = (int) (progress / 1000) % 60 ;
                    int minutes = (int) ((progress / (1000*60)) % 60);
                    int h=(int) ((progress / (1000*60*60)) % 24);
                    videoDuration_running.setText(h+":"+minutes+":"+seconds);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    videoWatchTimeUpdate=true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    handler.removeCallbacks(UpdateTimeTask);
                    videoView.seekTo(seekBar.getProgress());
                    updateSeekBar();
                    videoWatchTimeUpdate=false;
                }
            });
        }
    };


    public void checkplaypauseVideoBtn(){
        if(videoView.isPlaying()){
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
        }else {
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }
    public void playpauseVideo(){

        //  Toast.makeText(getContext(),"pos=="+AudioPlaySystem.getInstance().songProgress,Toast.LENGTH_SHORT).show();
    }
    public void PlayVideo(){
        videoView.start();
    }
    public void StopVideo(){
        videoView.stopPlayback();
        videoView.suspend();
        videoView.clearAnimation();
        videoView.clearFocus();
        videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        videoIsPlaying="pause";
//        getActivity().getFragmentManager().beginTransaction().remove(VideoPlayFragment.this).commit();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(VideoPlayFragment.this).commit();

    }

//=============================== video Framsize
    void ChangeVideoFrame(){
        if(i_state==0) {
            paramsRight.guidePercent = 1f;
            guidelineRight.setLayoutParams(paramsRight);
            paramsLeft.guidePercent = 0f;
            guidelineLeft.setLayoutParams(paramsLeft);
            paramsTop.guidePercent = 0f;
            guidelineTop.setLayoutParams(paramsTop);
            paramsBottom.guidePercent = 1f;
            guidelineBootom.setLayoutParams(paramsBottom);
            i_state = 1;
        } else if(i_state==1) {
            paramsRight.guidePercent = 0.65f;
            guidelineRight.setLayoutParams(paramsRight);
            paramsLeft.guidePercent = 0.35f;
            guidelineLeft.setLayoutParams(paramsLeft);
            i_state=2;
        }else if(i_state==2){
            paramsRight.guidePercent = 1f;
            guidelineRight.setLayoutParams(paramsRight);
            paramsLeft.guidePercent = 0f;
            guidelineLeft.setLayoutParams(paramsLeft);
            paramsTop.guidePercent = 0.1f;
            guidelineTop.setLayoutParams(paramsTop);
            paramsBottom.guidePercent = 0.9f;
            guidelineBootom.setLayoutParams(paramsBottom);
            i_state=3;
        }else if(i_state==3) {
            paramsRight.guidePercent = 0.9f;
            guidelineRight.setLayoutParams(paramsRight);
            paramsLeft.guidePercent = 0.1f;
            guidelineLeft.setLayoutParams(paramsLeft);
            paramsTop.guidePercent = 0.1f;
            guidelineTop.setLayoutParams(paramsTop);
            paramsBottom.guidePercent = 0.9f;
            guidelineBootom.setLayoutParams(paramsBottom);
            i_state = 0;
        }

        constraintSet.applyTo(rootVideoContiner);
    }
    //=================================== video screen rotation====================

    public void ScreenRotation(boolean isDown){
        if(isDown){
            videoPlayViewModel.btnVideoView(getActivity());
            videoPlayViewModel.btnVideoViewMiniFram(getActivity());
        }else  videoPlayViewModel.btnVideoView(getActivity());

    }
    public void ScreenMiniFram(){
        videoPlayViewModel.btnVideoViewMiniFram(getActivity());
    }

    //================================== Video lanMod scrol button View ==============
    ConstraintLayout.LayoutParams paramsScrolButonView;
    Guideline guidelineScrollBtnView;
    public TextView sleepTimeVideoPlay;
    public ImageButton videoAutoPlay;
    public ImageButton likeVideo;
    ImageButton videoSaveInPlaylist;
    public ImageButton videoTimer;
    ImageButton screenshot;
    ImageButton videoBackgroundPlay,scrolviewButton;
    HorizontalScrollView videoPlayHorizantalScrollView;
    boolean scrolBtnViewIsOpen=false;
    void VideoViewScrollButton(){
        videoPlayHorizantalScrollView=view.findViewById(R.id.videoPlayHorizanalScrolView);
        videoPlayHorizantalScrollView.fullScroll(HorizontalScrollView.FOCUS_BACKWARD);



        guidelineScrollBtnView=view.findViewById(R.id.guidLineScrolBtnViiew);
        paramsScrolButonView=(ConstraintLayout.LayoutParams)guidelineScrollBtnView.getLayoutParams();

        scrolviewButton=view.findViewById(R.id.scrolViewBtn);
        scrolviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrolBtnViewIsOpen){
                    paramsScrolButonView.guidePercent=0.8f;
                    guidelineScrollBtnView.setLayoutParams(paramsScrolButonView);
                    scrolviewButton.setImageResource(R.drawable.ic_baseline_chevron_left_24);
                    scrolBtnViewIsOpen=false;
                }else{
                    paramsScrolButonView.guidePercent=0.4f;
                    guidelineScrollBtnView.setLayoutParams(paramsScrolButonView);
                    scrolBtnViewIsOpen=true;
                    scrolviewButton.setImageResource(R.drawable.ic_baseline_chevron_right_24);
                }
                constraintSet.applyTo(rootVideoContiner);
                videoPlayHorizantalScrollView.fullScroll(HorizontalScrollView.FOCUS_BACKWARD);
            }
        });


        //video auto play
        videoAutoPlay=view.findViewById(R.id.videoAutoPlay);
        videoAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayViewModel.ChengeAutoPlayOption();
            }
        });

        // like video
        likeVideo=view.findViewById(R.id.likeVideo);
        likeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayViewModel.videoSongAddRemoveFavouriteList();
            }
        });

        //video Save in Play Lise
        videoSaveInPlaylist=view.findViewById(R.id.videoSaveList);
        videoSaveInPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayViewModel.AddSongInPlayList(getFragmentManager());
            }
        });

        // timer
        sleepTimeVideoPlay=view.findViewById(R.id.timeView);
        videoTimer=view.findViewById(R.id.videoTimer);
        videoTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeForSleep();
            }
        });
        sleepTimeVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeForSleep();
            }
        });
        screenshot=view.findViewById(R.id.screenShot);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayViewModel.askForPermissionsForSs(getContext());
            }
        });
        // video backGround play
        videoBackgroundPlay=view.findViewById(R.id.videoBackground);
        videoBackgroundPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayViewModel.PlayVideoBackground();
            }
        });

    }

    void getTimeForSleep() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_for_brek_time, null);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        final Button off = (Button) mView.findViewById(R.id.off);
        final Button m15 = (Button) mView.findViewById(R.id.m15);
        final Button m30 = (Button) mView.findViewById(R.id.m30);
        final Button m45 = (Button) mView.findViewById(R.id.m45);
        final Button m60 = (Button) mView.findViewById(R.id.m60);

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // timerIsNotOff=false;
                int splashTime = 65 * 60000;
                videoPlayViewModel.SsleepTimer(splashTime, 0, false);

                dialog.hide();
            }
        });
        m15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int splashTime = 0;
                int time = 900000;
                videoPlayViewModel.SsleepTimer(splashTime, time, true);
                dialog.hide();
            }
        });
        m30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int splashTime = 0;
                int time = 30 * 60000;
                videoPlayViewModel.SsleepTimer(splashTime, time, true);
                dialog.hide();
            }
        });
        m45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int splashTime = 0;
                int time = 45 * 60000;
                videoPlayViewModel.SsleepTimer(splashTime, time, true);
                dialog.hide();
            }
        });
        m60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int splashTime = 0;
                int time = 60 * 60000;
//               if(!timerIsNotOff){
//                   timmerCheck();}
//               timerIsNotOff=true;
                videoPlayViewModel.SsleepTimer(splashTime, time, true);
                dialog.hide();
            }
        });
    }

//================================== insert Data in DBM==================================

    void insertDAtaDBM(){
        ContentValues values = new ContentValues();
        values.put(DBManager_History.song_name, AudioPlaySystem.getInstance().SongName);
        values.put(DBManager_History.song_folder, AudioPlaySystem.getInstance().SongFolder);
        values.put(DBManager_History.song_album, "Unknown");
        values.put(DBManager_History.song_album_cover, AudioPlaySystem.getInstance().AlbumCover);
        values.put(DBManager_History.song_artist, "Unknown");
        values.put(DBManager_History.song_duration, AudioPlaySystem.getInstance().SongDuration);
        values.put(DBManager_History.song_path, AudioPlaySystem.getInstance().SongUri);

        long id = dbManager.Insert(values);
        if (id > 0) {
            Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Error Please clear app data", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void nextClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            VideoDetailsFragment.getInstance().NextSong();
        }
    }

    @Override
    public void previousClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            VideoDetailsFragment.getInstance().PreviousSong();
        }
    }

    @Override
    public void playClick() {
        if(videoView.isPlaying()){
            videoView.pause();
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            videoIsPlaying="pause";
        }else {
            videoView.start();
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
            videoIsPlaying="playing";
        }
    }

    @Override
    public void Dismis() {

    }
}