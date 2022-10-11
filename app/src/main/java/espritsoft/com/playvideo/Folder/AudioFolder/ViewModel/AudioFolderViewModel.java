package espritsoft.com.playvideo.Folder.AudioFolder.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import espritsoft.com.playvideo.Folder.AudioFolder.Repository.AudioFolderRepository;
import espritsoft.com.playvideo.Folder.Model.FolderModul;

public class AudioFolderViewModel extends AndroidViewModel {

    private MutableLiveData<List<FolderModul>>folderList=new MutableLiveData<>();
    private AudioFolderRepository audioFolderRepository;
    public AudioFolderViewModel(@NonNull Application application) {
        super(application);
        audioFolderRepository=new AudioFolderRepository(application);
    }

    public void getPermission(){
        folderList=audioFolderRepository.getFolder();
    }

    public LiveData<List<FolderModul>>getFolder(){
        return folderList;
    }
}
