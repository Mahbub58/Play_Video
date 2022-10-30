package espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList.AllSongList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.R;


public class SonglistBottomFragmentAdaptor extends RecyclerView.Adapter<SonglistBottomFragmentAdaptor.NewsViewHolder>  {

    Context mContext;
    List<AudiouModel> mDataSonglist;
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(AudiouModel audiouModel);
        void menudialog(AudiouModel audiouModel,View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;
    }

    public SonglistBottomFragmentAdaptor(Context mContext,List<AudiouModel>mDataSonglist) {
        this.mContext = mContext;
        this.mDataSonglist=mDataSonglist;
    }
    public void setList(List<AudiouModel>audioData){
        this.mDataSonglist=audioData;
    }



    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(mContext).inflate(R.layout.item_view_play_list_item,viewGroup,false);

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    mContext.getContentResolver(), Uri.parse(mDataSonglist.get(position).albumArtUriImage));
            holder.Album_Cover.setImageBitmap(bitmap);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.music);
            holder.Album_Cover.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.tv_title.setText(mDataSonglist.get(position).getSong_name());
        holder.tv_Album.setText(mDataSonglist.get(position).getFolderName());
//        if(Integer.parseInt(mDataSonglist.get(position).getHour()) != 0)
//            holder.Song_duration.setText(mDataSonglist.get(position).getHour()+":"+mDataSonglist.get(position).getMunite()+":"+mDataSonglist.get(position).getSecond());
//        else if(Integer.parseInt(mDataSonglist.get(position).getMunite()) != 0)
//            holder.Song_duration.setText(mDataSonglist.get(position).getMunite()+":"+mDataSonglist.get(position).getSecond());
//        else holder.Song_duration.setText(mDataSonglist.get(position).getSecond());


        for(int i=0; i<AudioPlaySystem.getInstance().queueList.size();i++) {
            if (mDataSonglist.get(position).getSong_name().equals(AudioPlaySystem.getInstance().queueList.get(i).getSong_name())) {
                holder.songListMenu.setChecked(true);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataSonglist.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{


        TextView tv_title,tv_Album;
        ImageView Album_Cover;
        CheckBox songListMenu;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

           tv_title= itemView.findViewById(R.id.playListName);
            tv_Album=itemView.findViewById(R.id.playListTotalSong);

            songListMenu=itemView.findViewById(R.id.checkbox);
            Album_Cover=itemView.findViewById(R.id.playListImage);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mListner!=null){
//                        int position=getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            mListner.onItemClick(mDataSonglist.get(position));
//                        }
//                    }
//                }
//            });

            songListMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.menudialog(mDataSonglist.get(position), v);

                        }
                    }

                }
            });


        }
    }
}