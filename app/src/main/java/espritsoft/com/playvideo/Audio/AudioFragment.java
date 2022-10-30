package espritsoft.com.playvideo.Audio;

import android.Manifest;
import android.app.Notification;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import espritsoft.com.playvideo.Audio.ViewModel.AudioFragmentViewModel;
import espritsoft.com.playvideo.AudioPlay.AudioPlay;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment    {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
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
    private static AudioFragment instance;

    public static AudioFragment getInstance() {
        return instance;
    }

    AudioFragmentViewModel audioFragmentViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_audio, container, false);
        instance=this;
        audioFragmentViewModel= ViewModelProviders.of(getActivity()).get(AudioFragmentViewModel.class);


       iniTilaizLayout();


        return view;
    }

    ImageButton setting;
    TextView serchEditText;
    private void iniTilaizLayout() {

        setting=view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  audioFragmentViewModel.SongMenuMore(v);
            }
        });

        serchEditText=view.findViewById(R.id.search);
        serchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.getInstance().audioSerchViewOpen();
            }
        });



        CheckUserPermsions();
        load_audio_song();
    }

    //================================================================ User Permission =============================================================

    public void CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE,},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        audioFragmentViewModel.getPermission();
    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   audioFragmentViewModel.getPermission();
                } else {
                    // Permission Denied
                    Alart();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public androidx.appcompat.app.AlertDialog.Builder Alart() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder=new androidx.appcompat.app.AlertDialog.Builder(getContext());
        mBuilder.setTitle("Alart")
                .setMessage("App Will not working properly.Please Allow all permission")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckUserPermsions();
                    }
                });

        mBuilder.create();
        mBuilder.show();
        return mBuilder;


    }



    public AudioAdaptor audioAdaptor;
    void load_audio_song(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewVideo);
        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

         audioAdaptor =new AudioAdaptor(getContext());

        //padinr top and bottom==================================
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
//int space = getResources().getDimensionPixelSize(
//    R.dimen.list_item_padding_vertical); // from resources
        recyclerView.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerView.setAdapter(audioAdaptor);


        audioFragmentViewModel.getAudio().observe(this, new Observer<List<AudiouModel>>() {
            @Override
            public void onChanged(List<AudiouModel> audiouModels) {
                audioAdaptor.setList(audiouModels);
            }
        });

        adaptorClick();
    }

    void adaptorClick(){
        if(audioAdaptor !=null){
            audioAdaptor.setOnItemClickListner(new AudioAdaptor.OnItemClickListner() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(AudiouModel audiouModel) {
                    AudioPlaySystem.getInstance().SongFolder=audiouModel.getFolderName();
                    AudioPlaySystem.getInstance().SongUri=audiouModel.getPath();
                    AudioPlaySystem.getInstance().AlbumCover=audiouModel.getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongName=audiouModel.getSong_name();
                    AudioPlaySystem.getInstance().SongAlbam=audiouModel.getFolderName();
                    AudioPlaySystem.getInstance().SongDuration=audiouModel.getHour()+":"+audiouModel.getMunite()+":"+
                            audiouModel.getSecond() ;


                    AudioPlaySystem.getInstance().songProgress=0;
                    audioFragmentViewModel.GetQueuSongList(audiouModel.getFolderName());
                    ((Home)getActivity()).PlayAudio();
                    //  ((Home)getActivity()).AudiouQueueList();

                    AudioPlaySystem.getInstance().AudioPlay();

                }

                @Override
                public void menudialog(AudiouModel audiouModel, View v) {
                    audioFragmentViewModel.SongMenuMore(audiouModel,getContext(),getActivity(),v,getFragmentManager());
                }
            });
        }
    }


}