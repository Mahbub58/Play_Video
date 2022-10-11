package espritsoft.com.playvideo.Video;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import espritsoft.com.playvideo.DBManager.Model.History;
import espritsoft.com.playvideo.R;

import static java.security.AccessController.getContext;

public class VideoAdaptor extends RecyclerView.Adapter<VideoAdaptor.NewsViewHolder>  {

    Context mContext;
    List<VideoModel> mDataSonglist;
    private OnItemClickListner mListner;



    public interface OnItemClickListner{
        void onItemClick(VideoModel videoModel);
        void menudialog(VideoModel videoModel,View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;

    }

    public VideoAdaptor(Context mContext) {
        this.mContext = mContext;
    }
    public void setList(List<VideoModel>mDataSonglist){
        this.mDataSonglist=mDataSonglist;
        notifyDataSetChanged();
    }



    View layout;
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        int orientation =mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_view_video, viewGroup, false);
        } else {
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_view_video_potrait, viewGroup, false);
        }

        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        //bind data heare

        //    holder.Album_Cover.setImageResource(R.drawable.ic_music_note_white_24dp);

        /** Set Albub Cover**/
     /*   final String albumArtUri=mDataSonglist.get(position).albumArtUriImage;
        int SPLASH_TIME_OUT = 400;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            mContext.getContentResolver(), Uri.parse(albumArtUri));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
                    holder.Album_Cover.setImageBitmap(bitmap);

                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.logo);
                    holder.Album_Cover.setImageBitmap(bitmap);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }, SPLASH_TIME_OUT);



        holder.tv_title.setText(mDataSonglist.get(position).getSong_name());
        holder.tv_Artist.setText(mDataSonglist.get(position).getArtist_name());
        holder.Song_duration.setText(mDataSonglist.get(position).getSecond()+":"+mDataSonglist.get(position).getMunite());
        holder.tv_Album.setText(mDataSonglist.get(position).getAlbam_name());
 */

//
//        Glide.with(mContext).load(mDataSonglist.get(position).albumArtUriImage).
//                placeholder(R.drawable.image_1).dontAnimate().
//                into(holder.Album_Cover);

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

            Album_Cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.onItemClick(mDataSonglist.get(position));
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