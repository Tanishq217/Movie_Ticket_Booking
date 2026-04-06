package org.example.computer;


public class Movie {
    private String id;
    private String name;
    private String language;

    public Movie(String id, String name, String language) {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLanguage() { return language; }
}