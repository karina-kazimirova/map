package test.svg.svg.ui;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import test.svg.svg.PhotoDialogBackground;
import test.svg.svg.R;

public class MarkerPreview extends View{
    private String photoUrl;
    private ImageView imageView;
    private Window window;
    private WindowManager.LayoutParams layoutParams;
    private float locationX;
    private float locationY;

    public MarkerPreview(Context context) {
        super(context);
        init();
    }

    public MarkerPreview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkerPreview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.photo_preview);
        Glide
                .with(getContext())
                .load(photoUrl)
                .override(660, 414)
                .centerCrop()
                .into(imageView);
    }

    private void setBackground(Window window) {
        PhotoDialogBackground photoDialogBackground = PhotoDialogBackground.DOWN;
        layoutParams = window.getAttributes();
        int viewHeight = (int) getContext().getResources().getDimension(R.dimen.photo_dialog_height);
        int viewWidth = (int) getContext().getResources().getDimension(R.dimen.photo_dialog_width);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        layoutParams.y = (int) locationY - viewHeight;

        if (locationY < getContext().getResources().getDisplayMetrics().heightPixels / 2) {
            photoDialogBackground = PhotoDialogBackground.UP;
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            layoutParams.y = (int) locationY;
        }
        setBackgroundResource(photoDialogBackground.getId());
        window.setAttributes(layoutParams);
    }


}
