package espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.AllSongList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongListBootomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongListBootomFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongListBootomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoonsListBootomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongListBootomFragment newInstance(String param1, String param2) {
        SongListBootomFragment fragment = new SongListBootomFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_soons_list_bootom, container, false);

        intiLayout();


        return view;
    }

    SonglistBottomFragmentAdaptor songlistBottomFragmentAdaptor;
    private void intiLayout() {

        RecyclerView recyclerView=view.findViewById(R.id.recyclerViewSongList);
        songlistBottomFragmentAdaptor =new SonglistBottomFragmentAdaptor(getContext(),Home.getInstance().AudiouSongsList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songlistBottomFragmentAdaptor);


    }
}