package espritsoft.com.playvideo.Audio.Ripository;

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

public class AudioFragmentRepository {

    private ArrayList<AudiouModel> AudioData=new ArrayList();
    Context context;

    public AudioFragmentRepository(Application application) {

        context=application;
    }


    public MutableLiveData<List<AudiouModel>>getAudioData(){
        AudioData.clear();
        getAllAudio();

        MutableLiveData<List<AudiouModel>>Data=new MutableLiveData<>();
        Data.setValue(AudioData);
        return Data;
    }

    private void getAllAudio() {
        String orderBy= MediaStore.Images.Media.DATE_MODIFIED;
        Uri allsonguri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor =context.getContentResolver().query(allsonguri, null, selection, null, orderBy+" DESC");
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
                    /**
                     * video time collect
                     */
                    int seconds = (int) (duration1 / 1000) % 60 ;
                    int minutes = (int) ((duration1 / (1000*60)) % 60);
                    int hours=(int) ((duration1 / (1000*60*60)) % 24);
                    String second = String.valueOf(seconds);
                    String minute = String.valueOf(minutes);
                    String hour =String.valueOf(hours);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];

                    if(duration1>0.0)
                        AudioData.add(new AudiouModel(fullpath, song_name, album_name, artist_name, albumArtUriImage, second, minute,hour,folderName));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }


}
