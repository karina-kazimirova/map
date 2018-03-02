package test.svg.svg;

public enum PhotoDialogBackground {
    UP(R.layout.photo_dialog),
    DOWN(R.layout.photo_dialog_down);

    private int id;

    PhotoDialogBackground(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
