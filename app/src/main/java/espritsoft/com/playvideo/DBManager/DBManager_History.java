package espritsoft.com.playvideo.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DBManager_History {
    private SQLiteDatabase sqlDB;
    static final String DBName = "PlayVideo";
    static final String TableName = "History";
    static final String TableName2 = "PlayList";
    public static final String playl_list_name = "playl_list_name";
    public static final String song_name = "song_name";
    public static final String song_folder = "song_folder";
    public static final String song_album = "Song_album";
    public static final String song_artist = "song_artist";
    public static final String song_duration ="song_duration";
    public static final String song_path = "song_path";
    public static final String song_album_cover = "song_album_cover";

    public static final String ColID = "ID";
    static final int DBVersion = 1;
    //create table Logine(ID integer primary key autoincrment,UserName text,Passwoord text)

    static final String CreateTable = "Create table IF NOT EXISTS " + TableName +
            "(ID integer PRIMARY KEY AUTOINCREMENT," + song_name + " text,"+ song_folder + " text," + song_album + " text," + song_artist + " text," + song_duration + " text," + song_album_cover + " text," + song_path + " text );";

    static final String CreateTable2 = "Create table IF NOT EXISTS " + TableName2 +
            "(ID integer PRIMARY KEY AUTOINCREMENT," + song_name + " text,"+ song_folder + " text," + song_album + " text," + song_artist + " text," + song_duration + " text," + song_album_cover + " text," + song_path +  " text," + playl_list_name +" text );";


    static class DatabaseHelperUser extends SQLiteOpenHelper {
        Context context;

        DatabaseHelperUser(Context context) {
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
            db.execSQL(CreateTable2);
            Toast.makeText(context, "Table is created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("Drop table IF EXISTS " + TableName);
            db.execSQL("Drop table IF EXISTS " + TableName2);
            onCreate(db);
        }
    }


    public DBManager_History(Context context) {
        DatabaseHelperUser db = new DatabaseHelperUser(context);
        sqlDB = db.getWritableDatabase();
    }

    public long Insert(ContentValues values) {
        long ID = sqlDB.insert(TableName, "", values);

        return ID;
    }
    public long Insert_table2(ContentValues values) {
        long ID2 = sqlDB.insert(TableName2, "", values);

        return ID2;
    }

    //select user name ,password from Logins where ID=1;
    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);


        Cursor cursor = qb.query(sqlDB, Projection, Selection, SelectionArgs, null, null, SortOrder);
        return cursor;
    }
    //select user name ,password from Logins where ID=1;
    public Cursor query2(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName2);


        Cursor cursor = qb.query(sqlDB, Projection, Selection, SelectionArgs, null, null, SortOrder);
        return cursor;
    }

    public int Delet(String Selection, String[] SelectionArgs){
        int count=sqlDB.delete(TableName,Selection,SelectionArgs);
        return count;
    }

    public int Delet2(String Selection, String[] SelectionArgs){
        int count=sqlDB.delete(TableName2,Selection,SelectionArgs);
        return count;
    }

    public int Update(ContentValues values, String Selection, String[] SelectionArgs){
        int count=sqlDB.update(TableName,values,Selection,SelectionArgs);
        return count;
    }


}