package espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList;

public class ModulPlayListItem {
   public String playListName;
    public String playListCover;
    public String playListTotal;
    public String playList_song_path;
    public String song_name;


    public ModulPlayListItem() {
    }

    public ModulPlayListItem(String playListName, String playListCover, String playListTotal, String playList_song_path, String song_name) {
        this.playListName = playListName;
        this.playListCover = playListCover;
        this.playListTotal = playListTotal;
        this.playList_song_path = playList_song_path;
        this.song_name = song_name;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public String getPlayListCover() {
        return playListCover;
    }

    public void setPlayListCover(String playListCover) {
        this.playListCover = playListCover;
    }

    public String getPlayList_song_path() {
        return playList_song_path;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public void setPlayList_song_path(String playList_song_path) {
        this.playList_song_path = playList_song_path;
    }

    public String getPlayListTotal() {
        return playListTotal;
    }

    public void setPlayListTotal(String playListTotal) {
        this.playListTotal = playListTotal;
    }
}
