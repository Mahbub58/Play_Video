package espritsoft.com.playvideo.VideoPlay.AudioTracks;

public class trackModel {

    String track;
    int indeg;

    public trackModel(String track, int indeg) {
        this.track = track;
        this.indeg = indeg;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getIndeg() {
        return indeg;
    }

    public void setIndeg(int indeg) {
        this.indeg = indeg;
    }
}
