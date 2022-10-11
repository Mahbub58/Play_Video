package espritsoft.com.playvideo.DBManager.RepositoryHistoryDB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import espritsoft.com.playvideo.DBManager.Model.History;

@Database(entities = {History.class},version = 4)
public abstract class RoomDBHistory extends RoomDatabase {


    private static RoomDBHistory instance;

    public abstract HistoryDao daoHisoryDB();


    public static synchronized RoomDBHistory getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),RoomDBHistory.class,"History_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBacK)
                    .build();
        }



        return instance;
    }

    private static RoomDatabase.Callback roomCallBacK=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataAsyncTask(instance).execute();
        }
    };



    private static class PopulateDataAsyncTask extends AsyncTask<Void,Void,Void>{

        private HistoryDao daoHisoryDB;

        private PopulateDataAsyncTask(RoomDBHistory roomDBHistory){
            daoHisoryDB=roomDBHistory.daoHisoryDB();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
