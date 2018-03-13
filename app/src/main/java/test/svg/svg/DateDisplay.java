package test.svg.svg;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DateDisplay {
    DATE_ACTIVE(R.dimen.map_date_active, R.color.map_date_color_inactive),
    DATE_INACTIVE(R.dimen.map_date_active, R.color.black);

    private final int textSize;
    private final int textColor;

    private static final Map<Integer, DateDisplay> TEXT_COLOR = new HashMap<>();
    private static final Map<Integer, DateDisplay> TEXT_SIZE = new HashMap<>();

    static {
        for (final DateDisplay indexes: EnumSet.allOf(DateDisplay.class)) {
            TEXT_COLOR.put(indexes.textColor, indexes);
        }
    }

    static {
        for (final DateDisplay indexes: EnumSet.allOf(DateDisplay.class)) {
            TEXT_SIZE.put(indexes.textSize, indexes);
        }
    }

    DateDisplay(int textSize, int textColor) {
        this.textSize = textSize;
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
    }

}
