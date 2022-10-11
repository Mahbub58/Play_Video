package espritsoft.com.playvideo.Folder.FolderOpen.Repository;

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
import espritsoft.com.playvideo.Folder.FolderOpen.Model.FolderItemModel;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsModel;

public class FolderOpenRepository {

    private ArrayList<FolderItemModel>folderItem=new ArrayList<>();
    private ArrayList<VideoModel> videoDataByFolder=new ArrayList<>();
    Context context;
    public FolderOpenRepository(Application application) {
       context=application;
    }

    public MutableLiveData<List<FolderItemModel>> getFolderItem(boolean isAudio,String folderName){
        folderItem.clear();
        getItem(isAudio,folderName);

        MutableLiveData<List<FolderItemModel>>data=new MutableLiveData<>();
        data.setValue(folderItem);
        return data;
    }

    private void getItem(boolean isAudio,String seclectedFolder) {
        if(isAudio){
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
                        }catch (Exception e){}
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
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                        String albumArtUriImage = albumArtUri.toString();

                        //find folder
                        String  recpintList=fullpath;
                        String[] recpints=(recpintList.split("/"));
                        String folderName=recpints[recpints.length-2];
                        if(folderName.equals(seclectedFolder)) {
                            if (duration1 > 0.0)
                                folderItem.add(new FolderItemModel(fullpath, song_name, album_name,artist_name, albumArtUriImage, second, minute, hour, folderName));
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        }else{
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
                        String folder_Name=recpints[recpints.length-2];


                        if(seclectedFolder.equals(folder_Name)) {
                            folderItem.add(new FolderItemModel(fullpath, song_name, album_name,artist_name, albumArtUriImage, second, minute, hour, folder_Name));
                        }
                        //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
    }

    public MutableLiveData<List<VideoModel>> getVideoByFolder(String folderName) {
        videoDataByFolder.clear();
        getVideoListByFolder(folderName);

        MutableLiveData<List<VideoModel>>data=new MutableLiveData<>();
        data.setValue(videoDataByFolder);
        return data;
    }

    private void getVideoListByFolder(String folderName) {
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
                    int duration2 = duration1 / 1000;
                    int hour1=duration2/3600;
                    int minute1 = duration2 % 60;
                    int second1 = duration2 / 60;
                    String second = String.valueOf(second1);
                    String minute = String.valueOf(minute1);
                    String hour =String.valueOf(hour1);
                    /**AlbumArt**/

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    //  Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumId));

                    //  String albumArtUriImage = albumArtUri.toString();

                    //find folder
                    String  recpintList=fullpath;
                    String[] recpints=(recpintList.split("/"));
                    String folder_Name=recpints[recpints.length-2];


                    if(folderName.equals(folder_Name)) {
                        videoDataByFolder.add(new VideoModel(fullpath, song_name, album_name,folderName, artist_name, albumArtUriImage, second, minute,hour));
                    }
                    //    SuffleList.add(new customItem(fullpath, song_name, album_name, artist_name, albumArtUriImage, minute, second));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

}
