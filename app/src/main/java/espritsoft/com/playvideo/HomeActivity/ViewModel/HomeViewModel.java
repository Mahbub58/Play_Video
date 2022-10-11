package espritsoft.com.playvideo.HomeActivity.ViewModel;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.HomeActivity.Repository.HomeRepository;

public class HomeViewModel extends AndroidViewModel {


    public  HomeRepository homeRepository;
    Context context;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository=new HomeRepository(application);
        context=application;
    }

    public void GetQueuSonglisr(){

    }

    public void AccessFilePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                context.startActivity(intent);
                return;
            }
        }
    }

    //Screen Mode Chenge
    public void ScreenPotraitMode(Activity activity){
        int orientation =activity.getResources().getConfiguration().orientation;
        switch(orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                // getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
//                Home.getInstance().forcelyActivetedLandspeceMood=true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER);
                break;
        }
    }



}
