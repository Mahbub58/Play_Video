package espritsoft.com.playvideo.VideoPlay.AudioTracks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoModel;

public class AudioTrackAdaptor extends RecyclerView.Adapter<AudioTrackAdaptor.NewsViewHolder>  {

    Context mContext;
    List<trackModel> mDataSonglist;
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position, trackModel videoModel);
        void menudialog(VideoModel videoModel,View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;
    }


    public AudioTrackAdaptor(Context mContext, List<trackModel> mDataSonglist) {
        this.mContext = mContext;
        this.mDataSonglist = mDataSonglist;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(mContext).inflate(R.layout.audiio_trake_layout,viewGroup,false);

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        //bind data heare


        holder.tv_title.setText(mDataSonglist.get(position).getTrack());

    }

    @Override
    public int getItemCount() {
        return mDataSonglist.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{


        Button tv_title;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

           tv_title= itemView.findViewById(R.id.audio_track_view);


            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.onItemClick(position,mDataSonglist.get(position));
                        }
                    }
                }
            });




        }
    }


}