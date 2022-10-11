package espritsoft.com.playvideo.HistoryLibrary.PlayList_open.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.DBManager.RepositoryHistoryDB.RoomDbHistoryRepository;
import espritsoft.com.playvideo.HistoryLibrary.HistoryFragment;
import espritsoft.com.playvideo.HistoryLibrary.PlayList_open.PlayListOpenFragment;
import espritsoft.com.playvideo.R;

public class PlayListOpenViewModel extends AndroidViewModel {

    public static String RecordID;
    PlayListOpenViewModel playListOpenViewModel;
    public static DBManager_History dbManager;
    RoomDbHistoryRepository roomDbHistoryRepository;

    public PlayListOpenViewModel(@NonNull Application application) {
        super(application);
        dbManager = new DBManager_History(application);
        roomDbHistoryRepository=new RoomDbHistoryRepository(application);
    }

    //room db
    public void insertHistory(History history) {
        roomDbHistoryRepository.insert(history);
    }
    public int getId(){
        return roomDbHistoryRepository.getId();
    }



    public void ItemMenu(Context context, View v,String itemName){
        PopupMenu menu=new PopupMenu(context,v);
        menu.inflate(R.menu.history_pay_ist_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch (id){
                    case R.id.delete:
                       deleteFromDB(itemName);
                       break;
                }
                return false;
            }
        });
        menu.show();
    }

    public void deleteFromDB(String name){
        String[] SelectionArgss = new String[]{name};
        int count = dbManager.Delet2("song_name=?", SelectionArgss);
        //refresh Element
        if (count > 0) {
            //  checkPlayList();
            PlayListOpenFragment.getInstance().LoadElement();
            PlayListOpenFragment.getInstance().historyAdaptor.notifyDataSetChanged();
            HistoryFragment.getInstance().CollectPlayList();
            HistoryFragment.getInstance().playListItemAdaptor.notifyDataSetChanged();
            Log.d("tass", "==========Delete");
        }
    }

}
