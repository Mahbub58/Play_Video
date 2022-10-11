package espritsoft.com.playvideo.Folder.AudioFolder.Repository;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.Folder.Model.FolderModul;

public class AudioFolderRepository {

    private ArrayList<FolderModul>audioFolder=new ArrayList<>();
    private ArrayList<FolderModul>sub_audioFolder=new ArrayList<>();

    Context context;
    public AudioFolderRepository(Application application) {
        context=application;
    }


    public MutableLiveData<List<FolderModul>>getFolder(){
        audioFolder.clear();
        getFolderList();
        SortedFolderList();

        MutableLiveData<List<FolderModul>> data=new MutableLiveData<>();
        data.setValue(audioFolder);
        return data;
    }

    private void getFolderList() {
        sub_audioFolder.clear();
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

                    if(duration1>0.0)
                        sub_audioFolder.add(new FolderModul(folderName,albumArtUriImage,"1"));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }


    /**folder colect **/

//================== Find Audio Folder
    int totalA=1;
    public void SortedFolderList(){
        totalA=1;
        //clear
        audioFolder.clear();

        Collections.sort(sub_audioFolder, new Comparator<FolderModul>() {
            @Override
            public int compare(FolderModul o1, FolderModul o2) {
                return o1.getFolderName().compareTo(o2.getFolderName());
            }

        });

        if(sub_audioFolder.size()==1){
            int p=0;
            audioFolder.add(new FolderModul(sub_audioFolder.get(p).getFolderName(),sub_audioFolder.get(p).getCover(),String.valueOf(1)));
        }

        for (int i = 1; i < sub_audioFolder.size(); i++) {
            String a1 = sub_audioFolder.get(i).getFolderName();
            String cover=sub_audioFolder.get(i-1).getCover();
            String a2 = sub_audioFolder.get(i-1).getFolderName();

            if (!a1.equals(a2)) {
                audioFolder.add(new FolderModul(a2,cover,String.valueOf(totalA)));

                if(i==sub_audioFolder.size()-1){
                    audioFolder.add(new FolderModul(sub_audioFolder.get(i).getFolderName(),sub_audioFolder.get(i).getCover(),String.valueOf(1)));
                }
                totalA=1;
            }else totalA++;

            if (a1.equals(a2))
                if(i==sub_audioFolder.size()-1){
                    audioFolder.add(new FolderModul(sub_audioFolder.get(i).getFolderName(),sub_audioFolder.get(i).getCover(),String.valueOf(totalA)));
                }
        }
    }


}
