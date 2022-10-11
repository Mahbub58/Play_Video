package espritsoft.com.playvideo.Search.SearchVideo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Search.SearchVideo.ViewModel.SearchViewVideoViewModel;
import espritsoft.com.playvideo.Video.VideoModel;

public class SearchVideoFragment extends Fragment {

    public SearchVideoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static SearchVideoFragment instance;
    public SearchVideoFragment getInstance(){
        return instance;
    }


    private SearchViewVideoViewModel searchViewVideoViewModel;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_search_video,container,false);
        instance=this;
        searchViewVideoViewModel= ViewModelProviders.of(getActivity()).get(SearchViewVideoViewModel.class);


        iniTializLayot();

        return view;

    }
//    ArrayList<VideoModel> videoList=new ArrayList<>();
    SearchVideoAdaptor searchVideoAdaptor;
    EditText searchEditText;
    ImageButton backButton;
    RecyclerView viewItem;
    private void iniTializLayot() {

        searchViewVideoViewModel.openKeyboard();
        backButton=view.findViewById(R.id.backPrease);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewVideoViewModel.closeKeyboard(getView());
                Home.getInstance().SerchViewClose();
            }
        });

        searchEditText=view.findViewById(R.id.searchText);
        searchEditText.requestFocus();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        viewItem=view.findViewById(R.id.viewItem);


        searchViewVideoViewModel.getpermission();

         searchVideoAdaptor=new SearchVideoAdaptor(getContext());
        viewItem.setAdapter(searchVideoAdaptor);
        viewItem.setLayoutManager(new LinearLayoutManager(getContext()));

        searchViewVideoViewModel.getVideo().observe(this, new Observer<List<VideoModel>>() {
            @Override
            public void onChanged(List<VideoModel> videoModels) {
                searchVideoAdaptor.setList(videoModels);
            }
        });

        //load all data to filter List
        filter("");
    }

    private void filter(String toString) {
        ArrayList<VideoModel>filterList=new ArrayList<>();
        for(VideoModel videoModel:searchViewVideoViewModel.getVideo().getValue()){
            if (videoModel.getSong_name().toLowerCase().contains(toString.toLowerCase())){
                filterList.add(videoModel);
            }
        }
        searchVideoAdaptor.filterList(filterList);

        if(searchVideoAdaptor!=null){
            searchVideoAdaptor.onItemClickListner(new SearchVideoAdaptor.onItemClickListner() {
                @Override
                public void onItemClickListner(VideoModel videoModel) {
                    AudioPlaySystem.getInstance().AlbumCover=videoModel.getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongUri=videoModel.getPath();
                    AudioPlaySystem.getInstance().SongName=videoModel.getSong_name();
                    AudioPlaySystem.getInstance().SongFolder=videoModel.getFolderName();
                    AudioPlaySystem.getInstance().SongDuration=videoModel.getHour()+":"+
                            videoModel.getMunite()+":"+videoModel.getSecond();


                    // Home.getInstance().get_All_Video_by_folder(((Home) getActivity()).VideoList.get(position).getAlbam_name());

                    searchViewVideoViewModel.getDataByFolder(videoModel.getFolderName());
                    searchViewVideoViewModel.getVideoQueList();

//                    Home.getInstance().videoPlayIsActive=true;
                    searchViewVideoViewModel.closeKeyboard(getView());
                    AudioPlaySystem.getInstance().songProgress=0;

                    ((Home)getActivity()).PlayVideo();
                }
            });
        }
    }
}
