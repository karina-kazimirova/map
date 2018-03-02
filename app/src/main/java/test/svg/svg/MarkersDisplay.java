package test.svg.svg;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MarkersDisplay {
    POINTER(R.mipmap.icon_pointer, "pointer"),
    CAFE(R.mipmap.icon_pointer, "cafe"),
    RESTROOM(R.mipmap.icon_wc, "restroom"),
    PHARMA(R.mipmap.icon_pharma, "pharma");

    private static final Map<Integer, MarkersDisplay> INDEX = new HashMap<>();
    private static final Map<String, MarkersDisplay> TYPE = new HashMap<>();

    private final int markerId;
    private final String markerType;

    static {
        for (final MarkersDisplay indexes: EnumSet.allOf(MarkersDisplay.class)) {
            INDEX.put(indexes.markerId, indexes);
        }
    }
    static {
        for (final MarkersDisplay indexes: EnumSet.allOf(MarkersDisplay.class)) {
            TYPE.put(indexes.markerType, indexes);
        }
    }

    MarkersDisplay(int markerId, String markerType) {
        this.markerId = markerId;
        this.markerType = markerType;
    }

    public static MarkersDisplay getIndexById(final int markerId) {
        return INDEX.get(markerId);
    }
    public static MarkersDisplay getIndexByType(final String markerType) {
        return TYPE.get(markerType);
    }

    public int getMarkerId() {
        return markerId;
    }
    public String getMarkerType() {
        return markerType;
    }
}
