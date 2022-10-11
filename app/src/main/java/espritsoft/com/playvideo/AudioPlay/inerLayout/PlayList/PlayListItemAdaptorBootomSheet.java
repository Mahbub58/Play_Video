package espritsoft.com.playvideo.AudioPlay.inerLayout.PlayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import espritsoft.com.playvideo.AditionalClass.PlayListDataForSave;
import espritsoft.com.playvideo.Audio.AudiouModel;
import espritsoft.com.playvideo.AudioPlay.AudiouServices.AudioPlaySystem;
import espritsoft.com.playvideo.R;


public class PlayListItemAdaptorBootomSheet extends RecyclerView.Adapter<PlayListItemAdaptorBootomSheet.NewsViewHolder>  {

    Context mContext;
    List<ModulPlayListItem> mDataSonglist;
    private OnItemClickListner mListner;

    /** search
     /*  public Filter getFilter() {
     return searchfilter;
     }

     private Filter searchfilter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
    List<customItem> filteredlist = new ArrayList<>();
    if (constraint == null || constraint.length() == 0) {
    filteredlist.addAll(Search.mDataSarch);
    } else {
    String filterPatern = constraint.toString().toLowerCase().trim();
    for (customItem item : Search.mDataSarch) {
    if (item.song_name.toLowerCase().contains(filterPatern)) {
    filteredlist.add(item);
    }
    }
    }
    FilterResults results = new FilterResults();
    results.values = filteredlist;
    return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    Search.mDataLoadedSarch.clear();
    Search.mDataLoadedSarch.addAll((List) results.values);
    Menu_SearchHome.adaptorsearch.notifyDataSetChanged();


    }
    };
     **/


    public interface OnItemClickListner{
        void onItemClick(int position);
        void menudialog(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner= (OnItemClickListner) listner;
    }

    public PlayListItemAdaptorBootomSheet(Context mContext, ArrayList<ModulPlayListItem> mData) {
        this.mContext = mContext;
        this.mDataSonglist = mData;
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
        //bind data heare


        String  recpintList=mDataSonglist.get(position).getPlayList_song_path();
        String extension = recpintList.substring(recpintList.lastIndexOf("."));
        if(extension.equals(".mp4")||extension.equals(".mkv")||extension.equals(".3gp")){
            Glide.with(mContext).load("file://"+mDataSonglist.get(position).getPlayListCover())
                    .skipMemoryCache(false)
                    .into(holder.Album_Cover);
        }else{
            holder.Album_Cover.setImageURI(Uri.parse(mDataSonglist.get(position).getPlayListCover()));
        }


        holder.tv_title.setText(mDataSonglist.get(position).getPlayListName());
        holder.total.setText(mDataSonglist.get(position).getPlayListTotal()+" Videos");

        for(int i=position;i<PlayListFragment.getInstance().PlayListItem.size();i++){
            if(PlayListFragment.getInstance().PlayListItem.get(i).getSong_name().equals(PlayListDataForSave.PName)
            && PlayListFragment.getInstance().PlayListItem.get(i).getPlayListName().equals(mDataSonglist.get(position).getPlayListName())){
                holder.checkBox.setChecked(true);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mDataSonglist.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{


        TextView tv_title,total,tv_Album,Song_duration;
        ImageView Album_Cover,VideoIcon,backgroundImage;
        ImageButton songListMenu,playitem;
        ConstraintLayout itemview;
        CheckBox checkBox;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

           tv_title= itemView.findViewById(R.id.playListName);
            Album_Cover= itemView.findViewById(R.id.playListImage);
            total= itemView.findViewById(R.id.playListTotalSong);

            itemview=itemView.findViewById(R.id.itemViewplayList);
            checkBox=itemView.findViewById(R.id.checkbox);

            itemview.setOnClickListener(new View.OnClickListener() {
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
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.menudialog(position);
                        }
                    }
                }
            });




        }
    }


}