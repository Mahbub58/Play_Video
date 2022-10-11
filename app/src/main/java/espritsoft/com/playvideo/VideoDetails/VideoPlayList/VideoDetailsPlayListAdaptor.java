package espritsoft.com.playvideo.VideoDetails.VideoPlayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.Video.VideoModel;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsAdaptor;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsFragment;

public class VideoDetailsPlayListAdaptor extends RecyclerView.Adapter<VideoDetailsPlayListAdaptor.ItemViewHolder> {

    List<VideoModel>mData;
    Context context;

    public VideoDetailsPlayListAdaptor(List<VideoModel> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(context).inflate(R.layout.video_details_video_view_item,parent,false);

        return new ItemViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {


             String formate;
            String pth=mData.get(position).getPath();
            formate=pth.substring(pth.lastIndexOf("."));

        if(!formate.equals(".mp3")) {
            Glide.with(context).load("file://" + mData.get(position).getAlbumArtUriImage())
                    .skipMemoryCache(false)
                    .into(holder.Album_Cover);

            //  Toast.makeText(mContext,""+mDataSonglist.get(position).getAlbumArtUriImage(),Toast.LENGTH_SHORT).show();

            //  holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getAlbumArtUriImage()));

//        holder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        holder.rl_select.setAlpha(0);

            if (VideoDetailsFragment.getInstance().songPosition == position) {
                holder.itemRoot.setVisibility(View.VISIBLE);
            } else holder.itemRoot.setVisibility(View.INVISIBLE);

            holder.tv_title.setText(mData.get(position).getSong_name());
            holder.tv_Album.setText(mData.get(position).getFolderName());
            holder.Song_duration.setText(mData.get(position).getMunite() + ":" + mData.get(position).getSecond());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public onItemClickListner listner;
    public interface onItemClickListner{
        void itemClick(int position,VideoModel videoModel);
        void menu(int position);
    }
    public void setOnItemClickListner(onItemClickListner mlistner){
        listner=(onItemClickListner) mlistner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_Artist,tv_Album,Song_duration;
        ImageView Album_Cover,VideoIcon;
        ImageButton songListMenu;
        TextView itemRoot;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemRoot= itemView.findViewById(R.id.txShow);
            tv_title= itemView.findViewById(R.id.VideoTitel);
            tv_Album=itemView.findViewById(R.id.VideoBody);
            Song_duration=itemView.findViewById(R.id.duration);

            songListMenu=itemView.findViewById(R.id.VideoMore);
            VideoIcon=itemView.findViewById(R.id.VideoIcon);
            Album_Cover=itemView.findViewById(R.id.videoImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listner.itemClick(position,mData.get(position));
                        }
                    }
                }
            });

            songListMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listner.menu(position);
                        }
                    }

                }
            });

        }
    }
}
