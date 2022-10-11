package espritsoft.com.playvideo.HistoryLibrary.ViewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.Repository.AudioPlayRepositor;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.DBManager.RepositoryHistoryDB.RoomDbHistoryRepository;
import espritsoft.com.playvideo.HistoryLibrary.HistoryFragment;
import espritsoft.com.playvideo.HistoryLibrary.PlayList_open.PlayListOpenFragment;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoModel;

public class HistoryFragmentViewModel extends AndroidViewModel {

    private RoomDbHistoryRepository roomDbHistoryRepository;

    private LiveData<List<History>>allHistory;
    AudioPlayRepositor audioPlayRepositor;


    public HistoryFragmentViewModel(@NonNull Application application) {
        super(application);
        roomDbHistoryRepository=new RoomDbHistoryRepository(application);
        allHistory=roomDbHistoryRepository.getAllHistory();
        audioPlayRepositor=new AudioPlayRepositor(application);

    }

    public LiveData<List<History>>getHistory(){
        return allHistory;
    }

    public void insert(History dataModelHistiory){
        roomDbHistoryRepository.insert(dataModelHistiory);
    }
    public void update(History dataModelHistiory){
        roomDbHistoryRepository.upddate(dataModelHistiory);
    }
    public void delete(History dataModelHistiory){
        roomDbHistoryRepository.delete(dataModelHistiory);
    }
    public void deleteAll( ){
        roomDbHistoryRepository.deleteAllHistory();
    }
    public LiveData<List<History>>getAllHistory(){
        return allHistory;
    }

    public void getAudioQueList(){
        AudioPlaySystem.getInstance().queueList.clear();
        for(int i=0;i<allHistory.getValue().size();i++){
            String hour="00",minut="00",second="00";
            try{
                String time=allHistory.getValue().get(i).getDuration();
                String[] subTime=time.split(":");
                hour=subTime[subTime.length-2];
                minut=subTime[subTime.length-1];
                second=subTime[subTime.length];
            }catch (Exception e){}
            AudioPlaySystem.getInstance().queueList.add(new AudiouModel(allHistory.getValue().get(i).getPath(),allHistory.getValue().get(i).getName(),
                    allHistory.getValue().get(i).getAlbum(),allHistory.getValue().get(i).getArtist(),allHistory.getValue().get(i).getAlbumCover(),second,minut,hour,
                    allHistory.getValue().get(i).getFolder()));
        }

    }
    public void getVideoQueList(){
        AudioPlaySystem.getInstance().VideoQueList.clear();
        for(int i=0;i<allHistory.getValue().size();i++){
            String hour="00",minut="00",second="00";
            try{
                String time=allHistory.getValue().get(i).getDuration();
                String[] subTime=time.split(":");
                hour=subTime[subTime.length-2];
                minut=subTime[subTime.length-1];
                second=subTime[subTime.length];
            }catch (Exception e){}
            String pth=allHistory.getValue().get(i).getPath();
            String formate=pth.substring(pth.lastIndexOf("."));
            if(!formate.equals(".mp3")) {
                AudioPlaySystem.getInstance().VideoQueList.add(new VideoModel(allHistory.getValue().get(i).getPath(),
                        allHistory.getValue().get(i).getName(), allHistory.getValue().get(i).getAlbum(),
                        allHistory.getValue().get(i).getFolder(), allHistory.getValue().get(i).getArtist(),
                        allHistory.getValue().get(i).getAlbumCover(), second, minut, hour));
            }
        }

    }
    public void ItemMenu(Context context, View v, History history){
        PopupMenu menu=new PopupMenu(context,v);
        menu.inflate(R.menu.history_pay_ist_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.delete:
                        delete(history);
                        break;
                }
                return false;
            }
        });
        menu.show();
    }


    public void confirmDialog(Context context) {
        AlertDialog.Builder cmfDialog=new AlertDialog.Builder(context);
        cmfDialog.setTitle("Confirmation")
                .setMessage("Recent will be deleted.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAll();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
