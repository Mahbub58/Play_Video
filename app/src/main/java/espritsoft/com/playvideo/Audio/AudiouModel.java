package espritsoft.com.playvideo.Audio;

public class AudiouModel {
    public String Path;
    public String song_name;
    public String albam_name;
    public String artist_name;
    public String albumArtUriImage;
    public String second;
    public String munite;
    public String hour;
    public String folderName;

    public AudiouModel() {
    }

    public AudiouModel(String path, String song_name, String albam_name, String artist_name, String albumArtUriImage, String second, String munite, String hour,String folderName) {
        Path = path;
        this.song_name = song_name;
        this.albam_name = albam_name;
        this.artist_name = artist_name;
        this.albumArtUriImage = albumArtUriImage;
        this.second = second;
        this.munite = munite;
        this.hour = hour;
        this.folderName=folderName;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getAlbam_name() {
        return albam_name;
    }

    public void setAlbam_name(String albam_name) {
        this.albam_name = albam_name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getAlbumArtUriImage() {
        return albumArtUriImage;
    }

    public void setAlbumArtUriImage(String albumArtUriImage) {
        this.albumArtUriImage = albumArtUriImage;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMunite() {
        return munite;
    }

    public void setMunite(String munite) {
        this.munite = munite;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
