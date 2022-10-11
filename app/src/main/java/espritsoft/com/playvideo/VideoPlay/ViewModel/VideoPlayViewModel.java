package espritsoft.com.playvideo.VideoPlay.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.Repository.AudioPlayRepositor;
import espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList.PlayListFragment;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.DBManager.RepositoryHistoryDB.RoomDbHistoryRepository;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.SaveData.SaveData;
import espritsoft.com.playvideo.Video.VideoFragment;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsFragment;
import espritsoft.com.playvideo.VideoDetails.ViewModel.VideoDetailsViewModel;
import espritsoft.com.playvideo.VideoPlay.AudioTracks.AudioTrackAdaptor;
import espritsoft.com.playvideo.VideoPlay.AudioTracks.trackModel;
import espritsoft.com.playvideo.VideoPlay.VideoPlayFragment;

public class VideoPlayViewModel extends AndroidViewModel {

    private RoomDbHistoryRepository roomDbHistoryRepository;

    private AudioPlayRepositor audioPlayRepositor;
    private static Context context;

    public static DBManager_History dbManager;
    public static String RecordID;
    SaveData saveData;



    public VideoPlayViewModel(@NonNull Application application) {
        super(application);
        roomDbHistoryRepository=new RoomDbHistoryRepository(application);
        context=application;
        dbManager=new DBManager_History(application);
        saveData=new SaveData(application);
        audioPlayRepositor=new AudioPlayRepositor(application);

    }

    public void insertHistory(History history){
      roomDbHistoryRepository.insert(history);
    }

    public void UpdateHistory(History history){
        roomDbHistoryRepository.upddate(history);
    }
    public int getId(){
        return roomDbHistoryRepository.getId();
    }




    //=========================================================== Video Play ============
    public long  videoProgress= (long) 0f;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    VideoView videoView;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void initilaizVideoPlay(VideoView v, int index, boolean track){
        videoView = v;
        try {
            v.setMediaController(null);
            v.setVideoPath(AudioPlaySystem.getInstance().SongUri);
                v.seekTo((int) AudioPlaySystem.getInstance().songProgress);
          //  videoProgress=v.getCurrentPosition();
          //  v.start();
            /** set audio track **/
            audiouTrake(v,context,index,track);


        } catch (Exception e) {
            e.printStackTrace();
        }

        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(saveData.LoadDataVideoAutoPlayOption()==1){
                    VideoDetailsFragment.getInstance().NextSong();
                }
            }
        });

    }
    /**
     * video song audio trake
     */
    ArrayList<trackModel>tarckList=new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   public void audiouTrake(VideoView mVideoView,Context context,int index,boolean track){
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                tarckList.clear();
                MediaPlayer.TrackInfo[] trackInfoArray = mp.getTrackInfo();
                for (int i = 0; i < trackInfoArray.length; i++) {
                    if (trackInfoArray[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO){
                        tarckList.add(new trackModel(trackInfoArray[i].getLanguage(),i));
                    }
                }
                if (track){
                    mp.selectTrack(index);
                    trackInfoArray[index].getLanguage();
                }

                return true;
            }
        });


    }


   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   public void DialogViewAudioTrake(Activity activity, Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        View view= activity.getLayoutInflater().inflate(R.layout.dialog_list_audio_trake,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
       videoView.pause();
       dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
           @Override
           public void onDismiss(DialogInterface dialog) {
               videoView.start();
           }
       });
        RecyclerView recyclerView=view.findViewById(R.id.audio_track_list);
        AudioTrackAdaptor adaptor=new AudioTrackAdaptor(context,tarckList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adaptor.setOnItemClickListner(new AudioTrackAdaptor.OnItemClickListner() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onItemClick(int position, trackModel videoModel) {
                    initilaizVideoPlay(videoView,tarckList.get(position).getIndeg(),true);
                    dialog.dismiss();
                }

                @Override
                public void menudialog(VideoModel videoModel, View v) {

                }
            });



    }

    //video Mode Chenge
    public void btnVideoView(Activity activity){
        int orientation =activity.getResources().getConfiguration().orientation;
        switch(orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
                Home.getInstance().forcelyActivetedLandspeceMood=true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
                Home.getInstance().forcelyActivetedLandspeceMood=false;
                break;
        }
    }
    public void PotraitScreen(Activity activity){
        int orientation =activity.getResources().getConfiguration().orientation;
        switch(orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                ScreenRotation();
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                btnVideoView(activity);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScreenRotation();
                    }
                },500);
                break;
        }

    }

    //change screen size screen Rotation
    public void ScreenRotation(){
        Home.getInstance().ScreenRatioChange();
    }

    //video miniView  screen
    public void btnVideoViewMiniFram(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Home.getInstance().VideoFremmSizeIsExpendet(false);
            Home.getInstance().bottomNavigationShow();
        }
        activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
        Home.getInstance().forcelyActivetedLandspeceMood=false;
    }


    //video Auto play
    public void ChengeAutoPlayOption(){
    if(saveData.LoadDataVideoAutoPlayOption()==0){
        saveData.SaveDataVideoAutoPlayOption(1);
        videoAutoPlayCheck();
    }else{
        saveData.SaveDataVideoAutoPlayOption(0);
        videoAutoPlayCheck();
    }}
    public void videoAutoPlayCheck(){
        if(saveData.LoadDataVideoAutoPlayOption()==0){
           VideoDetailsFragment.getInstance().videoAutoPlay.setImageResource(R.drawable.ic_baseline_play_disabled_24);
           VideoPlayFragment.getInstance().videoAutoPlay.setImageResource(R.drawable.ic_baseline_play_disabled_24);
        }else{
            VideoDetailsFragment.getInstance().videoAutoPlay.setImageResource(R.drawable.ic_right_arrow_svg);
            VideoPlayFragment.getInstance().videoAutoPlay.setImageResource(R.drawable.ic_right_arrow_svg);
        }
    }


    /**
     * video background play
     */

    public void PlayVideoBackground(){
        AudioPlaySystem.getInstance().SongFolder=AudioPlaySystem.getInstance().SongFolder;
        AudioPlaySystem.getInstance().SongUri=AudioPlaySystem.getInstance().SongUri;
        AudioPlaySystem.getInstance().AlbumCover=AudioPlaySystem.getInstance().AlbumCover;
        AudioPlaySystem.getInstance().SongName=AudioPlaySystem.getInstance().SongName;
        AudioPlaySystem.getInstance().SongAlbam=AudioPlaySystem.getInstance().SongFolder;
        AudioPlaySystem.getInstance().SongDuration=AudioPlaySystem.getInstance().SongDuration ;

        //vidieo player relese;


        Home.getInstance().PlayAudio();
       // Home.getInstance().AudiouQueueList();
         getAudioQueList();
        AudioPlaySystem.getInstance().AudioPlay();

        Home.getInstance().removeFragment();
    }
    public void getAudioQueList(){
        AudioPlaySystem.getInstance().queueList.clear();
        for(int i=0;i<AudioPlaySystem.getInstance().VideoQueList.size();i++){
            AudioPlaySystem.getInstance().queueList.add(new AudiouModel(AudioPlaySystem.getInstance().VideoQueList.get(i).getPath(),AudioPlaySystem.getInstance().VideoQueList.get(i).getSong_name(),
                    AudioPlaySystem.getInstance().VideoQueList.get(i).getAlbam_name(),AudioPlaySystem.getInstance().VideoQueList.get(i).getArtist_name(),AudioPlaySystem.getInstance().VideoQueList.get(i).getAlbumArtUriImage(),AudioPlaySystem.getInstance().VideoQueList.get(i).second,AudioPlaySystem.getInstance().VideoQueList.get(i).getMunite(),AudioPlaySystem.getInstance().VideoQueList.get(i).getHour(),
                    AudioPlaySystem.getInstance().VideoQueList.get(i).getFolderName()));
        }

    }

    //slip time=========================================
  //  int splashTime=0;boolean timerIsNotOff=false;int time;
    int splashTime,time;
    public boolean timerIsNotOff=false;
    public void SsleepTimer(int splashTimes,int time,boolean timerIsNotOf){
        if(timerIsNotOff){
            SlipTimer(splashTimes,time,timerIsNotOff,false);
        }else {
            SlipTimer(splashTimes,time,timerIsNotOff,true);
        }
    }
    public void SlipTimer(int splashTimes,int times,boolean timerIsNotOffs,boolean reRun){
         splashTime=splashTimes;
         time=times;
         timerIsNotOff=timerIsNotOffs;

         if(reRun) TimeCheck();
         if(!timerIsNotOffs) TimeCheck();
    }
    String tempTime;
    public void TimeCheck(){
        if (time > splashTime) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashTime+=1000;
                    int m=((time-splashTime)/1000)/60;
                    int s=((time-splashTime)/1000)%60;
                     tempTime=(m+":"+s);
                     TimeCheck();
                     VideoDetailsFragment.getInstance().sleepTime.setText(tempTime);
                     VideoPlayFragment.getInstance().sleepTimeVideoPlay.setText(tempTime);

                    VideoPlayFragment.getInstance().sleepTimeVideoPlay.setVisibility(View.VISIBLE);
                    VideoPlayFragment.getInstance().videoTimer.setVisibility(View.INVISIBLE);
                }
            },1000);
        }else{
            if(timerIsNotOff){
                VideoPlayFragment.getInstance().playClick();
              //  VideoDetailsFragment.getInstance().sleepTime.setText("Sleep");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    VideoDetailsFragment.getInstance().sleepTime.setText("Sleep");
                    VideoPlayFragment.getInstance().sleepTimeVideoPlay.setText("Sleep");
                    VideoPlayFragment.getInstance().sleepTimeVideoPlay.setVisibility(View.INVISIBLE);
                    VideoPlayFragment.getInstance().videoTimer.setVisibility(View.VISIBLE);
                }
            },1000);
            timerIsNotOff=false;
        }
    }

    //============================= add video on Play lis
    public void AddSongInPlayList(FragmentManager fragmentManager){
        //  View bottomView=getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog,null);
        // LinearLayout call=bottomView.findViewById(R.id.layout)

        PlayListDataForSave.PName=AudioPlaySystem.getInstance().SongName;
        PlayListDataForSave.PCover=AudioPlaySystem.getInstance().AlbumCover;
        PlayListDataForSave.PAlbum=AudioPlaySystem.getInstance().SongFolder;
        PlayListDataForSave.PFolder=AudioPlaySystem.getInstance().SongFolder;
        PlayListDataForSave.PDuration=AudioPlaySystem.getInstance().SongDuration;
        PlayListDataForSave.PArtist="Unknown";
        PlayListDataForSave.PSongPath=AudioPlaySystem.getInstance().SongUri;

        PlayListFragment playListFragment=new PlayListFragment();
        playListFragment.show(fragmentManager,null);
    }

    public void AddSongInPlayListVideoModel(VideoModel videoModel,FragmentManager fragmentManager){
        PlayListDataForSave.PName= videoModel.getSong_name();
        PlayListDataForSave.PCover=videoModel.getAlbumArtUriImage();
        PlayListDataForSave.PAlbum=videoModel.getAlbam_name();
        PlayListDataForSave.PFolder=videoModel.getFolderName();
        PlayListDataForSave.PDuration=videoModel.getHour()+":"+videoModel.getMunite()+":"+videoModel.getSecond();
        PlayListDataForSave.PArtist="Unknown";
        PlayListDataForSave.PSongPath=videoModel.getPath();

        PlayListFragment playListFragment=new PlayListFragment();
        playListFragment.show(fragmentManager,null);
    }


    // ScreeShot take ==================================
    public void askForPermissionsForSs(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                context.startActivity(intent);
                return;
            }
            TakeScreenShot();
        }
    }

    File folder;
    public void TakeScreenShot(){
        folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "MyPlay Video App");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
            Log.d("new=====","ss");
        }
        if (success) {
            // Do something on success
            Log.d("new=====","succ");
        } else {
            // Do something else on failure
            Log.d("new=====","fail");
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String strDate = sdf.format(c.getTime());
        Log.d("Date","DATE : " + strDate);
        try {

            // image naming and path  to include sd card  appending name you choose for file
            String mPath = folder+"/"+"videoPlay"+strDate.toString()+".jpg";

            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(AudioPlaySystem.getInstance().SongUri);
            Bitmap extractedImage = media.getFrameAtTime(AudioPlaySystem.getInstance().songProgress);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            extractedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(context,"Captured",Toast.LENGTH_SHORT).show();

            // openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }

    }
    // Favourite ========================================check========
    // ============ favourite list check===========================
    boolean plaListVideoadd=false;
    public  void setIconFavouriteVideoList() {
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
                    VideoDetailsFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_24);
                    VideoPlayFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_24);
                    RecordID=cursor.getString(cursor.getColumnIndex(DBManager_History.ColID));
                    plaListVideoadd=true;
                    break;
                }else if (AudioPlaySystem.getInstance().SongName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                        && !cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals("Favourite Video")) {
                    VideoDetailsFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    VideoPlayFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    plaListVideoadd=false;

                }
//                else{
//                    likeMusic.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                }

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }else{
           VideoDetailsFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            VideoPlayFragment.getInstance().likeVideo.setImageResource(R.drawable.ic_baseline_favorite_border_24);
           plaListVideoadd=false;
        }

    }
    //=========== add ed favorite list
   public void videoSongAddRemoveFavouriteList(){
        if(plaListVideoadd){
            String[] SelectionArgss = new String[]{RecordID};
            int count = dbManager.Delet2("ID=? ", SelectionArgss);
            //refresh Element
            if (count > 0) {
                setIconFavouriteVideoList();
            }

        }else{
            insertPlayListSongAtDBM("Favourite Video");
            setIconFavouriteVideoList();
        }
    }
    /** DBM  work procidure ===================================== **/

    void insertPlayListSongAtDBM(String playListName){
        ContentValues values = new ContentValues();
        values.put(DBManager_History.song_name, AudioPlaySystem.getInstance().SongName);
        values.put(DBManager_History.song_folder, AudioPlaySystem.getInstance().SongFolder);
        values.put(DBManager_History.song_album, AudioPlaySystem.getInstance().SongAlbam);
        values.put(DBManager_History.song_album_cover, AudioPlaySystem.getInstance().AlbumCover);
        values.put(DBManager_History.song_artist, "unknown");
        values.put(DBManager_History.song_duration, AudioPlaySystem.getInstance().SongDuration);
        values.put(DBManager_History.song_path, AudioPlaySystem.getInstance().SongUri);
        values.put(DBManager_History.playl_list_name,playListName);

        long id = dbManager.Insert_table2(values);
        if (id > 0) {
          //  Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_LONG).show();
            setIconFavouriteVideoList();
        } else {
          //  Toast.makeText(getContext(), "Error Please clear app data", Toast.LENGTH_LONG).show();
        }
    }




    //item Menu Dialog
    /**
     * song menu more
     * @param context
     * @param view
     * @param fragmentManager
     */
    public void SongMenuMore(VideoModel videoModel, Context context, Activity activity, View view, FragmentManager fragmentManager){
        PopupMenu popupMenu=new PopupMenu(context,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AccessFilePermission(context);
                switch (item.getItemId()){
                    case R.id.addToPlayList:
                        AddSongInPlayListVideoModel(videoModel,fragmentManager);
                        break;
                    case R.id.properties:
                        ShowProperties(videoModel,activity,context);
                        break;
                    case R.id.audioTrack:
                        DialogViewAudioTrake(activity,context);
                        break;
                    case R.id.share:
                        ShareFile(videoModel.getPath(),context,activity,videoModel.getSong_name());
                        break;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.video_play_menu);
        popupMenu.show();
    }


    public void AccessFilePermission(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                context.startActivity(intent);
                return;
            }
        }
    }


    /**
     * Rename File
     * @param path
     * @param context
     * @param activity
     */
    public void RenameFile(String path,Context context,Activity activity){

        final File[] dir = {new File(path)};
        String file= dir[0].getParent();
        String sub_name=path.substring(path.lastIndexOf("/")+1);
        String name= sub_name.substring(0,sub_name.lastIndexOf("."));



        String name_extantion=sub_name.substring(sub_name.lastIndexOf("."));

        AlertDialog.Builder GetName=new AlertDialog.Builder(context);
        View mView=activity.getLayoutInflater().inflate(R.layout.dialog_get_name,null);

        GetName.setView(mView);
        AlertDialog dialog=GetName.create();
        dialog.show();
        EditText renameText;
        Button r_cancel,r_ok;
        renameText=mView.findViewById(R.id.rename);
        renameText.setText(name);
        r_cancel=mView.findViewById(R.id.cancel);
        r_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        r_ok=mView.findViewById(R.id.r_ok);
        r_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=renameText.getText().toString()+name_extantion;
                if(dir[0].exists()){
                    File from = new File(file,sub_name);
                    File to = new File(file,newName);
                    if(from.exists()) {
                        boolean rename= from.renameTo(to);
                        if(rename) {
                            ContentResolver resolver = activity.getApplicationContext().getContentResolver();
                            resolver.delete(
                                    MediaStore.Files.getContentUri("external")
                                    , MediaStore.MediaColumns.DATA + "=?", new String[]{dir[0].getAbsolutePath()});
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(Uri.fromFile(to));
                            activity.getApplicationContext().sendBroadcast(intent);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    VideoDetailsFragment.getInstance().CheckUserPermsions();
                                    VideoDetailsFragment.getInstance().videoDetailsAdaptor.notifyDataSetChanged();
                                }
                            },200);

                        }
                    }
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * Share file
     */
    public void ShareFile(String path,Context context,Activity activity,String title){
        MediaScannerConnection.scanFile(activity, new String[] { path },
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT, title);
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_TITLE, title);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        context.startActivity(Intent.createChooser(shareIntent,
                                "Share"));
                    }
                });
    }

    /**
     * Delete File
     */
    public void DeleteFileConfermation(String path,Context context,Activity activity){
        AlertDialog.Builder deleteDialog=new AlertDialog.Builder(context);
        deleteDialog.setTitle("Delete video From Device?")
                .setMessage("Video will be deleted permanently.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VideoDetailsFragment.getInstance().NextSong();
                        DeleteFile(path,context,activity);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
    public void DeleteFile(String path,Context context,Activity activity){
        File file = new File(path);
        boolean deleted = file.delete();
        if(!deleted){
            boolean deleted2 = false;
            try {
                deleted2 = file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!deleted2){
                boolean deleted3 = context.deleteFile(file.getName());
            }
        }
        /**
         * scan media fill
         */
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        activity.startActivityForResult(intent, 1);
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(path)));
        context.sendBroadcast(scanIntent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Home.getInstance().get_All_Video_Song();
                VideoDetailsFragment.getInstance().CheckUserPermsions();
                VideoDetailsFragment.getInstance().videoDetailsAdaptor.notifyDataSetChanged();
            }
        },200);
    }


    /**
     * Show properties
     * @param activity
     * @param context
     */
    public void ShowProperties( VideoModel videoModel,Activity activity,Context context){
        String siz= GetFileSize(videoModel.getPath());
        String path=videoModel.getPath();
        String sList=path.substring(path.lastIndexOf("."));
        String name=videoModel.getSong_name();
        String format=sList;
        String duration;
        String date=FileLastModifyDate(path);
        ShowDialogProperties(context,activity,name,
                videoModel.getPath(), videoModel.getFolderName(), videoModel.getHour()+":"+
                        videoModel.getMunite()+":"+ videoModel.getSecond(),String.valueOf(siz),date,format);
    }

    /**
     * Get file size
     * @param selectedPath
     * @return
     */
    public String GetFileSize(String selectedPath){
        File file = new File(selectedPath);
        int file_size_kb = Integer.parseInt(String.valueOf(file.length()/1024));
        float file_size_mb=file_size_kb/1000;
        float file_size_gb=file_size_mb/1000;

        if(file_size_gb<1){
            return String.valueOf(file_size_mb+"MB");
        }else{

            return String.valueOf(file_size_gb+"GB");
        }
    }
    /**
     * Get file last modify date
     */
    public String FileLastModifyDate(String selectedPath){
        File file=new File(selectedPath);
        if (file.exists()) {
            Date lastModified = new Date(file.lastModified());
            return String.valueOf(lastModified);
        }else{
            return String.valueOf("");
        }

    }


    /**
     * Show Properties
     * @return
     */
    public void ShowDialogProperties(Context context,Activity activity,String Name, String path, String folder,
                                     String duration, String size, String date, String format){

        AlertDialog.Builder PropertiesShow=new AlertDialog.Builder(context);
        View mView=activity.getLayoutInflater().inflate(R.layout.dialog_item_properties,null);
        PropertiesShow.setView(mView);
        AlertDialog dialog=PropertiesShow.create();


        Button f_ok;
        TextView dname,dlocation,dfolder,dpath,dsize,dduration,length,ddate,formate;
        dname=mView.findViewById(R.id.file_name);
        ddate=mView.findViewById(R.id.date);
        dfolder=mView.findViewById(R.id.file_folder);
        dduration=mView.findViewById(R.id.file_length);
        dpath=mView.findViewById(R.id.file_location);
        dsize=mView.findViewById(R.id.file_siz);
        formate=mView.findViewById(R.id.file_foramt);
        f_ok=mView.findViewById(R.id.f_ok);
        f_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dname.setText(Name);
        ddate.setText(date);
        dfolder.setText(folder);
        dduration.setText(duration);
        dpath.setText(path);
        dsize.setText(size);
        formate.setText(format);
        dialog.show();

    }



}
