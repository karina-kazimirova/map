package test.svg.svg;

import java.util.ArrayList;
import java.util.List;

import test.svg.svg.entities.MapDrawableIcon;


public class Controller {
    // convert received arraylist to drawable array
    private List<MapDrawableIcon> mapDrawableList;

    public List<MapDrawableIcon> getList() {
        // The following code for testing purposes only.
        // -->
        mapDrawableList = new ArrayList<>();
        mapDrawableList.add(new MapDrawableIcon(0.12f, 0.32f,
                0.84f,"http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ", true));
        mapDrawableList.add(new MapDrawableIcon(0.12f, 0.76f,
                1.0f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "restroom", "Туалет", false));
        mapDrawableList.add(new MapDrawableIcon(0.26f, 0.66f,
                0.93f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ", true));
        mapDrawableList.add(new MapDrawableIcon(0.56f, 0.46f,
                0.68f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pharma", "Аптека", false));
        mapDrawableList.add(new MapDrawableIcon(0.16f, 0.16f,
                0.82f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ", true));
        // <--
        return mapDrawableList;
    }
}
