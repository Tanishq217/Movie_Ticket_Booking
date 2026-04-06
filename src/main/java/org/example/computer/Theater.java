package org.example.computer;

import java.util.*;

public class Theater {


    private String id;
    private String name;

    private String city;

    private String address;
    private Map<Integer, Screen> screens;




    public Theater(String id, String name, String city, String address) {
        this.id = id;


        this.name = name;
        this.city = city;

        this.address = address;
        this.screens = new HashMap<>();
    }


    public void addScreen(Screen screen) {


        screens.put(screen.getScreenNumber(), screen);
    }

    public Screen getScreen(int screenNumber) {
        return screens.get(screenNumber);
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getCity() { return city; }
    public String getAddress() { return address; }
}