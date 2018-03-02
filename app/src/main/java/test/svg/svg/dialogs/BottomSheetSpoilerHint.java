package test.svg.svg.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import test.svg.svg.R;

public class BottomSheetSpoilerHint extends BottomSheetHint {
    private String markerName;
    private TextView nameTextView;
    private BottomSheetBehavior bottomSheetBehavior;
    private PreviewDialog previewDialog;

    public BottomSheetSpoilerHint(@NonNull Context context, String markerName,
                                  PreviewDialog previewDialog) {
        super(context, markerName, previewDialog);
        this.markerName = markerName;
        this.previewDialog = previewDialog;
        init();
    }

    public BottomSheetSpoilerHint(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        final View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_popup,
                null);
        setContentView(bottomSheet);
        setCanceledOnTouchOutside(true);

        nameTextView = (TextView) findViewById(R.id.markerName);
        nameTextView.setText(markerName);

        previewDialog.show();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View)
                bottomSheet.getParent()).getLayoutParams();
        bottomSheetBehavior = (BottomSheetBehavior) params.getBehavior();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(144);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                previewDialog.dismiss();
            }
        });
    }
}
