package espritsoft.com.playvideo.Folder.VideoFolder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

import espritsoft.com.playvideo.AditionalClass.SpacingDecorationVertical;
import espritsoft.com.playvideo.Folder.FolderFragment;
import espritsoft.com.playvideo.Folder.Model.FolderModul;
import espritsoft.com.playvideo.Folder.VideoFolder.ViewModel.VideoFolderViewModel;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFolderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoFolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFolderFragment newInstance(String param1, String param2) {
        VideoFolderFragment fragment = new VideoFolderFragment();
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
    String fname;
    View view;
    VideoFolderViewModel videoFolderViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_folder, container, false);
        videoFolderViewModel= ViewModelProviders.of(getActivity()).get(VideoFolderViewModel.class);
        CheckUserPermsions();
        initLayout();

     if(savedInstanceState !=null){
       //  fname=savedInstanceState.getString("fname");
       //  Home.getInstance().get_All_Video_by_folder(fname);
     }



        return view;
    }

    VideoFolderAdaptor videoFolderAdaptor;
    RecyclerView recyclerViewFolder;



    private void initLayout() {
        recyclerViewFolder=view.findViewById(R.id.recyclerViewVideoFolder);


        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE); //???????? ?????? ??????
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x; //?????? ??????

        int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, this.getResources().getDisplayMetrics()); //????????? ? ?????

        int columnsCount = screenWidth/photoWidth; //????? ????????
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),columnsCount,GridLayoutManager.VERTICAL,false);
        recyclerViewFolder.setLayoutManager(gridLayoutManager);
        videoFolderAdaptor =new VideoFolderAdaptor(getContext());
        //padinr top and bottom==================================
        SpacingDecorationVertical spacingDecoration=new SpacingDecorationVertical(20,10);
        recyclerViewFolder.addItemDecoration(spacingDecoration);

        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
        recyclerViewFolder.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerViewFolder.setAdapter(videoFolderAdaptor);



        videoFolderViewModel.getVideoFolder().observe(this, new Observer<List<FolderModul>>() {
            @Override
            public void onChanged(List<FolderModul> folderModuls) {
                videoFolderAdaptor.setList(folderModuls);
            }
        });


        if(videoFolderAdaptor!=null){
            videoFolderAdaptor.setOnItemClickListner(new VideoFolderAdaptor.OnItemClickListner() {
                @Override
                public void onItemClick(FolderModul folderModul) {
                    Home.getInstance().isAudio=false;
                    // Home.getInstance().get_All_Video_by_folder(((Home) getActivity()).VideoFolderListItem.get(position).getFolderName());
                    Home.getInstance().opendedFolderName=folderModul.getFolderName();
                 //   FolderFragment.getInstance().openedFolderName=folderModul.getFolderName();
                    FolderFragment.getInstance().folderOpenClose(true);

                }

                @Override
                public void menudialog(FolderModul folderModul) {

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
        videoFolderViewModel.getPermission();
    }

    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    videoFolderViewModel.getPermission();
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
        outState.putString("fame",fname);
    }


}