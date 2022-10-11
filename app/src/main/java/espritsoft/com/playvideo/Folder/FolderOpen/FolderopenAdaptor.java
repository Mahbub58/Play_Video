package espritsoft.com.playvideo.Folder.FolderOpen;

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
import java.util.List;

import espritsoft.com.playvideo.Folder.FolderOpen.Model.FolderItemModel;
import espritsoft.com.playvideo.R;
import espritsoft.com.playvideo.VideoDetails.VideoDetailsModel;

public class FolderopenAdaptor extends RecyclerView.Adapter<FolderopenAdaptor.NewsViewHolder>  {

    Context mContext;
    List<FolderItemModel> mDataSonglist;
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(FolderItemModel folderItemModel);
        void menudialog(FolderItemModel folderItemModel,View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;
    }

    public FolderopenAdaptor(Context mContext) {
        this.mContext = mContext;

    }
    public void setList(List<FolderItemModel>data){
        this.mDataSonglist=data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View layout;
        layout= LayoutInflater.from(mContext).inflate(R.layout.item_view_video_from_folder_open,viewGroup,false);

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

        String  recpintList=mDataSonglist.get(position).getPath();
        String extension = recpintList.substring(recpintList.lastIndexOf("."));
        if(!extension.equals(".mp3")){
            Glide.with(mContext).load("file://"+mDataSonglist.get(position).getAlbumArtUriImage())
                    .skipMemoryCache(false)
                    .into(holder.Album_Cover);
        }else{
            holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getAlbumArtUriImage()));
        }
      //  holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getAlbumArtUriImage()));

//        holder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        holder.rl_select.setAlpha(0);

        holder.tv_title.setText(mDataSonglist.get(position).getSong_name());
       // holder.tv_Album.setText(mDataSonglist.get(position).getAlbam_name());
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
          //  tv_Album=itemView.findViewById(R.id.VideoBody);
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
                            mListner.menudialog((mDataSonglist.get(position)),v);
                        }
                    }

                }
            });


        }
    }


}