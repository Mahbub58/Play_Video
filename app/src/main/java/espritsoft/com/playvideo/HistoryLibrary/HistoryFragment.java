package espritsoft.com.playvideo.HistoryLibrary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import espritsoft.com.playvideo.AditionalClass.SpacingDecorationVertical;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.SpacingDecoration;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.HistoryLibrary.PlayListView.ModulPlayListItemView;
import espritsoft.com.playvideo.HistoryLibrary.PlayListView.PlayListItemAdaptor;
import espritsoft.com.playvideo.HistoryLibrary.PlayList_open.PlayListOpenFragment;
import espritsoft.com.playvideo.HistoryLibrary.ViewModel.HistoryFragmentViewModel;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    private static HistoryFragment instence;
    public static HistoryFragment getInstance(){
        return instence;
    }

    public static DBManager_History dbManager;
    public static String RecordID;

    /** opened folder name**
     */
    private ConstraintSet constraintSet = new ConstraintSet();
    ConstraintLayout.LayoutParams paramsminiView,paramsmSRootView;
    ConstraintLayout hContainer,hRootContainer;
    Guideline guidelineMiniView,guidelineSRootView;
    private HistoryFragmentViewModel historyFragmentViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_history, container, false);
        dbManager = new DBManager_History(getContext());
        instence=this;
        initLayout();
        ListOpenClose( Home.getInstance().playlistOpen);


        return view;
    }





    Button favourite,favouriteVideo; HistoryAdaptor historyAdaptor;
    RecyclerView recyclerViewHistory,recyclerViewPlayList;
    private void initLayout() {

        //toolbar
        TextView tollbarTitle=view.findViewById(R.id.search);
        tollbarTitle.setText("Library");
        tollbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //history
        TextView clearhistory=view.findViewById(R.id.clearAll);
        clearhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyFragmentViewModel.confirmDialog(getContext());
            }
        });


        // list open
        hContainer=view.findViewById(R.id.hContainer);
        hRootContainer=view.findViewById(R.id.hrootContainer);
        guidelineMiniView=view.findViewById(R.id.guidline_below_fOpen);
        paramsminiView = (ConstraintLayout.LayoutParams) guidelineMiniView.getLayoutParams();

        favourite=view.findViewById(R.id.favourite);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Home.getInstance().opendedPlayListName="Favourite Audio";
                ListOpenClose(true);


            }
        });
        favouriteVideo=view.findViewById(R.id.favouriteVideo);
        favouriteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.getInstance().opendedPlayListName="Favourite Video";
                ListOpenClose(true);
              //  Home.getInstance().bottomNavigationHide();

//                History history=new History("title","descc","folder","artist","cover","path","duration");
//                historyFragmentViewModel.insert(history);
            }
        });

        recyclerViewPlayList=view.findViewById(R.id.recyclerViewPlayList);
        CollectPlayList();

        recyclerViewHistory=view.findViewById(R.id.recyclerViewHistory);


        historyFragmentViewModel= ViewModelProviders.of(getActivity()).get(HistoryFragmentViewModel.class);
        callAdaptor();
        historyFragmentViewModel.getHistory().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> dataModelHistiories) {

                historyAdaptor.setList(dataModelHistiories);
              //  Toast.makeText(getContext(),"ss=="+dataModelHistiories.get(0).getTitle(),Toast.LENGTH_SHORT).show();

            }
        });



     //   historyList.add(new historyItemModul("title","album","folder","path","artist","3.15","cover"));
      //  callAdaptor();
    }
    ArrayList<historyItemModul>  historyList=new ArrayList();
    public  void LoadElement() {


        //sarch
        String[] SelectionArgs = {"%" + "" + "%"};



        //clear after load
        historyList.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {
            /*    tableData+=cursor.getString(cursor.getColumnIndex(DBManager.task_name))+","+
                       cursor.getString(cursor.getColumnIndex(DBManager.task_desc))+"::";
*/


                //adaptor
                    historyList.add(new historyItemModul(
                             cursor.getString(cursor.getColumnIndex(DBManager_History.song_name))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_folder))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_path))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_artist))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_duration))
                            , cursor.getString(cursor.getColumnIndex(DBManager_History.song_album_cover))

                    ));

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }
        callAdaptor();

    }

    void callAdaptor(){
        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE); //???????? ?????? ??????
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x; //?????? ??????

        int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, this.getResources().getDisplayMetrics()); //????????? ? ?????

        int columnsCount = screenWidth/photoWidth; //????? ????????
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false);
        recyclerViewHistory.setLayoutManager(gridLayoutManager);
        historyAdaptor =new HistoryAdaptor(getContext());
        //padinr top and bottom==================================
        SpacingDecorationVertical spacingDecoration=new SpacingDecorationVertical(20,10);
        recyclerViewHistory.addItemDecoration(spacingDecoration);

        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
        recyclerViewHistory.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerViewHistory.setAdapter(historyAdaptor);

        if(historyAdaptor!=null){
            historyAdaptor.setOnItemClickListner(new HistoryAdaptor.onItemClickListner() {
                @Override
                public void onItemClickListner(History audiouModel) {
                    AudioPlaySystem.getInstance().SongFolder=audiouModel.getFolder();
                    AudioPlaySystem.getInstance().SongUri=audiouModel.getPath();
                    AudioPlaySystem.getInstance().AlbumCover=audiouModel.getAlbumCover();
                    AudioPlaySystem.getInstance().SongName=audiouModel.getName();
                    AudioPlaySystem.getInstance().SongAlbam=audiouModel.getAlbum();
                    AudioPlaySystem.getInstance().SongDuration=audiouModel.getDuration();
                    AudioPlaySystem.getInstance().songProgress= Integer.parseInt(audiouModel.getProgress());
                    String pth=audiouModel.getPath();
                    String ex=pth.substring(pth.indexOf("."));
                    if(!ex.equals(".mp3")){
                        AudioPlaySystem.getInstance().db_item_id=audiouModel.getId();
                        historyFragmentViewModel.getVideoQueList();
                        AudioPlaySystem.getInstance().videoFromPlayList=2;
                        AudioPlaySystem.getInstance().PlayListName="Recent";
                        ((Home)getActivity()).PlayVideo();
                    }else{
                        historyFragmentViewModel.getAudioQueList();
                        ((Home)getActivity()).PlayAudio();
                        //  ((Home)getActivity()).AudiouQueueList();
                        AudioPlaySystem.getInstance().AudioPlay();
                    }
                    Log.e("vProgresss","=progress="+audiouModel.getProgress());
                }

                @Override
                public void menu(History history, View v) {
                    historyFragmentViewModel.ItemMenu(getContext(),v,history);
                }
            });
        }
    }


    public void ListOpenClose(boolean open){
        if(open){
            Fragment mFragment = new PlayListOpenFragment();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(hContainer.getId(), mFragment).commit();

            paramsminiView.guidePercent = 0F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);
            constraintSet.applyTo(hRootContainer);
            Home.getInstance().playlistOpen=true;

            Home.getInstance().bottomNavigationHide();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationExpand();
                }
            },1000);

//            statusbarWhite();
        }else{
            animationDown();
            paramsminiView.guidePercent = 1F; // 45% // range: 0 <-> 1
            guidelineMiniView.setLayoutParams(paramsminiView);

            constraintSet.applyTo(hRootContainer);

            //  slideDown(folderContainer);
            Home.getInstance().playlistOpen=false;

//            statusbarGrey();

        }
    }

    void animationDown(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Transition transition = new ChangeBounds();
            transition.setInterpolator(new AnticipateOvershootInterpolator());
            transition.setDuration(1000);

            TransitionManager.beginDelayedTransition(hRootContainer, transition);
        }

    }

    void animationExpand() {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 1, 0);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 1, 0);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 1, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 1, 1);
        @SuppressLint("ObjectAnimatorBinding") PropertyValuesHolder pvhRoundness = PropertyValuesHolder.ofFloat("roundness", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(hContainer, pvhLeft, pvhTop,
                pvhRight, pvhBottom, pvhRoundness);
        collapseExpandAnim.setupStartValues();

        hContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                hContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });
    }
    // ================================== cullect all playList ===================
    public ArrayList<ModulPlayListItemView> PlayListItem=new ArrayList();
    // ============ all playList calculate coullect
   public PlayListItemAdaptor playListItemAdaptor;
    /**PlayList colect **/
    int totalV=1;
    boolean state;
    public ArrayList<ModulPlayListItemView> CalculatedPlaylist =new ArrayList<>();
    public   void calculatePlayList(){
        CalculatedPlaylist.clear();
        Collections.sort(PlayListItem, new Comparator<ModulPlayListItemView>() {
            @Override
            public int compare(ModulPlayListItemView o1, ModulPlayListItemView o2) {
                return o1.getPlayListName().compareTo(o2.getPlayListName());
            }

        });


        if(PlayListItem.size()==1){
            int p=0;
            CalculatedPlaylist.add(new ModulPlayListItemView(PlayListItem.get(p).getPlayListName()
                    ,PlayListItem.get(p).getPlayListCover(),String.valueOf(totalV)
                    ,PlayListItem.get(p).getPlayList_song_path(),PlayListItem.get(p).getSong_name()));
        }


        for (int i = 1; i < PlayListItem.size(); i++) {
            String a1 = PlayListItem.get(i).getPlayListName();
            String cover=PlayListItem.get(i-1).getPlayListCover();
            String a2 = PlayListItem.get(i-1).getPlayListName();
            String songPath = PlayListItem.get(i-1).getPlayList_song_path();
            String songName = PlayListItem.get(i-1).getSong_name();

            if (!a1.equals(a2)) {
                CalculatedPlaylist.add(new ModulPlayListItemView(a2,cover,String.valueOf(totalV),songPath,songName));

                if(i==PlayListItem.size()-1){
                    CalculatedPlaylist.add(new ModulPlayListItemView(PlayListItem.get(i).getPlayListName(),PlayListItem.get(i).getPlayListCover(),String.valueOf(1),PlayListItem.get(i).getPlayList_song_path(),PlayListItem.get(i).getSong_name()));
                }
                totalV=1;
            }else totalV++;

            if (a1.equals(a2))
                if(i==PlayListItem.size()-1){
                    CalculatedPlaylist.add(new ModulPlayListItemView(PlayListItem.get(i).getPlayListName()
                            ,PlayListItem.get(i).getPlayListCover(),String.valueOf(totalV)
                            ,PlayListItem.get(i).getPlayList_song_path(),PlayListItem.get(i).getSong_name()));
                }

        }


        playListItemAdaptor=new PlayListItemAdaptor(getContext(),CalculatedPlaylist);
        //padinr top and bottom==================================
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
//int space = getResources().getDimensionPixelSize(
//    R.dimen.list_item_padding_vertical); // from resources
        recyclerViewPlayList.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerViewPlayList.setAdapter(playListItemAdaptor);

        recyclerViewPlayList.setLayoutManager(new LinearLayoutManager(getContext()));
        SpacingDecoration spacingDecoration=new SpacingDecoration(0);
        recyclerViewPlayList.addItemDecoration(spacingDecoration);
        adaptorOnClick();



    }
    public  void CollectPlayList() {


        //sarch
        String[] SelectionArgs = {"%" + "" + "%"};



        //clear after load
        PlayListItem.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {

                //adaptor

                PlayListItem.add(new ModulPlayListItemView(
                        cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)),
                        cursor.getString(cursor.getColumnIndex(DBManager_History.song_album_cover)),
                        "1",
                        cursor.getString(cursor.getColumnIndex(DBManager_History.song_path))
                        , cursor.getString(cursor.getColumnIndex(DBManager_History.song_name))


                ));

            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();


        }

        calculatePlayList();
    }

    ArrayList<String> playListAlocated=new ArrayList();
    void adaptorOnClick(){
        if(playListAlocated!=null){
            playListItemAdaptor.setOnItemClickListner(new PlayListItemAdaptor.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {
                    Home.getInstance().opendedPlayListName=(CalculatedPlaylist.get(position).getPlayListName());
                    ListOpenClose(true);
                   // Home.getInstance().bottomNavigationHide();
                }

                @Override
                public void menudialog(int position) {
                   // checkPlayList(PlayListItem.get(position).getPlayListName());
                }
            });
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
      //  outState.putString("ttl",opendedPlayListName);
       // outState.putBoolean("opcp",  Home.getInstance().playlistOpen);
    }
}