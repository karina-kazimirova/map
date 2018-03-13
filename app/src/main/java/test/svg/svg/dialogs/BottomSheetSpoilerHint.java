package test.svg.svg.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import test.svg.svg.Controller;
import test.svg.svg.DateDisplay;
import test.svg.svg.R;
import test.svg.svg.entities.Event;


public class BottomSheetSpoilerHint extends BottomSheetHint {
    private String markerName;
    private TextView nameTextView;
    private TextView date;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private PreviewDialog previewDialog;
    private List<Event> eventList;
    private Event activeEvent;
    private Controller controller;
    private DateDisplay dateDisplay;


    public BottomSheetSpoilerHint(@NonNull Context context, String markerName,
                                  PreviewDialog previewDialog, List<Event> eventList) {
        super(context, markerName, previewDialog);
        this.markerName = markerName;
        this.previewDialog = previewDialog;
        this.eventList = eventList;
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
        bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_popup, null);
        setContentView(bottomSheet);

        nameTextView = (TextView) findViewById(R.id.markerName);
        nameTextView.setText(markerName);

        date = (TextView) findViewById(R.id.event_date);

        controller = new Controller();
        activeEvent = controller.getActiveEvent(eventList);

        initializeDate();

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

    private void initializeDate() {
        if (activeEvent == null) {
            dateDisplay = DateDisplay.DATE_INACTIVE;
            date.setText(getContext().getResources().getString(R.string.noActiveEvent));
        } else {
            dateDisplay = DateDisplay.DATE_ACTIVE;
            // The following code for testing purposes only.
            // -->
            date.setText("24 марта 2018 года, суббота");
            // <--
            initializeViews();
        }
        date.setTextColor(getContext().getResources().getColor(dateDisplay.getTextColor()));
        date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources()
                .getDimension(dateDisplay.getTextSize()));
    }

    private void initializeViews() {

    }
}
