package test.svg.svg.dialogs;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import test.svg.svg.R;

public class MapDialogFragment extends DialogFragment {
    private GestureDetector gestureDetector;
    private Window window;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params = window.getAttributes();
        params.y = (int) getContext().getResources().getDimension(R.dimen.photo_dialog_margin_bottom);
        window.setAttributes(params);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        View root = inflater.inflate(R.layout.popup, container, false);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean eventConsumed = gestureDetector.onTouchEvent(event);
                if (eventConsumed) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        window.setCallback(windowCallback);
        return root;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 180;
        private static final int SWIPE_THRESHOLD_VELOCITY = 220;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
           if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                dismiss();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private Window.Callback windowCallback = new Window.Callback() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                getDialog().dismiss();
            }
            return false;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent event) {
            return false;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (gestureDetector != null) {
                return gestureDetector.onTouchEvent(event);
            }
            return false;
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
            return false;
        }

        @Nullable
        @Override
        public View onCreatePanelView(int featureId) {
            return null;
        }

        @Override
        public boolean onCreatePanelMenu(int featureId, Menu menu) {
            return false;
        }

        @Override
        public boolean onPreparePanel(int featureId, View view, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuOpened(int featureId, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuItemSelected(int featureId, MenuItem item) {
            return false;
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) { }

        @Override
        public void onContentChanged() { }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) { }

        @Override
        public void onAttachedToWindow() { }

        @Override
        public void onDetachedFromWindow() { }

        @Override
        public void onPanelClosed(int featureId, Menu menu) { }

        @Override
        public boolean onSearchRequested() {
            return false;
        }

        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Nullable
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
            return null;
        }

        @Override
        public void onActionModeStarted(ActionMode mode) { }

        @Override
        public void onActionModeFinished(ActionMode mode) { }
    };
}


