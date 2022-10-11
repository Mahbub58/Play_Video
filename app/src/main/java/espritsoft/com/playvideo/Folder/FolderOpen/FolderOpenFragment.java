package espritsoft.com.playvideo.Folder.FolderOpen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.Folder.FolderFragment;
import espritsoft.com.playvideo.Folder.FolderOpen.Model.FolderItemModel;
import espritsoft.com.playvideo.Folder.FolderOpen.ViewModel.FolderOpenViewModel;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;
import espritsoft.com.playvideo.VideoPlay.VideoPlayFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderOpenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderOpenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderOpenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderOpenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderOpenFragment newInstance(String param1, String param2) {
        FolderOpenFragment fragment = new FolderOpenFragment();
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
    public static FolderOpenFragment instence;
    public static FolderOpenFragment getInstence(){
        return instence;
    }


    View view;
    String fopen="";
    FolderOpenViewModel folderOpenViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_folder_open, container, false);
        instence=this;
        folderOpenViewModel= ViewModelProviders.of(getActivity()).get(FolderOpenViewModel.class);




        if(savedInstanceState!=null){
            fopen=savedInstanceState.getString("f");
            initLayout();
        }else{
            CheckUserPermsions();
            initLayout();
        }

        return view;
    }

    RecyclerView recyclerView;
    public FolderopenAdaptor folderopenAdaptor;
    Toolbar toolbar;

TextView ttitle;
    private void initLayout() {
        toolbar=view.findViewById(R.id.f_open_tolbar);
        ttitle=view.findViewById(R.id.ttitle);
        ((Home)getActivity()).getDelegate().setSupportActionBar(toolbar);
        fopen=Home.getInstance().opendedFolderName;
        ttitle.setText(fopen);
        ImageButton backPress=view.findViewById(R.id.backPrease);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderFragment.getInstance().folderOpenClose(false);
            }
        });


        recyclerView=view.findViewById(R.id.recyclerViewVideofromFolder);

        folderopenAdaptor = new FolderopenAdaptor(getContext());
        //padinr top and bottom==================================
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerView.setAdapter(folderopenAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        folderOpenViewModel.getItem().observe(this, new Observer<List<FolderItemModel>>() {
            @Override
            public void onChanged(List<FolderItemModel> folderItemModels) {
                folderopenAdaptor.setList(folderItemModels);
            }
        });



            if(folderopenAdaptor != null){
                folderopenAdaptor.setOnItemClickListner(new FolderopenAdaptor.OnItemClickListner() {
                    @Override
                    public void onItemClick(FolderItemModel folderItemModel) {

                        AudioPlaySystem.getInstance().SongUri=folderItemModel.getPath();
                        AudioPlaySystem.getInstance().SongName=folderItemModel.getSong_name();
                        AudioPlaySystem.getInstance().SongFolder=folderItemModel.getFolderName();
                        AudioPlaySystem.getInstance().SongDuration=folderItemModel.getHour()+":"+
                                folderItemModel.getMunite()+":"+folderItemModel.getSecond();
                        AudioPlaySystem.getInstance().songProgress=0;
                        if(Home.getInstance().isAudio){
                            ((Home)getActivity()).PlayAudio();
                            ((Home)getActivity()).AudiouQueueList();
                            AudioPlaySystem.getInstance().AudioPlay();
                        }else{
                            // Home.getInstance().videoPlayIsActive=true;
                            folderOpenViewModel.getDataByFolder(folderItemModel.getFolderName());
                            folderOpenViewModel.getVideoQueList();
                            //playList is off
                            AudioPlaySystem.getInstance().videoFromPlayList=0;
                            History history=new History(folderItemModel.getSong_name(),folderItemModel.getAlbam_name(),folderItemModel.getFolderName(),folderItemModel.getArtist_name(),folderItemModel.getAlbumArtUriImage(),
                                    folderItemModel.getPath(),folderItemModel.getHour()+":"+
                                    folderItemModel.getMunite()+":"+folderItemModel.getSecond(),"0");
                            folderOpenViewModel.insertHistory(history);
                          //  VideoPlayFragment.getInstance().init_videoPlay();
                            AudioPlaySystem.getInstance().db_item_id=folderOpenViewModel.getId();
                            ((Home)getActivity()).PlayVideo();
                        }
                    }

                    @Override
                    public void menudialog(FolderItemModel folderItemModel, View view) {
                        folderOpenViewModel.SongMenuMore(folderItemModel,getContext(),getActivity(),view,getFragmentManager());
                    }
                });
            }
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
        folderOpenViewModel.getPermission( Home.getInstance().isAudio,Home.getInstance().opendedFolderName);
    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    folderOpenViewModel.getPermission( Home.getInstance().isAudio,Home.getInstance().opendedFolderName);
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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("f",fopen);
    }
}