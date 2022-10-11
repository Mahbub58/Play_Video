package espritsoft.com.playvideo.AudioPlay.inerLayout.SongQueList;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingDecoration extends RecyclerView.ItemDecoration {
    final int verticalSpaceHight;
    public SpacingDecoration(int verticalSpaceHight){
        this.verticalSpaceHight=verticalSpaceHight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom=verticalSpaceHight;
    }
}
