package espritsoft.com.playvideo.AditionalClass;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingDecorationVertical extends RecyclerView.ItemDecoration {
    final int verticalSpaceHight;
    final int horizantallSpaceHight;
    public SpacingDecorationVertical(int verticalSpaceHight,int horizantelSpeceHight){
        this.verticalSpaceHight=verticalSpaceHight;
        this.horizantallSpaceHight=horizantelSpeceHight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom=verticalSpaceHight;
        outRect.left =horizantallSpaceHight;
    }
}
