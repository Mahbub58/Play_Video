package espritsoft.com.playvideo.Folder.Model;

public class FolderModul {
    String FolderName="unknown";
    String Cover="unknown";
    String TotalItem="unknown";

    public FolderModul() {
    }

    public FolderModul(String folderName, String cover, String totalItem) {
        FolderName = folderName;
        Cover = cover;
        TotalItem = totalItem;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    public String getTotalItem() {
        return TotalItem;
    }

    public void setTotalItem(String totalItem) {
        TotalItem = totalItem;
    }
}
