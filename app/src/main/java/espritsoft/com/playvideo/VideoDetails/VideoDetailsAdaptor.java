package espritsoft.com.playvideo.VideoDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoModel;

public class VideoDetailsAdaptor extends RecyclerView.Adapter<VideoDetailsAdaptor.NewsViewHolder>  {

    Context mContext;
    List<VideoModel> mDataSonglist;
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position,VideoModel videoModel);
        void menudialog(VideoModel videoModel,View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;
    }

    public VideoDetailsAdaptor(Context mContext) {
        this.mContext = mContext;
    }
    public void setList(List<VideoModel>data){
        this.mDataSonglist=data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(mContext).inflate(R.layout.video_details_video_view_item,viewGroup,false);

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        //bind data heare


        Glide.with(mContext).load("file://"+mDataSonglist.get(position).getAlbumArtUriImage())
                .skipMemoryCache(false)
                .into(holder.Album_Cover);

      //  Toast.makeText(mContext,""+mDataSonglist.get(position).getAlbumArtUriImage(),Toast.LENGTH_SHORT).show();

      //  holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getAlbumArtUriImage()));

//        holder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        holder.rl_select.setAlpha(0);

        holder.tv_title.setText(mDataSonglist.get(position).getSong_name());
        holder.tv_Album.setText(mDataSonglist.get(position).getFolderName());
        if(Integer.parseInt(mDataSonglist.get(position).getHour()) != 0)
            holder.Song_duration.setText(mDataSonglist.get(position).getHour()+":"+mDataSonglist.get(position).getMunite()+":"+mDataSonglist.get(position).getSecond());
        else if(Integer.parseInt(mDataSonglist.get(position).getMunite()) != 0)
            holder.Song_duration.setText(mDataSonglist.get(position).getMunite()+":"+mDataSonglist.get(position).getSecond());
        else holder.Song_duration.setText(mDataSonglist.get(position).getSecond());
    }

    @Override
    public int getItemCount() {
        return mDataSonglist.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{


        TextView tv_title,tv_Artist,tv_Album,Song_duration;
        ImageView Album_Cover,VideoIcon;
        ImageButton songListMenu;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

           tv_title= itemView.findViewById(R.id.VideoTitel);
            tv_Album=itemView.findViewById(R.id.VideoBody);
            Song_duration=itemView.findViewById(R.id.duration);

            songListMenu=itemView.findViewById(R.id.VideoMore);
            VideoIcon=itemView.findViewById(R.id.VideoIcon);
            Album_Cover=itemView.findViewById(R.id.videoImage);

            itemView.setOnClickListener(new View.OnClickListener() {
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

            songListMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.menudialog(mDataSonglist.get(position),v);
                        }
                    }

                }
            });


        }
    }


}