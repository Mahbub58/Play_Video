package espritsoft.com.playvideo.Search.SearchVideo.ViewModel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.RepositoryHistoryDB.RoomDbHistoryRepository;
import espritsoft.com.playvideo.HomeActivity.ViewModel.HomeViewModel;
import espritsoft.com.playvideo.Search.SearchVideo.SearchVideoRepository;
import espritsoft.com.playvideo.Video.Repository.VideoFragmentRepository;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoPlay.ViewModel.VideoPlayViewModel;

public class SearchViewVideoViewModel extends AndroidViewModel {

    MutableLiveData<List<VideoModel>> videoData=new MutableLiveData<>();
    MutableLiveData<List<VideoModel>>videoByFolder=new MutableLiveData<>();
    VideoPlayViewModel videoPlayViewModel;
    SearchVideoRepository searchVideoRepository;
    RoomDbHistoryRepository roomDbHistoryRepository;
    Context context;
    public SearchViewVideoViewModel(@NonNull Application application) {
        super(application);
        searchVideoRepository=new SearchVideoRepository(application);
        context=application;
    }


    public void closeKeyboard(View view) {
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    context.getSystemService(
                            context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
    public void openKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

//    public void getAllVideoSong(ArrayList arrayList){
//            Uri allsonguri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//            // String selection = MediaStore.Video.Media. + "!=0";
//            String orderBy=MediaStore.Images.Media.DATE_MODIFIED;
//
//            String[] projection = {MediaStore.MediaColumns.DATA,
//                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//                    MediaStore.Video.Media._ID,
//                    MediaStore.Video.Thumbnails.DATA};
//
//            Cursor cursor =context.getApplicationContext().getContentResolver().query(allsonguri, null, null, null, orderBy+" DESC");
//
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        String albumArtUriImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
//                        String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
//                        String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                        String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
//                        String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
//                        /** Duration**/
//                        String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
//                        int duration1=0;
//                        try {
//                            duration1 = Integer.parseInt(duration);
//                        }catch (Exception e){ }
//                        int duration2 = duration1 / 1000;
//                        int hour1=duration2/3600;
//                        int minute1 = duration2 % 60;
//                        int second1 = duration2 / 60;
//                        String second = String.valueOf(second1);
//                        String minute = String.valueOf(minute1);
//                        String hour =String.valueOf(hour1);
//                        /**AlbumArt**/
//
//                        Uri sArtworkUri = Uri
//                                .parse("content://media/external/audio/albumart");
//                        //  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));
//
//                        //  String albumArtUriImage = albumArtUri.toString();
//
//                        //find folder
//                        String  recpintList=fullpath;
//                        String[] recpints=(recpintList.split("/"));
//                        String folderName=recpints[recpints.length-2];
//
//
//                        arrayList.add(new VideoModel(fullpath, song_name, album_name,folderName, artist_name, albumArtUriImage, minute, second,hour));
//                        //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//
//
//            }
//
//
//    }



    public void getpermission(){
        videoData=searchVideoRepository.getVideoData();
    }

    public LiveData<List<VideoModel>> getVideo(){
        return videoData;
    }


    public void getDataByFolder(String folderName){
        videoByFolder =searchVideoRepository.getVideoByFolder(folderName);
    }
    public void getVideoQueList(){
        AudioPlaySystem.getInstance().VideoQueList.clear();
        for(int i=0;i<videoByFolder.getValue().size();i++){
            AudioPlaySystem.getInstance().VideoQueList.add(new VideoModel(videoByFolder.getValue().get(i).getPath(), videoByFolder.getValue().get(i).getSong_name(), videoByFolder.getValue().get(i).getAlbam_name(),videoByFolder.getValue().get(i).getFolderName(), videoByFolder.getValue().get(i).getArtist_name(), videoByFolder.getValue().get(i).getAlbumArtUriImage(), videoByFolder.getValue().get(i).getSecond(),videoByFolder.getValue().get(i).getMunite(),videoByFolder.getValue().get(i).getHour()));
        }

    }

}
