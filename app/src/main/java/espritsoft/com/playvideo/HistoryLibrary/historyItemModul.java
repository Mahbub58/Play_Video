package espritsoft.com.playvideo.HistoryLibrary;

public class historyItemModul {
    public String song_name ;
    public String song_album;
    public String song_folder;
    public String song_path;
    public String song_artist;
    public String song_duration;
    public String song_album_cover;


    public historyItemModul( String song_name, String song_album, String song_folder, String song_path, String song_artist, String song_duration, String song_album_cover) {
        this.song_name = song_name;
        this.song_album = song_album;
        this.song_folder = song_folder;
        this.song_path = song_path;
        this.song_artist = song_artist;
        this.song_duration = song_duration;
        this.song_album_cover = song_album_cover;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_album() {
        return song_album;
    }

    public void setSong_album(String song_album) {
        this.song_album = song_album;
    }

    public String getSong_folder() {
        return song_folder;
    }

    public void setSong_folder(String song_folder) {
        this.song_folder = song_folder;
    }

    public String getSong_path() {
        return song_path;
    }

    public void setSong_path(String song_path) {
        this.song_path = song_path;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public String getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(String song_duration) {
        this.song_duration = song_duration;
    }

    public String getSong_album_cover() {
        return song_album_cover;
    }

    public void setSong_album_cover(String song_album_cover) {
        this.song_album_cover = song_album_cover;
    }
}
