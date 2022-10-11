package espritsoft.com.playvideo.Video;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.ViewModel.VideoFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
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
    private VideoFragmentViewModel videoFragmentViewModel;
    private static VideoFragment instance;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video, container, false);
        instance=this;
        videoFragmentViewModel= ViewModelProviders.of(getActivity()).get(VideoFragmentViewModel.class);



        //restore session
//        if (savedInstanceState != null) {
//            mId = savedInstanceState.getLong(STATE_ID, 0);
//            ((Home) getActivity()).VideoList= (ArrayList<VideoModel>) savedInstanceState.getSerializable(STATE_ITEMS);
//
//        } else {
//           // ((Home) getActivity()).VideoList = new ArrayList<>();
//        }

            initLayout();




      return view;
    }



    public String videoSongUri;
    String videoSongName;
    String videoSongDuration;
    String videoSongFolder;
    TextView Seach;
    public VideoAdaptor videoAdaptor;
    ImageButton setting;



    void initLayout(){

        setting=view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  videoFragmentViewModel.SongMenuMore(getContext(),v);
            }
        });

        Seach=view.findViewById(R.id.search);
        Seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Home.getInstance().videoSerchViewOpen();
            }
        });

        videoFragmentViewModel.getpermission();
        loadVideo();
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
        videoFragmentViewModel.getpermission();
    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   videoFragmentViewModel.getpermission();
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


   public void loadVideo(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewVideo);
        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
          videoAdaptor =new VideoAdaptor(getContext());
        //padinr top and bottom==================================
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
//int space = getResources().getDimensionPixelSize(
//    R.dimen.list_item_padding_vertical); // from resources
        recyclerView.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerView.setAdapter(videoAdaptor);


        //set data
       videoFragmentViewModel.getVideo().observe(this, new Observer<List<VideoModel>>() {
           @Override
           public void onChanged(List<VideoModel> videoModels) {
               videoAdaptor.setList(videoModels);
           }
       });

        onAdaptorClickItem();
    }

    void onAdaptorClickItem(){
        if(videoAdaptor !=null){
            videoAdaptor.setOnItemClickListner(new VideoAdaptor.OnItemClickListner() {
                @Override
                public void onItemClick(VideoModel videoModel) {
                    AudioPlaySystem.getInstance().AlbumCover=videoModel.getAlbumArtUriImage();
                    AudioPlaySystem.getInstance().SongUri=videoModel.getPath();
                    AudioPlaySystem.getInstance().SongName=videoModel.getSong_name();
                    AudioPlaySystem.getInstance().SongFolder=videoModel.getFolderName();
                    AudioPlaySystem.getInstance().SongDuration=videoModel.getHour()+":"+
                            videoModel.getMunite()+":"+videoModel.getSecond() ;
                    AudioPlaySystem.getInstance().songProgress=0;

                    //  Home.getInstance().get_All_Video_by_folder(((Home) getActivity()).VideoList.get(position).getAlbam_name());

//                    Home.getInstance().videoPlayIsActive=true;
                     videoFragmentViewModel.getDataByFolder(videoModel.getFolderName());
                     videoFragmentViewModel.getVideoQueList();
                     //add to recent
                    History history=new History(AudioPlaySystem.getInstance().SongName,AudioPlaySystem.getInstance().SongAlbam,AudioPlaySystem.getInstance().SongFolder,"unknown",AudioPlaySystem.getInstance().AlbumCover,AudioPlaySystem.getInstance().SongUri,AudioPlaySystem.getInstance().SongDuration,
                            String.valueOf( AudioPlaySystem.getInstance().songProgress));
                    videoFragmentViewModel.insertHistory(history);
                     //playList is off
                    AudioPlaySystem.getInstance().videoFromPlayList=0;
                    //song play
                     ((Home)getActivity()).PlayVideo();
                      AudioPlaySystem.getInstance().db_item_id=videoFragmentViewModel.getId();

                }

                @Override
                public void menudialog(VideoModel videoModel, View v) {
                    videoFragmentViewModel.SongMenuMore(videoModel,getContext(),getActivity(),v,getFragmentManager());
                }

            });
        }
    }

    /**
     * Show Properties
     * @return
     */
    public void ShowDialogProperties(
            Context context, String Name, String path, String folder,
            String duration, String size, String date, String format){

        AlertDialog.Builder PropertiesShow=new AlertDialog.Builder(context);
        View mView= getLayoutInflater().inflate(R.layout.dialog_item_properties,null);
        PropertiesShow.setView(mView);
        PropertiesShow.create();
        PropertiesShow.show();

        TextView dname,dlocation,dfolder,dpath,dsize,dduration,length,ddate,formate;
        dname=mView.findViewById(R.id.file_name);
        ddate=mView.findViewById(R.id.date);
        dfolder=mView.findViewById(R.id.file_folder);
        dduration=mView.findViewById(R.id.file_length);
        dpath=mView.findViewById(R.id.file_location);
        dsize=mView.findViewById(R.id.file_siz);
        formate=mView.findViewById(R.id.file_foramt);
        dname.setText(Name);
        ddate.setText(date);
        dfolder.setText(folder);
        dduration.setText(duration);
        dpath.setText(path);
        dsize.setText(size);
        formate.setText(format);


    }




    public static VideoFragment getInstance() {
        return instance;
    }

    private static final String STATE_COUNTER = "counter";
    private int mCounter;
    private static final String STATE_ID = "id";
    private static final String STATE_ITEMS = "items";
    private long mId;
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(STATE_COUNTER, mCounter);
//        outState.putSerializable(STATE_ITEMS, ((Home) getActivity()).VideoList);
//    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//    }

}

