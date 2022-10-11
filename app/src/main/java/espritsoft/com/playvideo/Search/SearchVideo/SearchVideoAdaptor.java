package espritsoft.com.playvideo.Search.SearchVideo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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

public class SearchVideoAdaptor extends RecyclerView.Adapter<SearchVideoAdaptor.NewsViewHolder> {

    List<VideoModel>mDataVideoList=new ArrayList<>();
    Context context;

    public SearchVideoAdaptor(Context context) {
        this.context=context;
    }

    public void setList(List<VideoModel>mDataSonglist){
        this.mDataVideoList=mDataSonglist;
        notifyDataSetChanged();
    }

    View view;
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view=LayoutInflater.from(context).inflate(R.layout.item_view_video_potrait,parent,false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        Glide.with(context).load("file://"+mDataVideoList.get(position).getAlbumArtUriImage())
                .skipMemoryCache(false)
                .into(holder.Album_Cover);


        holder.tv_title.setText(mDataVideoList.get(position).getSong_name());
        holder.tv_Album.setText(mDataVideoList.get(position).getFolderName());
        holder.Song_duration.setText(mDataVideoList.get(position).getMunite()+":"+mDataVideoList.get(position).getSecond());

    }

    @Override
    public int getItemCount() {
        return mDataVideoList.size();
    }


    public interface onItemClickListner{
        void onItemClickListner(VideoModel videoModel);
    }
    public onItemClickListner mlistner;
    public void onItemClickListner(onItemClickListner listner){
       mlistner=listner;
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {

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
                    if(mlistner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mlistner.onItemClickListner(mDataVideoList.get(position));
                        }
                    }
                }
            });

        }
    }


    //search
    // search
  public void filterList(ArrayList<VideoModel>filteredList){
        mDataVideoList=filteredList;
        notifyDataSetChanged();
  }


}
