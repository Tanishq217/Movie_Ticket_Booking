package org.example.computer;


import java.util.*;

public class Booking {

    private String id;
    private String userEmail;

    private String showId;
    private String movieName;

    private String theaterName;

    private String showTime;
    private List<String> seatNumbers;

    private double totalAmount;
    private String status;

    public Booking(
            String id, String userEmail, String showId, String movieName,

            String theaterName, String showTime, List<String> seatNumbers,
                   double totalAmount, String status) {
        this.id = id;


        this.userEmail = userEmail;


        this.showId = showId;
        this.movieName = movieName;

        this.theaterName = theaterName;

        this.showTime = showTime;


        this.seatNumbers = seatNumbers;


        this.totalAmount = totalAmount;
        this.status = status;
    }


    public String getId() { return id; }

    public String getUserEmail() { return userEmail; }

    public String getShowId() { return showId; }

    public String getMovieName() { return movieName; }


    public String getTheaterName() { return theaterName; }

    public String getShowTime() { return showTime; }

    public List<String> getSeatNumbers() { return seatNumbers; }

    public double getTotalAmount() { return totalAmount; }


    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
