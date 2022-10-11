package espritsoft.com.playvideo.Search.SearchAudio;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.R;


public class SearchAudioAdaptor extends RecyclerView.Adapter<SearchAudioAdaptor.NewsViewHolder> {

    List<AudiouModel>mDataAudioList=new ArrayList<>();

    public SearchAudioAdaptor(List<AudiouModel> mDataAudioList) {
        this.mDataAudioList = mDataAudioList;
    }

    View view;
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_view,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        holder.Album_Cover.setImageURI(Uri.parse(mDataAudioList.get(position).getAlbumArtUriImage()));
        holder.tv_title.setText(mDataAudioList.get(position).getSong_name());
        holder.tv_Album.setText(mDataAudioList.get(position).getFolderName());
        holder.Song_duration.setText(mDataAudioList.get(position).getMunite()+":"+mDataAudioList.get(position).getSecond());
    }

    @Override
    public int getItemCount() {
        return mDataAudioList.size();
    }

    public void Filter(ArrayList<AudiouModel> filteredList) {
        mDataAudioList=filteredList;
        notifyDataSetChanged();
    }



    public void setOnItemClickListner(onClickListner listner){
        mListner=listner;
    }

    public interface onClickListner{
       void onClickListner(int position);
    }
    private onClickListner mListner;

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_Artist,tv_Album,Song_duration;
        ImageView Album_Cover,VideoIcon,backgroundImage;
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
                            mListner.onClickListner(position);
                        }
                    }
                }
            });
        }
    }
}
