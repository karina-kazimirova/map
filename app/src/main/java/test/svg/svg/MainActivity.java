package test.svg.svg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import test.svg.svg.dialogs.MapDialogFragment;
import test.svg.svg.svgandroid_library.SVG;
import test.svg.svg.svgandroid_library.SVGParser;

public class MainActivity extends AppCompatActivity {
    private ZoomView zoomView;
    private SVG svg;
    private MapDialogFragment mapDialogFragment;
    private ScrollTouchImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svg = SVGParser.getSVGFromResource(getResources(), R.raw.map3);
        imageView = new ScrollTouchImageView(this, svg.createPictureDrawable());
        zoomView = new ZoomView(this, svg.createPictureDrawable());
        setContentView(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapDialogFragment == null) {
            mapDialogFragment = new MapDialogFragment();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    mapDialogFragment.show(getFragmentManager(), MapDialogFragment.class.getName());
                    t.cancel();
                }
            }, 500);
        }
    }
}


