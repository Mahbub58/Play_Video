package espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.AllSongList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import espritsoft.com.playvideo.Audio.AudioAdaptor;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudioPlay;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.HomeActivity.Home;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoSpacesItemDecoration;

public class SongListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        intiLayout();


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);



    }

    SonglistBottomFragmentAdaptor songlistBottomFragmentAdaptor;
    private void intiLayout() {

        RecyclerView recyclerView=findViewById(R.id.recyclerViewSongList);
        songlistBottomFragmentAdaptor =new SonglistBottomFragmentAdaptor(getApplicationContext(), Home.getInstance().AudiouSongsList);
        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                getResources().getDisplayMetrics()); // calculated
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.addItemDecoration(new VideoSpacesItemDecoration(space));
        recyclerView.setAdapter(songlistBottomFragmentAdaptor);



        if(songlistBottomFragmentAdaptor!=null){
            songlistBottomFragmentAdaptor.setOnItemClickListner(new SonglistBottomFragmentAdaptor.OnItemClickListner() {
                @Override
                public void onItemClick(AudiouModel audiouModel) {

                }

                @Override
                public void menudialog(AudiouModel audiouModel, View v) {
                    boolean isEnque=true;
                   for(int i=0; i<AudioPlaySystem.getInstance().queueList.size();i++) {
                       if (audiouModel.getSong_name().equals(AudioPlaySystem.getInstance().queueList.get(i).getSong_name())) {
                           AudioPlaySystem.getInstance().queueList.remove(i);
                           isEnque=false;
                           break;
                       }
                   }
                   if(isEnque){
                       AudioPlaySystem.getInstance().queueList.add(audiouModel);
                   }
                }
            });
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}