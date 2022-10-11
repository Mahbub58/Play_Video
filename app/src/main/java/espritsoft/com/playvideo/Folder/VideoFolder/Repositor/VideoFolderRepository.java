package espritsoft.com.playvideo.Folder.VideoFolder.Repositor;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import espritsoft.com.playvideo.Folder.Model.FolderModul;
import espritsoft.com.playvideo.Video.VideoModel;

public class VideoFolderRepository {

    private ArrayList<FolderModul>videoFolder=new ArrayList<>();
    private ArrayList<FolderModul>sub_videoFolder=new ArrayList<>();
    Context context;
    public VideoFolderRepository(Application application) {
        context=application;
    }

    public MutableLiveData<List<FolderModul>>getFolder(){
        videoFolder.clear();
        sub_videoFolder.clear();
        getSubFolderLis();
        getvideoFolderList();

        MutableLiveData<List<FolderModul>>data=new MutableLiveData<>();
        data.setValue(videoFolder);
        return data;
    }

    private void getSubFolderLis() {
        Uri allsonguri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        // String selection = MediaStore.Video.Media. + "!=0";
        String orderBy=MediaStore.Images.Media.DATE_MODIFIED;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA};

        Cursor cursor =context.getApplicationContext().getContentResolver().query(allsonguri, null, null, null, orderBy+" DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumArtUriImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                    /** Duration**/
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
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

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    //  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    //  String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folderName=recpints[recpints.length-2];
                    sub_videoFolder.add(new FolderModul(folderName,albumArtUriImage,"1"));
                    //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
                } while (cursor.moveToNext());
            }
            cursor.close();


        }
    }

    /**folder colect **/
    int totalV=1;
    public void getvideoFolderList(){
        try {
            Collections.sort(sub_videoFolder, new Comparator<FolderModul>() {
                @Override
                public int compare(FolderModul o1, FolderModul o2) {
                    return o1.getFolderName().compareTo(o2.getFolderName());
                }
            });
        }catch (Exception e){
            Log.e("error=",""+e);
        }

        if(sub_videoFolder.size()==1){
            int p=0;
            videoFolder.add(new FolderModul(sub_videoFolder.get(p).getFolderName(),sub_videoFolder.get(p).getCover(),String.valueOf(1)));
        }
        for (int i = 1; i < sub_videoFolder.size(); i++) {
            String a1 = sub_videoFolder.get(i).getFolderName();
            String cover=sub_videoFolder.get(i-1).getCover();
            String a2 = sub_videoFolder.get(i-1).getFolderName();

            if (!a1.equals(a2)) {
                videoFolder.add(new FolderModul(a2,cover,String.valueOf(totalV)));

                if(i==sub_videoFolder.size()-1){
                    videoFolder.add(new FolderModul(sub_videoFolder.get(i).getFolderName(),sub_videoFolder.get(i).getCover(),String.valueOf(1)));
                }
                totalV=1;
            }else totalV++;
            if (a1.equals(a2))
                if(i==sub_videoFolder.size()-1){
                    videoFolder.add(new FolderModul(sub_videoFolder.get(i).getFolderName(),sub_videoFolder.get(i).getCover(),String.valueOf(totalV)));
                }
        }
    }

}
