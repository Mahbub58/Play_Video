package espritsoft.com.playvideo.DBManager.RepositoryHistoryDB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import espritsoft.com.playvideo.DBManager.Model.History;

public class RoomDbHistoryRepository {
    private HistoryDao daoHisoryDB;
    private LiveData<List<History>>allHistory;

     public static int id;
    public RoomDbHistoryRepository(Application application){
        RoomDBHistory roomDBHistory=RoomDBHistory.getInstance(application);
        daoHisoryDB= roomDBHistory.daoHisoryDB();
        allHistory=daoHisoryDB.getAllHistory();
    }

    public void insert(History dataModelHistiory){
        new InsertDaraAsyncTask(daoHisoryDB).execute(dataModelHistiory);
    }
    public int getId(){
        return id;
    }

    public void upddate(History dataModelHistiory){
        new UpdateDataAsyncTask(daoHisoryDB).execute(dataModelHistiory);
    }
    public void delete(History dataModelHistiory){
        new DeleteDataAsyncTask(daoHisoryDB).execute(dataModelHistiory);
    }
    public void deleteAllHistory(){
        new DeleteAllAsyncTask(daoHisoryDB).execute();
    }
    public LiveData<List<History>>getAllHistory(){
        return allHistory;
    }


    private static class InsertDaraAsyncTask extends AsyncTask<History, Void, Void> {
        private HistoryDao daoHisoryDB;
        private InsertDaraAsyncTask(HistoryDao daoHisoryDB){
            this.daoHisoryDB=daoHisoryDB;
        }

        @Override
        protected Void doInBackground(History... dataModelHistiories) {
         //  daoHisoryDB.insert(dataModelHistiories[0]);
           id= (int) daoHisoryDB.insert(dataModelHistiories[0]);
            return null;
        }

    }

    private static class UpdateDataAsyncTask extends AsyncTask<History,Void, Void>{
        private HistoryDao daoHisoryDB;

        private UpdateDataAsyncTask(HistoryDao daoHisoryDB){
            this.daoHisoryDB=daoHisoryDB;
        }


        @Override
        protected Void doInBackground(History... dataModelHistiories) {
            daoHisoryDB.update(dataModelHistiories[0]);
            return null;
        }
    }

    private static class DeleteDataAsyncTask extends AsyncTask<History,Void,Void>{
        private HistoryDao daoHisoryDB;

        private DeleteDataAsyncTask(HistoryDao daoHisoryDB){
            this.daoHisoryDB=daoHisoryDB;
        }

        @Override
        protected Void doInBackground(History... dataModelHistiories) {
            daoHisoryDB.delete(dataModelHistiories[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<History,Void,Void>{
        private HistoryDao daoHisoryDB;

        private DeleteAllAsyncTask(HistoryDao daoHisoryDB){
            this.daoHisoryDB=daoHisoryDB;
        }

        @Override
        protected Void doInBackground(History... dataModelHistiories) {
            daoHisoryDB.deleteAllHistory();
            return null;
        }
    }
}
