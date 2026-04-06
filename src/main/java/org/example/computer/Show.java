package org.example.computer;



public class Show {

    private String id;
    private Movie movie;

    private Theater theater;

    private Screen screen;
    private String showTime;


    public Show(String id, Movie movie, Theater theater, Screen screen, String showTime) {
        this.id = id;

        this.movie = movie;

        this.theater = theater;

        this.screen = screen;

        this.showTime = showTime;
    }

    public String getId() { return id; }


    public Movie getMovie() { return movie; }

    public Theater getTheater() { return theater; }

    public Screen getScreen() { return screen; }
    public String getShowTime() { return showTime; }
}