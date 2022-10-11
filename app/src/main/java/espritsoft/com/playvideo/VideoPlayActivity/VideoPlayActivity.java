package espritsoft.com.playvideo.VideoPlayActivity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsFragment;
import espritsoft.com.playvideo.VideoPlay.VideoPlayFragment;
import espritsoft.com.playvideo.VideoPlayActivity.ViewModel.VideoPlayActivityViewModel;

public class VideoPlayActivity extends AppCompatActivity {

    public static VideoPlayActivity instens;
    public static VideoPlayActivity getInstance(){
        return instens;
    }

    SaveData saveData;int ScreenIsPotrait=1;
    VideoPlayActivityViewModel videoPlayActivityViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        instens=this;
        saveData=new SaveData(this);

        videoPlayActivityViewModel= ViewModelProviders.of(this).get(VideoPlayActivityViewModel.class);

        Intent intent=getIntent();
        ScreenIsPotrait=intent.getIntExtra("screen",1);
        iniTializeLayout();
        videoBackHide();

    }


    public void init_videoPlay() {
        videoDuretion=findViewById(R.id.duration);
        videoDuretionRunning=findViewById(R.id.duration_running);
        videoDuretion.setText(AudioPlaySystem.getInstance().SongDuration);

        videoPlayActivityViewModel.InitVideoView(videoView);
        videoView.start();
        videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);

        handler=new Handler();
        updateSeekBar();

    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkplaypauseVideoBtn();
            }
        },400);
    }


    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsRight;
    ConstraintLayout.LayoutParams paramsLeft;
    ConstraintLayout.LayoutParams paramsTop;
    ConstraintLayout.LayoutParams paramsBottom;

    public VideoView videoView;
    ImageButton videoPausePlayBtn;
    SeekBar seekBar;Handler handler;ConstraintLayout videoBack;
    Guideline guidLine_btnView;String TAG; boolean backMove=false;
    ImageButton btnVideoNext,btnVideoPrevious,btnVideoDown,btnVideoMore,btnVideoView,btnVideoMiniView;
    TextView videoDuretion,videoDuretionRunning,videoSongName;
    ConstraintLayout viBack,rootVideoContiner;
    Guideline guidelineBootom,guidelineTop,guidelineRight,guidelineLeft;
    private void iniTializeLayout() {
        videoView=findViewById(R.id.videoView);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoBackOpen){
                    videoBackHide();
                }else videoBackShow();
            }
        });

        videoSongName=findViewById(R.id.songName);
        videoSongName.setText(AudioPlaySystem.getInstance().SongName);
        rootVideoContiner=findViewById(R.id.rootContainer);


        //video Back function
        viBack=findViewById(R.id.videoBack);


        seekBar=findViewById(R.id.seekBarVideo);
        btnVideoDown=findViewById(R.id.videoDown);
        btnVideoNext=findViewById(R.id.video_Next);
        btnVideoPrevious=findViewById(R.id.video_privious);
        btnVideoView=findViewById(R.id.videoFullscreen);
        btnVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // videoPlayViewModel.btnVideoView(getActivity());
                //  videoPlayViewModel.ScreenRotation();
                finish();
                if(ScreenIsPotrait==0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VideoPlayFragment.getInstance().ScreenRotation(false);
                        }
                    }, 400);
                }else VideoPlayFragment.getInstance().PlayVideo();
            }
        });

        btnVideoMore=findViewById(R.id.video_mores);
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
                        ss,
                        mm,
                        hh
                );

                videoPlayActivityViewModel.SongMenuMore(videoModel,VideoPlayActivity.this, VideoPlayActivity.this,v,getSupportFragmentManager());


            }
        });



        videoPausePlayBtn=findViewById(R.id.videoPausePlay);
        videoPausePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpauseVideo();
            }
        });

        btnVideoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailsFragment.getInstance().NextSong();
                init_videoPlay();
            }
        });
        btnVideoPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailsFragment.getInstance().PreviousSong();
                init_videoPlay();
            }
        });
        btnVideoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(ScreenIsPotrait==0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VideoPlayFragment.getInstance().ScreenRotation(true);
                        }
                    }, 400);
                }else{
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            VideoPlayFragment.getInstance().ScreenMiniFram();
//                        }
//                    }, 200);
                    VideoPlayFragment.getInstance().ScreenMiniFram();
                    VideoPlayFragment.getInstance().PlayVideo();
                }
            }
        });



        //fram size
        guidelineLeft=findViewById(R.id.guidLineLeft);
        guidelineRight=findViewById(R.id.guidLineRight);
        guidelineTop=findViewById(R.id.guidLineTop);
        guidelineBootom=findViewById(R.id.guidLineBottom);

        paramsLeft=(ConstraintLayout.LayoutParams)guidelineLeft.getLayoutParams();
        paramsRight=(ConstraintLayout.LayoutParams)guidelineRight.getLayoutParams();
        paramsTop=(ConstraintLayout.LayoutParams)guidelineTop.getLayoutParams();
        paramsBottom=(ConstraintLayout.LayoutParams)guidelineBootom.getLayoutParams();

        ImageButton videoScreenSize = findViewById(R.id.videoScreensize);
        videoScreenSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ChangeVideoFrame();
            }
        });


        VideoViewScrollButton();
        init_videoPlay();
    }
    int vibackDismisTime;boolean videoCatchTime=true,videoWatchTimeUpdate=false,videoBackOpen=false;;
    public void videoBackShow(){
        viBack.setVisibility(View.VISIBLE);
       videoBackOpen=true;
        videoCatchTime=true;

    }
    public void videoBackHide(){
        viBack.setVisibility(View.INVISIBLE);
        //  Home.getInstance().videoBackOpen=true;
        // Toast.makeText(getContext(), "Hide", Toast.LENGTH_SHORT).show();
        videoBackOpen=false;

    }


    int dbupdate_delay=3000;
    //=========================== SeekBar==========
    public void updateSeekBar() {
        handler.postDelayed(UpdateTimeTask, 10);
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
                        if(videoCatchTime||videoWatchTimeUpdate){
                            vibackDismisTime=progress+3000;
                            videoCatchTime=false;
                        }
                        if(progress>vibackDismisTime){
                            videoBackHide();
                            Home.getInstance().videoBackOpen=true;

                       }
                    //update db
                    if(dbupdate_delay<progress){
                        dbupdate_delay+=3000+AudioPlaySystem.getInstance().songProgress;
                        History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,String.valueOf(progress));
                        history.setId(AudioPlaySystem.getInstance().db_item_id);
                        videoPlayActivityViewModel.UpdateHistory(history);
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
                    videoDuretionRunning.setText(h+":"+minutes+":"+seconds);
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


    //=================== video btn
    public void checkplaypauseVideoBtn(){
        if(videoView.isPlaying()){
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
        }else {
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }
    public void playpauseVideo(){
        if(videoView.isPlaying()){
            videoView.pause();
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }else {
            videoView.start();
            videoPausePlayBtn.setImageResource(R.drawable.ic_baseline_pause_24);
        }
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
        videoPlayHorizantalScrollView=findViewById(R.id.videoPlayHorizanalScrolView);
        videoPlayHorizantalScrollView.fullScroll(HorizontalScrollView.FOCUS_BACKWARD);



        guidelineScrollBtnView=findViewById(R.id.guidLineScrolBtnViiew);
        paramsScrolButonView=(ConstraintLayout.LayoutParams)guidelineScrollBtnView.getLayoutParams();

        scrolviewButton=findViewById(R.id.scrolViewBtn);
        scrolviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrolBtnViewIsOpen){
                    paramsScrolButonView.guidePercent=0.8f;
                    guidelineScrollBtnView.setLayoutParams(paramsScrolButonView);
                    scrolviewButton.setImageResource(R.drawable.ic_baseline_chevron_left_24);
                    scrolBtnViewIsOpen=false;
                }else{
                    paramsScrolButonView.guidePercent=0.2f;
                    guidelineScrollBtnView.setLayoutParams(paramsScrolButonView);
                    scrolBtnViewIsOpen=true;
                    scrolviewButton.setImageResource(R.drawable.ic_baseline_chevron_right_24);
                }
                constraintSet.applyTo(rootVideoContiner);
                videoPlayHorizantalScrollView.fullScroll(HorizontalScrollView.FOCUS_BACKWARD);
            }
        });


        //video auto play
        videoAutoPlay=findViewById(R.id.videoAutoPlay);
        videoAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveData.LoadDataVideoAutoPlayOption()==0)
                    saveData.SaveDataVideoAutoPlayOption(1);
                else saveData.SaveDataVideoAutoPlayOption(0);
                checkAutoPlayOption();
            }
        });

        // like video
        likeVideo=findViewById(R.id.likeVideo);
        likeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayActivityViewModel.videoSongAddRemoveFavouriteList();
                checkFavouriteVideoList();
            }
        });

        //video Save in Play Lise
        videoSaveInPlaylist=findViewById(R.id.videoSaveList);
        videoSaveInPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayActivityViewModel.AddSongInPlayList(getSupportFragmentManager());
            }
        });
        screenshot=findViewById(R.id.screenShot);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayActivityViewModel.screenShot(getApplicationContext());
            }
        });
        // video backGround play
        videoBackgroundPlay=findViewById(R.id.videoBackground);
        videoBackgroundPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // videoPlayActivityViewModel.PlayVideoBackground();
                finish();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videoPlayActivityViewModel.PlayVideoBackground();
                    }
                },400);
            }
        });

        ImageButton screenRptation=findViewById(R.id.videoScreenRotation);
        screenRptation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // videoPlayViewModel.PotraitScreen(getActivity());
                finish();
                if(ScreenIsPotrait==0) {
                    VideoPlayFragment.getInstance().PlayVideo();
                }else{
                    VideoPlayFragment.getInstance().ScreenRotation(false);
                }
            }
        });

        checkAutoPlayOption();
        checkFavouriteVideoList();
    }

    //================= check status
    void checkAutoPlayOption(){
        if(saveData.LoadDataVideoAutoPlayOption()==0){
            videoAutoPlay.setImageResource(R.drawable.ic_baseline_play_disabled_24);
        }else{
            videoAutoPlay.setImageResource(R.drawable.ic_right_arrow_svg);
        }
    }
    // Favourite ========================================check========
    // ============ favourite list check===========================
    boolean plaListVideoadd=false;
    public static DBManager_History dbManager;
    public static String RecordID;
    public  void checkFavouriteVideoList() {
        dbManager=new DBManager_History(getApplicationContext());
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
                        && cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals("Favourite Video")) {
                   likeVideo.setImageResource(R.drawable.ic_baseline_favorite_24);
                    RecordID=cursor.getString(cursor.getColumnIndex(DBManager_History.ColID));
                    plaListVideoadd=true;
                    break;
                }else if (AudioPlaySystem.getInstance().SongName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                        && !cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals("Favourite Video")) {
                    likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    plaListVideoadd=false;

                }
//                else{
//                    likeMusic.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                }

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }else{
           likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            plaListVideoadd=false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(videoView.isPlaying()){
            VideoPlayFragment.getInstance().PlayVideo();
        }
    }

}