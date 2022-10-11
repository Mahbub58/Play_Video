package espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudioPlay;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioBottomSheetDialog_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioBottomSheetDialog_Fragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudioBottomSheetDialog_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioBottomSheetDialog_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioBottomSheetDialog_Fragment newInstance(String param1, String param2) {
        AudioBottomSheetDialog_Fragment fragment = new AudioBottomSheetDialog_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    View view;
    private static AudioBottomSheetDialog_Fragment instance;
    public static AudioBottomSheetDialog_Fragment getInstance() {
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.audio_bottom_sheet_dialog_fragment, container, false);

        initializeLayout();
        return view;
    }

    AudioAdaptorBootomSheet audioAdaptor;
    RecyclerView bootomSheetRecyclerView;
    private void initializeLayout() {
        bootomSheetRecyclerView=view.findViewById(R.id.bottomSheetRecyclerView);

//         audioAdaptor = new AudioAdaptorBootomSheet(getContext(), ((Home) getActivity()).AudiouSongsList);
//        bootomSheetRecyclerView.setAdapter(audioAdaptor);
//        bootomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        audioAdaptor=new AudioAdaptorBootomSheet(getContext(),AudioPlaySystem.getInstance().queueList);
        bootomSheetRecyclerView.setAdapter(audioAdaptor);
        bootomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SpacingDecoration spacingDecoration=new SpacingDecoration(10);
        bootomSheetRecyclerView.addItemDecoration(spacingDecoration);
        AdaptorClicd();
    }

    void AdaptorClicd(){
        if(audioAdaptor!=null){
            audioAdaptor.setOnItemClickListner(new AudioAdaptorBootomSheet.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {
                    AudioPlaySystem.getInstance().SongUri= AudioPlaySystem.getInstance().queueList.get(position).getPath();
                    AudioPlaySystem.getInstance().AlbumCover= AudioPlaySystem.getInstance().queueList.get(position).getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongName= AudioPlaySystem.getInstance().queueList.get(position).getSong_name();
                    AudioPlaySystem.getInstance().SongAlbam= AudioPlaySystem.getInstance().queueList.get(position).getAlbam_name();
                    AudioPlaySystem.getInstance().SongDuration= AudioPlaySystem.getInstance().queueList.get(position).getMunite()+":"+
                            AudioPlaySystem.getInstance().queueList.get(position).getSecond() ;
                    AudioPlaySystem.getInstance().songProgress=0;
                    AudioPlay.getInstance().audioPlay();
                    AudioPlaySystem.getInstance().AudioPlay();
                    audioAdaptor.notifyDataSetChanged();
                }

                @Override
                public void menudialog(int position) {

                }
            });
        }
    }
}