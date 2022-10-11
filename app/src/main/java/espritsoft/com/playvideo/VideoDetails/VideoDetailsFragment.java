package espritsoft.com.playvideo.VideoDetails;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList.PlayListFragment;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;
import espritsoft.com.playvideo.Video.VideoAdaptor;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;
import espritsoft.com.playvideo.VideoDetails.VideoPlayList.VideoDetailsPlayListAdaptor;
import espritsoft.com.playvideo.VideoDetails.ViewModel.VideoDetailsViewModel;
import espritsoft.com.playvideo.VideoPlay.VideoPlayFragment;
import espritsoft.com.playvideo.VideoPlay.ViewModel.VideoPlayViewModel;
import espritsoft.com.playvideo.VideoPlayActivity.VideoPlayActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoDetailsFragment newInstance(String param1, String param2) {
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public static VideoDetailsFragment instance;
    public static   VideoDetailsFragment getInstance(){
        return instance;
    }
    View view;
    public static DBManager_History dbManager;
    public static String RecordID;
    SaveData saveData;
    private VideoDetailsViewModel videoDetailsViewModel;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_details, container, false);
       instance=this;
       saveData=new SaveData(getContext());
        dbManager = new DBManager_History(getContext());


        videoDetailsViewModel= ViewModelProviders.of(getActivity()).get(VideoDetailsViewModel.class);

        CheckUserPermsions();
        initialize();
        if(savedInstanceState !=null){
         //   Home.getInstance().get_All_Video_by_folder(AudioPlaySystem.getInstance().SongFolder);
         //   initialize();
           // videoDetailsViewModel.getVideoQueList();
        }



       return view;
    }

    RecyclerView videoDetailseRecyclerView;
    TextView title;
    TextView folderName;
    public TextView sleepTime;
    public VideoDetailsAdaptor videoDetailsAdaptor;
    public ImageButton videoAutoPlay;
    public ImageButton likeVideo;
    ImageButton videoSaveInPlaylist;
    ImageButton videoTimer;
    ImageButton screenshot,fullScreenPotrait;
    ImageButton videoBackgroundPlay;
    public void initialize() {
        title=view.findViewById(R.id.videoDetails_title);
        folderName=view.findViewById(R.id.videoDetails_folder_name);
        videoDetailseRecyclerView=view.findViewById(R.id.videoDetailsVideoRecyclerView);

        title.setText(AudioPlaySystem.getInstance().SongName);
        folderName.setText(AudioPlaySystem.getInstance().SongFolder);

//


        //padinr top and bottom==================================
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
//int space = getResources().getDimensionPixelSize(
//    R.dimen.list_item_padding_vertical); // from resources
        videoDetailseRecyclerView.addItemDecoration(new VideoSpacesItemDecoration(space));
        videoDetailsAdaptor=new VideoDetailsAdaptor(getContext());
        videoDetailseRecyclerView.setAdapter(videoDetailsAdaptor);
        videoDetailseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        videoDetailsViewModel.getVideoByFolder().observe(this, new Observer<List<VideoModel>>() {
            @Override
            public void onChanged(List<VideoModel> videoModels) {
                videoDetailsAdaptor.setList(videoModels);
            }
        });



        //video auto play
        videoAutoPlay=view.findViewById(R.id.videoAutoPlay);
        videoAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              videoDetailsViewModel.ChengeAutoPlayOption();
            }
        });

        // like video
        likeVideo=view.findViewById(R.id.likeVideo);
        likeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDetailsViewModel.FavouriteBtnClick();
            }
        });

        //video Save in Play Lise
        videoSaveInPlaylist=view.findViewById(R.id.videoSaveList);
        videoSaveInPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDetailsViewModel.AddSongInPlayList(getFragmentManager());
            }
        });

        fullScreenPotrait=view.findViewById(R.id.videoFullscreenPotrait);
        fullScreenPotrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("screen",1);
                startActivity(intent);
            }
        });

        // timer
        sleepTime=view.findViewById(R.id.sleepTime);
        videoTimer=view.findViewById(R.id.videoTimer);
        videoTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeForSleep();
            }
        });
        screenshot=view.findViewById(R.id.screenShot);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDetailsViewModel.TakeVideoScreenShot(getContext());
            }
        });
        // video backGround play
        videoBackgroundPlay=view.findViewById(R.id.videoBackground);
        videoBackgroundPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               videoDetailsViewModel.PlayVideoBackground();
            }
        });


        onAdaptorClickItem();
        findSongPosition();


        videoPlayListView(AudioPlaySystem.getInstance().videoFromPlayList);

        //setIconFavouriteVideoList();
        videoDetailsViewModel.FavouriteSongCheck();
        videoDetailsViewModel.VideoAutoPlayCheck();
    }


   void getTimeForSleep(){
       AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
       View mView=getLayoutInflater().inflate(R.layout.dialog_for_brek_time,null);

       mBuilder.setView(mView);
       AlertDialog dialog = mBuilder.create();
       dialog.show();

       final Button off=(Button) mView.findViewById(R.id.off);
       final Button m15=(Button) mView.findViewById(R.id.m15);
       final Button m30=(Button) mView.findViewById(R.id.m30);
       final Button m45=(Button) mView.findViewById(R.id.m45);
       final Button m60=(Button) mView.findViewById(R.id.m60);

       off.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // timerIsNotOff=false;
              int splashTime=65*60000;
               videoDetailsViewModel.SleepTimer(splashTime,0,false);

               dialog.hide();
           }
       });
       m15.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              int splashTime=0;
              int time=900000;
             videoDetailsViewModel.SleepTimer(splashTime,time,true);
               dialog.hide();
           }
       });
       m30.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              int splashTime=0;
              int time=30*60000;
              videoDetailsViewModel.SleepTimer(splashTime,time,true);
               dialog.hide();
           }
       });
       m45.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int  splashTime=0;
               int  time=45*60000;
               videoDetailsViewModel.SleepTimer(splashTime,time,true);
               dialog.hide();
           }
       });
       m60.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int   splashTime=0;
               int time=60*60000;
//               if(!timerIsNotOff){
//                   timmerCheck();}
//               timerIsNotOff=true;
               videoDetailsViewModel.SleepTimer(splashTime,time,true);
               dialog.hide();
           }
       });



   }



    public int songPosition;
    void onAdaptorClickItem(){
        if(videoDetailsAdaptor !=null){
            videoDetailsAdaptor.setOnItemClickListner(new VideoDetailsAdaptor.OnItemClickListner() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(int position,VideoModel videoModel) {
                    AudioPlaySystem.getInstance().SongUri=videoModel.getPath();
                    AudioPlaySystem.getInstance().AlbumCover=videoModel.getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongArtist=videoModel.getArtist_name();
                    AudioPlaySystem.getInstance().SongName=videoModel.getSong_name();
                    AudioPlaySystem.getInstance().SongFolder=videoModel.getFolderName();
                    AudioPlaySystem.getInstance().SongDuration=videoModel.getHour()+":"+
                            videoModel.getMunite()+":"+videoModel.getSecond() ;
                    AudioPlaySystem.getInstance().songProgress=0;

                    songPosition=position;
                    videoDetailsViewModel.getVideoQueList();
                    AudioPlaySystem.getInstance().videoFromPlayList=0;
//                    Home.getInstance().videoPlayIsActive=true;
                  //  ((Home)getActivity()).PlayVideo();
                    History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,"0");
                    videoDetailsViewModel.insertHistory(history);
                    ((Home)getActivity()).PlayVideo();
                  //  VideoPlayFragment.getInstance().init_videoPlay();
                   // videoPlayListView(AudioPlaySystem.getInstance().videoFromPlayList);
                }

                @Override
                public void menudialog(VideoModel videoModel,View v) {
                    videoDetailsViewModel.SongMenuMore(videoModel,getContext(),getActivity(),v,getFragmentManager());
                }
            });
        }
    }
    String TAG;
    void findSongPosition(){

       for(int i=0;i< AudioPlaySystem.getInstance().VideoQueList.size();i++){
           if(AudioPlaySystem.getInstance().SongName.equals(AudioPlaySystem.getInstance().VideoQueList.get(i).getSong_name())){
               songPosition=i;
               break;
           }
       }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void NextSong(){

        if(songPosition<AudioPlaySystem.getInstance().VideoQueList.size()-1) {
            songPosition++;
            AudioPlaySystem.getInstance().SongUri = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getPath();
            AudioPlaySystem.getInstance().AlbumCover = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getAlbumArtUriImage();
            AudioPlaySystem.getInstance().SongArtist = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getArtist_name();
            AudioPlaySystem.getInstance().SongName = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getSong_name();
            AudioPlaySystem.getInstance().SongFolder = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getFolderName();
            AudioPlaySystem.getInstance().SongDuration = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getHour() + ":" +
                    AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getMunite()+":"+AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getSecond();
            AudioPlaySystem.getInstance().songProgress=0;

            History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,"0");
            videoDetailsViewModel.insertHistory(history);
            VideoPlayFragment.getInstance().init_videoPlay();
            AudioPlaySystem.getInstance().db_item_id=videoDetailsViewModel.getId();
          // ((Home) getActivity()).PlayVideo();


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void PreviousSong() {

        if (songPosition > 0) {
            songPosition--;
            AudioPlaySystem.getInstance().AlbumCover = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getAlbumArtUriImage();
            AudioPlaySystem.getInstance().SongArtist = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getArtist_name();
            AudioPlaySystem.getInstance().SongUri = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getPath();
            AudioPlaySystem.getInstance().SongName = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getSong_name();
            AudioPlaySystem.getInstance().SongFolder = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getFolderName();
            AudioPlaySystem.getInstance().SongDuration = AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getHour() + ":" +
                    AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getMunite()+":"+AudioPlaySystem.getInstance().VideoQueList.get(songPosition).getSecond();
            AudioPlaySystem.getInstance().songProgress=0;

            History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,"0");
            videoDetailsViewModel.insertHistory(history);
            VideoPlayFragment.getInstance().init_videoPlay();
            AudioPlaySystem.getInstance().db_item_id=videoDetailsViewModel.getId();
               //((Home) getActivity()).PlayVideo();
        }
    }



    public void RefrashList(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                videoDetailsViewModel.getPermission(AudioPlaySystem.getInstance().SongFolder);
                videoDetailsAdaptor.notifyDataSetChanged();
            }
        },1000);

    }

    //================================================================ User Permission =============================================================
    public void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE,},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        videoDetailsViewModel.getPermission(AudioPlaySystem.getInstance().SongFolder);
    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    videoDetailsViewModel.getPermission(AudioPlaySystem.getInstance().SongFolder);
                } else {
                    // Permission Denied
                    Alart();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public androidx.appcompat.app.AlertDialog.Builder Alart() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        mBuilder.setTitle("Alart")
                .setMessage("App Will not working properly.Please Allow all permission")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckUserPermsions();
                    }
                });

        mBuilder.create();
        mBuilder.show();
        return mBuilder;


    }


    /**
     * Video PlayList View layout
     */
    ConstraintLayout videoPlayListViewRoot,videoPlayListViewMine;
    RelativeLayout vMini,vFull;
    Guideline videoPlaListMarzin;
    ConstraintSet constraintSet=new ConstraintSet();
    ConstraintLayout.LayoutParams paramsHorizantalPlayListMArzin;
    ImageButton videoPlaListVideDismiss,videoPlayOption;
    RecyclerView recyclerViewVideoPlayList;
    VideoDetailsPlayListAdaptor videoDetailsPlayListAdaptor;
    TextView playListNameMini,playListName,nextSongName;
    void videoPlayListView(int stet){
        videoPlaListMarzin=view.findViewById(R.id.videoPlayListMarzin);
        videoPlayListViewMine=view.findViewById(R.id.videoPlaylistViewMini);
        paramsHorizantalPlayListMArzin = (ConstraintLayout.LayoutParams) videoPlaListMarzin.getLayoutParams();


        vMini=view.findViewById(R.id.vMini);
        vFull=view.findViewById(R.id.vFull);
        vFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsHorizantalPlayListMArzin.guidePercent = 0F; // 45% // range: 0 <-> 1
                videoPlaListMarzin.setLayoutParams(paramsHorizantalPlayListMArzin);
                animationView();

                vMini.setVisibility(View.INVISIBLE);
                vFull.setVisibility(View.VISIBLE);
                constraintSet.applyTo(videoPlayListViewMine);
                AudioPlaySystem.getInstance().videoFromPlayList=2;
            }
        });
        videoPlaListVideDismiss=view.findViewById(R.id.dismisFullView);
        videoPlaListVideDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsHorizantalPlayListMArzin.guidePercent = 0.9F; // 45% // range: 0 <-> 1
                videoPlaListMarzin.setLayoutParams(paramsHorizantalPlayListMArzin);
                animationView();

                vMini.setVisibility(View.VISIBLE);
                vFull.setVisibility(View.INVISIBLE);
                constraintSet.applyTo(videoPlayListViewMine);
                AudioPlaySystem.getInstance().videoFromPlayList=1;
            }
        });


        //set PlayList details
        SetLisstDetails();
        //state=============================
        if(stet==1){
            vMini.setVisibility(View.VISIBLE);
            vFull.setVisibility(View.INVISIBLE);
            paramsHorizantalPlayListMArzin.guidePercent = 0.9F; // 45% // range: 0 <-> 1
            videoPlaListMarzin.setLayoutParams(paramsHorizantalPlayListMArzin);
            animationView();
            constraintSet.applyTo(videoPlayListViewMine);
        }else if(stet==2){
            vMini.setVisibility(View.INVISIBLE);
            vFull.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    paramsHorizantalPlayListMArzin.guidePercent = 0F; // 45% // range: 0 <-> 1
                    videoPlaListMarzin.setLayoutParams(paramsHorizantalPlayListMArzin);
                    animationView();
                    constraintSet.applyTo(videoPlayListViewMine);
                }
            },800);

        }else{
            vMini.setVisibility(View.INVISIBLE);
            vFull.setVisibility(View.INVISIBLE);
            paramsHorizantalPlayListMArzin.guidePercent = 1F; // 45% // range: 0 <-> 1
            videoPlaListMarzin.setLayoutParams(paramsHorizantalPlayListMArzin);
            animationView();
            constraintSet.applyTo(videoPlayListViewMine);
        }

        //setAdaptor
        recyclerViewVideoPlayList=view.findViewById(R.id.recyclerViewVideoPlayList);
        videoDetailsPlayListAdaptor=new VideoDetailsPlayListAdaptor(AudioPlaySystem.getInstance().VideoQueList,getContext());
        recyclerViewVideoPlayList.setAdapter(videoDetailsPlayListAdaptor);
        recyclerViewVideoPlayList.setLayoutManager(new LinearLayoutManager(getContext()));

        if(videoDetailsPlayListAdaptor!=null){
            videoDetailsPlayListAdaptor.setOnItemClickListner(new VideoDetailsPlayListAdaptor.onItemClickListner() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void itemClick(int position,VideoModel videoModel) {
                    AudioPlaySystem.getInstance().SongUri = videoModel.getPath();
                    AudioPlaySystem.getInstance().AlbumCover = videoModel.getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongArtist = videoModel.getArtist_name();
                    AudioPlaySystem.getInstance().SongName = videoModel.getSong_name();
                    AudioPlaySystem.getInstance().SongFolder = videoModel.getFolderName();
                    AudioPlaySystem.getInstance().SongDuration = videoModel.getHour() + ":" +
                            videoModel.getMunite()+":"+videoModel.getSecond();
                    AudioPlaySystem.getInstance().songProgress=0;
                    AudioPlaySystem.getInstance().videoFromPlayList=2;
                    songPosition=position;
                    VideoPlayFragment.getInstance().init_videoPlay();
                    videoDetailsPlayListAdaptor.notifyDataSetChanged();
                    SetLisstDetails();

                }

                @Override
                public void menu(int position) {

                }
            });
        }

    }
    void SetLisstDetails(){
        //set name
        playListNameMini=view.findViewById(R.id.videoPlaylistNameMini);
        playListName=view.findViewById(R.id.videoPlayListName);
        nextSongName=view.findViewById(R.id.vPlayNextSongName);
        int listSiz=AudioPlaySystem.getInstance().VideoQueList.size();
        playListName.setText(AudioPlaySystem.getInstance().PlayListName+"."+songPosition+"/"+listSiz);
        playListNameMini.setText(AudioPlaySystem.getInstance().PlayListName+"."+songPosition+"/"+listSiz);
        String nName;
        if(songPosition<AudioPlaySystem.getInstance().VideoQueList.size()-1) {
            int sp = songPosition + 1;
            nName = AudioPlaySystem.getInstance().VideoQueList.get(sp).getSong_name();
        }else{
            nName = AudioPlaySystem.getInstance().VideoQueList.get(0).getSong_name();
        }
        nextSongName.setText("Next "+nName);
    }

    void animationView(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition transition = new ChangeBounds();
            transition.setInterpolator(new AnticipateOvershootInterpolator());
            transition.setDuration(1000);

            TransitionManager.beginDelayedTransition(videoPlayListViewMine, transition);
        }
    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}