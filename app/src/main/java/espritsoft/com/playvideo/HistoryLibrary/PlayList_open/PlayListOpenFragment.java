package espritsoft.com.playvideo.HistoryLibrary.PlayList_open;

import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import espritsoft.com.playvideo.AditionalClass.SpacingDecorationVertical;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.Folder.FolderFragment;
import espritsoft.com.playvideo.HistoryLibrary.HistoryFragment;
import espritsoft.com.playvideo.HistoryLibrary.PlayList_open.ViewModel.PlayListOpenViewModel;
import espritsoft.com.playvideo.HistoryLibrary.historyItemModul;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListOpenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListOpenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayListOpenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListOpenFragment newInstance(String param1, String param2) {
        PlayListOpenFragment fragment = new PlayListOpenFragment();
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
    private static PlayListOpenFragment instence;
    public static PlayListOpenFragment getInstance(){
        return instence;
    }

    public static DBManager_History dbManager;
    public static String RecordID;
    PlayListOpenViewModel playListOpenViewModel;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_play_list_open, container, false);
        dbManager = new DBManager_History(getContext());
        instence=this;
        playListOpenViewModel= ViewModelProviders.of(getActivity()).get(PlayListOpenViewModel.class);

        initLayout();
        if(savedInstanceState!=null){

        }


        return view;
    }

    Toolbar toolbar;
    RecyclerView recyclerViewFavourite;
    private void initLayout() {
        toolbar=view.findViewById(R.id.f_open_tolbar);
        TextView ttitle=view.findViewById(R.id.ttitle);
       // ((Home)getActivity()).getDelegate().setSupportActionBar(toolbar);
       // ((Home)getActivity()).getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ttitle.setText(Home.getInstance().opendedPlayListName);
        ImageButton backPress=view.findViewById(R.id.backPrease);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment.getInstance().ListOpenClose(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Home.getInstance().bottomNavigationShow();
                    }
                },1000);
            }
        });

        recyclerViewFavourite=view.findViewById(R.id.recyclerViewFavourite);

        LoadElement();
    }
    ArrayList<historyItemModul> playListItem=new ArrayList();
    public  void LoadElement() {


        //sarch
        String[] SelectionArgs = {"%" + "" + "%"};



        //clear after load
        playListItem.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {
            /*    tableData+=cursor.getString(cursor.getColumnIndex(DBManager.task_name))+","+
                       cursor.getString(cursor.getColumnIndex(DBManager.task_desc))+"::";
*/


                //adaptor
                if (Home.getInstance().opendedPlayListName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)))) {
                    playListItem.add(new historyItemModul(
                            cursor.getString(cursor.getColumnIndex(DBManager_History.song_name))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_folder))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_path))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_artist))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_duration))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album_cover))

                    ));

                }

                }
                while ((cursor.moveToPrevious())) ;
                //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


            }

        callAdaptor();

    }
    public PlayListOpenAdaptor historyAdaptor;
    void callAdaptor(){
        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE); //???????? ?????? ??????
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x; //?????? ??????

        int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, this.getResources().getDisplayMetrics()); //????????? ? ?????

        int columnsCount = screenWidth/photoWidth; //????? ????????
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerViewFavourite.setLayoutManager(gridLayoutManager);
        historyAdaptor =new PlayListOpenAdaptor(getContext(),playListItem);
        //padinr top and bottom==================================
        SpacingDecorationVertical spacingDecoration=new SpacingDecorationVertical(20,10);
        recyclerViewFavourite.addItemDecoration(spacingDecoration);

        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
        recyclerViewFavourite.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerViewFavourite.setAdapter(historyAdaptor);

        adaptorClick();
    }

    private void adaptorClick() {
        if(historyAdaptor!=null){
            historyAdaptor.setOnItemClickListner(new PlayListOpenAdaptor.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void menudialog(int position,View v) {
                   playListOpenViewModel.ItemMenu(getContext(),v,playListItem.get(position).getSong_name());

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}