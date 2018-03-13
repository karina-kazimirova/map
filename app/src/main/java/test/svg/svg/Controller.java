package test.svg.svg;

import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import test.svg.svg.entities.Event;
import test.svg.svg.entities.Marker;


public class Controller {
    private List<Marker> markersList;
    private List<Event> eventList;


    public List<Marker> getList() {
        // The following code for testing purposes only.
        // -->
        markersList = new ArrayList<>();
        markersList.add(new Marker(0.12f, 0.32f,
                0.84f,"http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ1", true, 1));
        markersList.add(new Marker(0.12f, 0.76f,
                1.0f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "restroom", "Туалет", false, 2));
        markersList.add(new Marker(0.26f, 0.66f,
                0.93f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ2", true, 3));
        markersList.add(new Marker(0.56f, 0.46f,
                0.68f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pharma", "Аптека", false, 4));
        markersList.add(new Marker(0.16f, 0.16f,
                0.82f, "http://foto.cheb.ru/foto/foto.cheb.ru-27156.jpg",
                "pointer", "Холл БНТУ3", true, 5));
        // <--
        return markersList;
    }

    public List<Event> getEventByMarkerId(int markerId) {
        // The following code for testing purposes only.
        // -->

        eventList = new ArrayList<>();
        // get List from DB, call the ProgramScreenModel method
        //programScreenModel.getEventsList(markerId);

        // initializing dates
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        Date date5 = null;

        try {
            date = dateFormat.parse("05/02/2018 09:00");
            date1 = dateFormat.parse("05/04/2018 10:00");
            date2 = dateFormat.parse("05/04/2018 12:00");
            date3 = dateFormat.parse("05/04/2018 14:00");
            date4 = dateFormat.parse("05/04/2018 20:00");
            date5 = dateFormat.parse("05/04/2018 21:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp timestamp = new Timestamp(date.getTime());
        Timestamp timestamp1 = new Timestamp(date1.getTime());
        Timestamp timestamp2 = new Timestamp(date2.getTime());
        Timestamp timestamp3 = new Timestamp(date3.getTime());
        Timestamp timestamp4 = new Timestamp(date4.getTime());
        Timestamp timestamp5 = new Timestamp(date5.getTime());


        // initializing category names
        ArrayList<String> categoryNamesList = new ArrayList<>();
        categoryNamesList.add("Minsk");
        categoryNamesList.add("Science");
        categoryNamesList.add("Professions");

        // Populating list items
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ1",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp5, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ САМЫЕ НОВЫЕ ЯБЛОЧКИ-ПЯТКИ2",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ1",
                "Встреча с Орловой Анастасией", timestamp2,
                timestamp3, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ САМЫЕ НОВЫЕ ЯБЛОЧКИ-ПЯТКИ2",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ3",
                "Встреча с Орловой Анастасией", timestamp2,
                timestamp3, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ4",
                "Встреча с Орловой Анастасией", timestamp4,
                timestamp5, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ5",
                "Встреча с Орловой Анастасией", timestamp4,
                timestamp5, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ6",
                "Встреча с Орловой Анастасией", timestamp4,
                timestamp5, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ7",
                "Встреча с Орловой Анастасией", timestamp2,
                timestamp3, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ8",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ9",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ8",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        eventList.add(new Event("НАСТОЯЩИЕ ЯБЛОЧКИ-ПЯТКИ9",
                "Встреча с Орловой Анастасией", timestamp,
                timestamp1, categoryNamesList, false));
        // <--

       // Collections.sort(eventList, new StartTimeComparator());

        return eventList;
    }

    private ArrayList<Event> getActiveEventList(List<Event> allEventList) {
        ArrayList<Event> activeEventList = new ArrayList<>();

        Date currentDate = new Date();
        long currentDateMillis = currentDate.getTime();
        Date eventDate;

        for (int i = 0; i < allEventList.size(); i++) {
            eventDate = new Date(allEventList.get(i).getEndTime().getTime());
            long eventEndTime = eventDate.getTime();

            if (eventEndTime < currentDateMillis) {
                activeEventList.add(activeEventList.get(i));
            }
        }
        return activeEventList;
    }

    public Event getActiveEvent(List<Event> allEventList) {
        Event activeEvent = null;
        Date currentDate = new Date();
        long currentDateMillis = currentDate.getTime();
        Date eventStartDate;
        Date eventEndDate;
        long eventStartTime;
        long eventEndTime;

        for (int i = 0; i < allEventList.size(); i++) {
            eventStartDate = new Date(allEventList.get(i).getStartTime().getTime());
            eventEndDate = new Date(allEventList.get(i).getEndTime().getTime());
            eventStartTime = eventStartDate.getTime();
            eventEndTime = eventEndDate.getTime();
            Log.e("III", currentDate + " " + eventStartTime + " " + eventEndTime);

            if (currentDateMillis > eventStartTime && currentDateMillis < eventEndTime) {
               activeEvent = allEventList.get(i);
            }
        }

        return activeEvent;
    }
}
