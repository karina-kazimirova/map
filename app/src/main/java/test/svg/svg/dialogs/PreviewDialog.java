package test.svg.svg.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import test.svg.svg.PhotoDialogBackground;
import test.svg.svg.R;


public class PreviewDialog extends Dialog {
    private String photoUrl;
    private ImageView imageView;
    private Window window;
    private WindowManager.LayoutParams layoutParams;
    private float locationX;
    private float locationY;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PreviewDialog(@NonNull Context context, String photoUrl, float locationX,
                         float locationY) {
        super(context, R.style.CustomDialogWhite);
        this.photoUrl = photoUrl;
        this.locationX = locationX;
        this.locationY = locationY;
        init();
    }

    public PreviewDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.CustomDialogWhite);
        init();
    }

    protected PreviewDialog(@NonNull Context context, boolean cancelable,
                            @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        window = getWindow();
        setBackground(window);
        imageView = (ImageView) findViewById(R.id.photo_preview);
        float imageWidth = getContext().getResources().getDimension(R.dimen.photo_dialog_img_width);
        int imageWidthDp = (int) (imageWidth * getContext().getResources().getDisplayMetrics().density);
        float imageHeight  = getContext().getResources().getDimension(R.dimen.photo_dialog_img_height);
        int imageHeightDp = (int) (imageHeight * getContext().getResources().getDisplayMetrics().density);
        Glide
                .with(getContext())
                .load(photoUrl)
                .override(imageWidthDp, imageHeightDp)
                .centerCrop()
                .into(imageView);
    }

    private void setBackground(Window window) {
        PhotoDialogBackground photoDialogBackground = PhotoDialogBackground.DOWN;
        layoutParams = window.getAttributes();
        int viewHeight = (int) getContext().getResources().getDimension(R.dimen.photo_dialog_height);
        int viewWidth = (int) getContext().getResources().getDimension(R.dimen.photo_dialog_width);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        int displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        layoutParams.y = (int) locationY - viewHeight;

        if (locationY < getContext().getResources().getDisplayMetrics().heightPixels / 2) {
            photoDialogBackground = PhotoDialogBackground.UP;
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            layoutParams.y = (int) locationY;
        }

        setContentView(photoDialogBackground.getId());
        window.setAttributes(layoutParams);
    }
}