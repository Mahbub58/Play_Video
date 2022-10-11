package espritsoft.com.playvideo.HomeActivity.Repository;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsModel;

public class HomeRepository {

    Context context;
  //  private ArrayList<AudiouModel> VideoByFolderQueList=new ArrayList<>();

    public HomeRepository(Application application) {

        context=application;
    }



}
