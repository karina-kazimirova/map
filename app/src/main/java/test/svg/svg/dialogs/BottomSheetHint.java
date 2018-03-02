package test.svg.svg.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.widget.TextView;
import test.svg.svg.R;

public class BottomSheetHint extends BottomSheetDialog {
    private String markerName;
    private TextView nameTextView;
    private PreviewDialog previewDialog;

    public BottomSheetHint(@NonNull Context context, String markerName,
                           PreviewDialog previewDialog) {
        super(context, R.style.CustomBottomSheetStyle);
        this.markerName = markerName;
        this.previewDialog = previewDialog;
        init();
    }

    public BottomSheetHint(@NonNull Context context, int theme) {
        super(context, R.style.CustomBottomSheetStyle);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        setContentView(R.layout.bottom_sheet);
        setCanceledOnTouchOutside(true);
        nameTextView = (TextView) findViewById(R.id.markerName);
        nameTextView.setText(markerName);
        previewDialog.show();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                previewDialog.dismiss();
            }
        });
    }
}

