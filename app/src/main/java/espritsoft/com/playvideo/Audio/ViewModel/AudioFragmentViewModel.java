package espritsoft.com.playvideo.Audio.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.Audio.AudioFragment;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.Audio.Ripository.AudioFragmentRepository;
import espritsoft.com.playvideo.AudioPlay.Repository.AudioPlayRepositor;
import espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList.PlayListFragment;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.HomeActivity.Repository.HomeRepository;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoFragment;
import espritsoft.com.playvideo.Video.VideoModel;

public class AudioFragmentViewModel extends AndroidViewModel {

    private HomeRepository homeRepository;
    private AudioPlayRepositor audioPlayRepositor;
    Context context;
    private MutableLiveData<List<AudiouModel>>AudioData=new MutableLiveData<>();
    private AudioFragmentRepository audioFragmentRepository;

    public AudioFragmentViewModel(@NonNull Application application) {
        super(application);
        context=application;
        homeRepository=new HomeRepository(application);
        audioPlayRepositor=new AudioPlayRepositor(application);
        audioFragmentRepository=new AudioFragmentRepository(application);
    }


    public void getPermission(){
        AudioData=audioFragmentRepository.getAudioData();
    }

    public LiveData<List<AudiouModel>>getAudio(){
        return AudioData;
    }



    public void GetQueuSongList(String folderName){
        audioPlayRepositor.getAudioQueList(folderName);
    }


    /**
     * song menu more
     * @param context
     * @param view
     * @param fragmentManager
     */
    public void SongMenuMore(AudiouModel audiouModel,Context context,Activity activity, View view, FragmentManager fragmentManager){
        PopupMenu popupMenu=new PopupMenu(context,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AccessFilePermission(context);
                switch (item.getItemId()){
                    case R.id.addToPlayList:
                        AddSongInPlayList(audiouModel,fragmentManager);
                        break;
                    case R.id.properties:
                        ShowProperties(audiouModel,activity,context);
                        break;
                    case R.id.rename:
                        RenameFile(audiouModel.getPath(),context,activity);
                        break;
                    case R.id.share:
                        ShareFile(audiouModel.getPath(),context,activity,audiouModel.getSong_name());
                        break;
                    case R.id.delete:
                        DeleteFileConfermation(audiouModel.getPath(),context,activity);
                        break;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.song_mor_menu);
        popupMenu.show();
    }

    public void AddSongInPlayList(AudiouModel videoModel, FragmentManager fragmentManager){
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
        EditText renameText;Button r_cancel,r_ok;
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
                                    getPermission();
                                  AudioFragment.getInstance().audioAdaptor.notifyDataSetChanged();
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
                        shareIntent.setType("audio/*");
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
                Home.getInstance().get_All_Video_Song();
                VideoFragment.getInstance().videoAdaptor.notifyDataSetChanged();
            }
        },200);
    }


    /**
     * Show properties
     * @param activity
     * @param context
     */
    public void ShowProperties( AudiouModel audiouModel,Activity activity,Context context){
        String siz= GetFileSize(audiouModel.getPath());
        String path=audiouModel.getPath();
        String sList=path.substring(path.lastIndexOf("."));
        String name=audiouModel.getSong_name();
        String format=sList;
        String duration;
        String date=FileLastModifyDate(path);
        ShowDialogProperties(context,activity,name,
                audiouModel.getPath(), audiouModel.getFolderName(), audiouModel.getHour()+":"+
                        audiouModel.getMunite()+":"+ audiouModel.getSecond(),String.valueOf(siz),date,format);
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
