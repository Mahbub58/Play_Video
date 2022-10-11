package espritsoft.com.playvideo.DBManager.Model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "History_Table")
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;


    public String songName;
    public String album;
    public String folder;
    public String artist;
    public String albumCover;
    public String path;
    public String duration;
    public String progress;
    //@ColumnInfo(name = "priority_column")
   // private String priority;


    public History(String name, String album, String folder, String artist, String albumCover, String path, String duration, String pogress) {
        this.songName = name;
        this.album = album;
        this.folder = folder;
        this.artist = artist;
        this.albumCover = albumCover;
        this.path = path;
        this.duration = duration;
        this.progress = pogress;
    }

    public History() {
    }

    public String getProgress() {
        return progress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return songName;
    }

    public String getAlbum() {
        return album;
    }

    public String getFolder() {
        return folder;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }
}
