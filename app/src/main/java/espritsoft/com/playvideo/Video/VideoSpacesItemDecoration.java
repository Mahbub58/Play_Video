package espritsoft.com.playvideo.Video;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

//padinr top and bottom==================================
public class VideoSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public VideoSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isLast = position == state.getItemCount()-1;
        if(isLast){
            outRect.bottom = space;
            outRect.top = 0; //don't forget about recycling...
        }
        //top pading
//        if(position == 0){
//            outRect.top = space;
//            // don't recycle bottom if first item is also last
//            // should keep bottom padding set above
//            if(!isLast)
//                outRect.bottom = 0;
//        }
    }
}