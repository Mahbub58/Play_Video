package espritsoft.com.playvideo.DBManager.RepositoryHistoryDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import espritsoft.com.playvideo.DBManager.Model.History;
@Dao
public interface HistoryDao {
        @Insert
        long insert(History dataModelHistiory);
        @Update
        void update(History dataModelHistiory);
        @Delete
        void delete(History dataModelHistiory);
        @Query("DELETE FROM History_Table")
        void deleteAllHistory();


        @Query("SELECT *FROM History_Table ORDER BY id DESC")
        LiveData<List<History>> getAllHistory();
}
