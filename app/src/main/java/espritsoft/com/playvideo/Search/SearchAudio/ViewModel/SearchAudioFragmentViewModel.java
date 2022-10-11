package espritsoft.com.playvideo.Search.SearchAudio.ViewModel;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.Audio.ViewModel.AudioFragmentViewModel;
import espritsoft.com.playvideo.Search.SearchVideo.ViewModel.SearchViewVideoViewModel;

public class SearchAudioFragmentViewModel extends AndroidViewModel {

    private SearchViewVideoViewModel searchViewVideoViewModel;
    private AudioFragmentViewModel audioFragmentViewModel;
    static Context context;

    public SearchAudioFragmentViewModel(@NonNull Application application) {
        super(application);
        context=application;
        searchViewVideoViewModel=new SearchViewVideoViewModel(application);
        audioFragmentViewModel=new AudioFragmentViewModel(application);

    }


    public void openKeyboard() {
        searchViewVideoViewModel.openKeyboard();
    }

    public void closeKeyboard(View view) {
        searchViewVideoViewModel.closeKeyboard(view);
    }

    public void getAllAudioSong(ArrayList arrayList) {
        String orderBy= MediaStore.Images.Media.DATE_MODIFIED;
        Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = context.getContentResolver().query(allsonguri, null, selection, null, orderBy+" DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int duration1=0;
                    try {
                        duration1 = Integer.parseInt(duration);
                    }catch (Exception e){ }
                    int duration2 = duration1 / 1000;
                    int hour1 = duration1/3600;
                    int minute1 = duration2 % 60;
                    int second1 = duration2 / 60;
                    String second = String.valueOf(second1);
                    String minute = String.valueOf(minute1);
                    String hour = String.valueOf(hour1);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];
                    if(duration1>0.1)
                        arrayList.add(new AudiouModel(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second,hour,folderName));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

    public void GetQueuSongList(String folderName) {
        audioFragmentViewModel.GetQueuSongList(folderName);
    }
}
