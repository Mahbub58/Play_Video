package espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.SpacingDecoration;
import espritsoft.com.playvideo.DBManager.DBManager_History;
import espritsoft.com.playvideo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
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

    public static DBManager_History dbManager;
    public static String RecordID;
    private static PlayListFragment instance;

    public static PlayListFragment getInstance() {
        return instance;
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_play_list, container, false);
        instance=this;
        dbManager = new DBManager_History(getContext());
        initlayout();
        CollectPlayList();



        return view;
    }

    RecyclerView recyclerViewPlayListItem;
    PlayListItemAdaptorBootomSheet playListItemAdaptorBootomSheet;
    EditText creat_palaylestName;
    Button createPlayList,done;

    private void initlayout() {

        recyclerViewPlayListItem=view.findViewById(R.id.bottomFragmentPlayListItem);
        creat_palaylestName=view.findViewById(R.id.create_playListName);
        createPlayList=view.findViewById(R.id.createPlayList);
        done=view.findViewById(R.id.done);
        createPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=creat_palaylestName.getText().toString();
                insertPlayListSongAtDBM(s);
            }
        });


    }


    ArrayList<ModulPlayListItem> PlayListItem=new ArrayList();
    // ============ all playList calculate coullect
    /**PlayList colect **/
    int totalV=1;
    boolean state;
    public ArrayList<ModulPlayListItem> CalculatedPlaylist =new ArrayList<>();
    public   void calculatePlayList(){
        CalculatedPlaylist.clear();
        Collections.sort(PlayListItem, new Comparator<ModulPlayListItem>() {
            @Override
            public int compare(ModulPlayListItem o1, ModulPlayListItem o2) {
                return o1.getPlayListName().compareTo(o2.getPlayListName());
            }

        });


        if(PlayListItem.size()==1){
            int p=0;
            CalculatedPlaylist.add(new ModulPlayListItem(PlayListItem.get(p).getPlayListName()
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
                CalculatedPlaylist.add(new ModulPlayListItem(a2,cover,String.valueOf(totalV),songPath,songName));

                if(i==PlayListItem.size()-1){
                    CalculatedPlaylist.add(new ModulPlayListItem(PlayListItem.get(i).getPlayListName(),PlayListItem.get(i).getPlayListCover(),String.valueOf(1),PlayListItem.get(i).getPlayList_song_path(),PlayListItem.get(i).getSong_name()));
                }
                totalV=1;
            }else totalV++;

            if (a1.equals(a2))
                if(i==PlayListItem.size()-1){
                    CalculatedPlaylist.add(new ModulPlayListItem(PlayListItem.get(i).getPlayListName()
                            ,PlayListItem.get(i).getPlayListCover(),String.valueOf(totalV)
                            ,PlayListItem.get(i).getPlayList_song_path(),PlayListItem.get(i).getSong_name()));
                }

        }


        playListItemAdaptorBootomSheet=new PlayListItemAdaptorBootomSheet(getContext(),CalculatedPlaylist);
        recyclerViewPlayListItem.setAdapter(playListItemAdaptorBootomSheet);
        recyclerViewPlayListItem.setLayoutManager(new LinearLayoutManager(getContext()));
        SpacingDecoration spacingDecoration=new SpacingDecoration(0);
        recyclerViewPlayListItem.addItemDecoration(spacingDecoration);
        adaptorOnClick();

    }
    public  void CollectPlayList() {

        //clear after load
        PlayListItem.clear();

        //sarch
        String[] SelectionArgs = {"%" + "" + "%"};

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {

                //adaptor

                PlayListItem.add(new ModulPlayListItem(
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
        if(playListItemAdaptorBootomSheet!=null){
            playListItemAdaptorBootomSheet.setOnItemClickListner(new PlayListItemAdaptorBootomSheet.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {
                  //  checkPlayList(PlayListItem.get(position).getPlayListName());
                }

                @Override
                public void menudialog(int position) {
                    checkPlayList(CalculatedPlaylist.get(position).getPlayListName());
                   // playListItemAdaptorBootomSheet.notifyDataSetChanged();
                   // CollectPlayList();

                }
            });
        }
    }

    //================================================================= data base work
    boolean isDataNotInput=false;
    public  void checkPlayList(String PlayListName) {
        //sarch
        String[] SelectionArgs = {"%" +PlayListDataForSave.PName+ "%"};



        //clear after load
          PlayListItem.clear();

        //String[] projection=("UserName","password");            //sarch
        Cursor cursor = dbManager.query2(null, "song_name LIke ? ", SelectionArgs, DBManager_History.ColID);

        if (cursor.moveToLast()) {
            String tableData = "";
            do {
                if (PlayListDataForSave.PName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                        && cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals(PlayListName)) {
                    RecordID=cursor.getString(cursor.getColumnIndex(DBManager_History.ColID));
                    isDataNotInput=false;
                    break;
                }else if (PlayListDataForSave.PName.equals(cursor.getString(cursor.getColumnIndex(DBManager_History.song_name)))
                        && !cursor.getString(cursor.getColumnIndex(DBManager_History.playl_list_name)).equals(PlayListName)) {
                    isDataNotInput=true;

                }


            } while ((cursor.moveToPrevious()));
            //   Toast.makeText(,tableData,Toast.LENGTH_LONG).show();
            InsertRemoveDataDBM(PlayListName);

        }else{
            insertPlayListSongAtDBM(PlayListName);
           // Log.d("dsadasd","dddddddd");
        }


    }
    void InsertRemoveDataDBM(String PlayListName){
        if(isDataNotInput){
            insertPlayListSongAtDBM(PlayListName);
        }else{
            String[] SelectionArgss = new String[]{RecordID};
            int count = dbManager.Delet2("ID=?", SelectionArgss);
            //refresh Element
            if (count > 0) {
                //data deleted
                totalV=1;
                CollectPlayList();
            }
        }
    }

    void insertPlayListSongAtDBM(String playListName){
        ContentValues values = new ContentValues();
        values.put(DBManager_History.song_name,PlayListDataForSave.PName);
        values.put(DBManager_History.song_folder, PlayListDataForSave.PFolder);
        values.put(DBManager_History.song_album, PlayListDataForSave.PAlbum);
        values.put(DBManager_History.song_album_cover, PlayListDataForSave.PCover);
        values.put(DBManager_History.song_artist, PlayListDataForSave.PArtist);
        values.put(DBManager_History.song_duration,PlayListDataForSave.PDuration);
        values.put(DBManager_History.song_path, PlayListDataForSave.PSongPath);
        values.put(DBManager_History.playl_list_name,playListName);

        long id = dbManager.Insert_table2(values);
        if (id > 0) {
            //Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_LONG).show();
            totalV=1;
           CollectPlayList();
        } else {
            Toast.makeText(getContext(), "Error Please clear app data", Toast.LENGTH_LONG).show();
        }
    }
}