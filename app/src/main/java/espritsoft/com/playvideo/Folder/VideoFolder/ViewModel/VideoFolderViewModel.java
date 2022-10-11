package espritsoft.com.playvideo.Folder.VideoFolder.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import espritsoft.com.playvideo.Folder.FolderOpen.Repository.FolderOpenRepository;
import espritsoft.com.playvideo.Folder.FolderOpen.ViewModel.FolderOpenViewModel;
import espritsoft.com.playvideo.Folder.Model.FolderModul;
import espritsoft.com.playvideo.Folder.VideoFolder.Repositor.VideoFolderRepository;

public class VideoFolderViewModel extends AndroidViewModel {

    MutableLiveData<List<FolderModul>>videoFolder=new MutableLiveData<>();
    private VideoFolderRepository videoFolderRepository;
    public VideoFolderViewModel(@NonNull Application application) {
        super(application);
        videoFolderRepository=new VideoFolderRepository(application);
    }

    public void getPermission(){
        videoFolder=videoFolderRepository.getFolder();
    }

    public MutableLiveData<List<FolderModul>>getVideoFolder(){
        return videoFolder;
    }


}
