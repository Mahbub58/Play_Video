package espritsoft.com.playvideo.Search.SearchAudio;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Search.SearchAudio.ViewModel.SearchAudioFragmentViewModel;
import espritsoft.com.playvideo.Search.SearchVideo.SearchVideoAdaptor;
import espritsoft.com.playvideo.Video.VideoModel;

public class SearchViewAudio extends Fragment {

    public SearchViewAudio() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SearchAudioFragmentViewModel searchAudioFragmentViewModel;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_view_audio,container,false);

        searchAudioFragmentViewModel= ViewModelProviders.of(getActivity()).get(SearchAudioFragmentViewModel.class);
        iniTializeLayout();

        return view;
    }
    ArrayList<AudiouModel> audioList=new ArrayList<>();
    SearchAudioAdaptor searchAudioAdaptor;
    EditText searchEditText;
    ImageButton backButton;
    RecyclerView viewItem;
    private void iniTializeLayout() {


        searchAudioFragmentViewModel.openKeyboard();
        backButton=view.findViewById(R.id.backPrease);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAudioFragmentViewModel.closeKeyboard(getView());
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

        searchAudioFragmentViewModel.getAllAudioSong(audioList);
        searchAudioAdaptor=new SearchAudioAdaptor(audioList);
        viewItem.setAdapter(searchAudioAdaptor);
        viewItem.setLayoutManager(new LinearLayoutManager(getContext()));


        //load all data to filter List
        filter("");
    }

    private void filter(String toString) {
        ArrayList<AudiouModel> filteredList=new ArrayList();
        for (AudiouModel audiouModel:audioList){
            if(audiouModel.getSong_name().toLowerCase().contains(toString.toLowerCase(Locale.ROOT))){
                filteredList.add(audiouModel);
            }
        }
        searchAudioAdaptor.Filter(filteredList);

        if(searchAudioAdaptor!=null){
            searchAudioAdaptor.setOnItemClickListner(new SearchAudioAdaptor.onClickListner() {
                @Override
                public void onClickListner(int position) {
                    AudioPlaySystem.getInstance().songProgress=0;
                    AudioPlaySystem.getInstance().SongName=filteredList.get(position).getSong_name();
                    AudioPlaySystem.getInstance().SongFolder=filteredList.get(position).getFolderName();
                    AudioPlaySystem.getInstance().SongUri=filteredList.get(position).getPath();
                    AudioPlaySystem.getInstance().AlbumCover=filteredList.get(position).getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongAlbam=filteredList.get(position).getAlbam_name();
                    AudioPlaySystem.getInstance().SongDuration=filteredList.get(position).getHour()+":"+filteredList.get(position).getMunite()+":"+filteredList.get(position).getSecond();

                    searchAudioFragmentViewModel.closeKeyboard(getView());
                   // Home.getInstance().SerchViewClose();
                    searchAudioFragmentViewModel.GetQueuSongList(filteredList.get(position).getFolderName());
                    ((Home)getActivity()).PlayAudio();
                    AudioPlaySystem.getInstance().AudioPlay();

                }
            });
        }
    }
}
