package espritsoft.com.playvideo.HistoryLibrary.PlayList_open;


import android.content.Context;
import android.net.Uri;
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

import espritsoft.com.playvideo.HistoryLibrary.historyItemModul;
import espritsoft.com.playvideo.R;

public class PlayListOpenAdaptor extends RecyclerView.Adapter<PlayListOpenAdaptor.NewsViewHolder>  {

    Context mContext;
    ArrayList<historyItemModul> mDataSonglist;
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position);
        void menudialog(int position,View v);
    }

    public void setOnItemClickListner(PlayListOpenAdaptor.OnItemClickListner listner){
        mListner= (PlayListOpenAdaptor.OnItemClickListner) listner;
    }

    public PlayListOpenAdaptor(Context mContext, ArrayList<historyItemModul> mData) {
        this.mContext = mContext;
        this.mDataSonglist = mData;
    }



    @NonNull
    @Override
    public PlayListOpenAdaptor.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(mContext).inflate(R.layout.item_view_video_from_folder_open,viewGroup,false);

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayListOpenAdaptor.NewsViewHolder holder, int position) {

        String  recpintList=mDataSonglist.get(position).getSong_path();
//        String[] recpints=(recpintList.split("."));
//        String songtype=recpints[0];
        String extension = recpintList.substring(recpintList.lastIndexOf("."));
        if(!extension.equals(".mp3")){
            Glide.with(mContext).load("file://"+mDataSonglist.get(position).getSong_album_cover())
                    .skipMemoryCache(false)
                    .into(holder.Album_Cover);
        }else{
            holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getSong_album_cover()));
        }


        //  Toast.makeText(mContext,""+mDataSonglist.get(position).getAlbumArtUriImage(),Toast.LENGTH_SHORT).show();

        //

//        holder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        holder.rl_select.setAlpha(0);

        holder.tv_title.setText(mDataSonglist.get(position).getSong_name());
     //   holder.tv_Album.setText(mDataSonglist.get(position).getSong_folder());
     //   holder.Song_duration.setText(mDataSonglist.get(position).getMunite()+":"+mDataSonglist.get(position).getSecond());

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
            tv_Album=itemView.findViewById(R.id.duration);
//            Song_duration=itemView.findViewById(R.id.duration);
//
            songListMenu=itemView.findViewById(R.id.VideoMore);
//            VideoIcon=itemView.findViewById(R.id.VideoIcon);
            Album_Cover=itemView.findViewById(R.id.videoImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.onItemClick(position);
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
                            mListner.menudialog(position,v);
                        }
                    }

                }
            });


        }
    }


}