package espritsoft.com.playvideo.HistoryLibrary;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.R;

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.NewsViewHolder>  {

    Context mContext;
    List<History> mDataSonglist=new ArrayList<>();


    public HistoryAdaptor(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public HistoryAdaptor.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_history,viewGroup,false);

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryAdaptor.NewsViewHolder holder, int position) {


        String  recpintList=mDataSonglist.get(position).getPath();
        try {
            String extension = recpintList.substring(recpintList.lastIndexOf("."));
            if(!extension.equals(".mp3")){
                Glide.with(mContext).load("file://"+mDataSonglist.get(position).getAlbumCover())
                        .skipMemoryCache(false)
                        .into(holder.Album_Cover);
            }else{
                holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getAlbumCover()));
            }
        }catch (Exception e){}

//        String pgs=mDataSonglist.get(position).getProgress();
//        String sTtime=mDataSonglist.get(position).getDuration();
//        String[] sub_sTtime=sTtime.split(":");
//        int second=Integer.parseInt(sub_sTtime[sub_sTtime.length-2]);
//        int minut=Integer.parseInt(sub_sTtime[sub_sTtime.length-1]);
//        int hour=Integer.parseInt(sub_sTtime[0]);
//        int totime=(second*1000)+(minut*60000)+(hour*3600000);
//        int prog=totime/(Integer.parseInt(pgs)/1000);


        holder.progressBar.setProgress(44);
        holder.tv_title.setText(mDataSonglist.get(position).getName());
        holder.tv_Album.setText(mDataSonglist.get(position).getFolder());
        holder.Song_duration.setText(mDataSonglist.get(position).getDuration());

    }

    @Override
    public int getItemCount() {
        return mDataSonglist.size();
    }

    public void setList(List<History>mDataSonglist){
        this.mDataSonglist=mDataSonglist;
        notifyDataSetChanged();
    }
//    public mDataSonglist getListAt(int position){
//        return mDataSonglist.get(position);
//    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{


        TextView tv_title,tv_Artist,tv_Album,Song_duration;
        ImageView Album_Cover,VideoIcon;
        ImageButton songListMenu;
        ProgressBar progressBar;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title= itemView.findViewById(R.id.htitle);
            tv_Album=itemView.findViewById(R.id.hfolder);
            Song_duration=itemView.findViewById(R.id.duration);
//
            songListMenu=itemView.findViewById(R.id.hMore);
//            VideoIcon=itemView.findViewById(R.id.VideoIcon);
            Album_Cover=itemView.findViewById(R.id.hImage);
            progressBar=itemView.findViewById(R.id.progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(listner!=null&& position!= RecyclerView.NO_POSITION)
                        listner.onItemClickListner(mDataSonglist.get(position));

                }
            });
            songListMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(listner!=null&& position!= RecyclerView.NO_POSITION)
                        listner.menu(mDataSonglist.get(position),v);

                }
            });

        }
    }

    public interface onItemClickListner{
        void onItemClickListner(History note);
        void menu(History history,View v);
    }
    private onItemClickListner listner;
    public void setOnItemClickListner(onItemClickListner listner){
        this.listner=listner;
    }
}