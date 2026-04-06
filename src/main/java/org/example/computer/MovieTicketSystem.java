package org.example.computer;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MovieTicketSystem {
    private Map<String, Movie> movies;
    private Map<String, Theater> theaters;
    private Map<String, Show> shows;
    private Map<String, Booking> bookings;
    private Map<String, List<Booking>> userBookings;
    private Map<String, Set<String>> bookedSeats;
    private String currentCity;
    private PricingStrategy pricingStrategy;

    public MovieTicketSystem() {
        movies = new HashMap<>();
        theaters = new HashMap<>();
        shows = new HashMap<>();
        bookings = new HashMap<>();
        userBookings = new HashMap<>();
        bookedSeats = new ConcurrentHashMap<>();
        pricingStrategy = new DynamicPricingStrategy();
    }

    public List<String> getAvailableCities() {
        Set<String> cities = new HashSet<>();
        for (Theater theater : theaters.values()) {
            cities.add(theater.getCity());
        }
        return new ArrayList<>(cities);
    }

    public void setCurrentCity(String city) {
        this.currentCity = city;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public List<Movie> getMoviesInCity(String city) {
        Set<Movie> moviesInCity = new HashSet<>();
        for (Show show : shows.values()) {
            if (show.getTheater().getCity().equals(city)) {
                moviesInCity.add(show.getMovie());
            }
        }
        return new ArrayList<>(moviesInCity);
    }

    public List<Theater> getTheatersInCity(String city) {
        List<Theater> theatersInCity = new ArrayList<>();
        for (Theater theater : theaters.values()) {
            if (theater.getCity().equals(city)) {
                theatersInCity.add(theater);
            }
        }
        return theatersInCity;
    }

    public List<Show> getShowsForMovie(String movieId, String city) {
        List<Show> showsForMovie = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getMovie().getId().equals(movieId) &&
                    show.getTheater().getCity().equals(city)) {
                showsForMovie.add(show);
            }
        }
        return showsForMovie;
    }

    public List<Show> getShowsInTheater(String theaterId) {
        List<Show> showsInTheater = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getTheater().getId().equals(theaterId)) {
                showsInTheater.add(show);
            }
        }
        return showsInTheater;
    }

    public void displaySeatMap(String showId) {
        Show show = shows.get(showId);
        if (show == null) return;

        Set<String> booked = bookedSeats.getOrDefault(showId, new HashSet<>());
        Screen screen = show.getScreen();

        System.out.println("\n=== Seat Map ===");
        for (char row = 'A'; row < 'A' + screen.getRows(); row++) {
            System.out.print(row + " ");
            for (int col = 1; col <= screen.getCols(); col++) {
                String seatNum = row + String.valueOf(col);
                if (booked.contains(seatNum)) {
                    System.out.print("[X] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
        System.out.println("Legend: [ ] = Available, [X] = Booked");
    }

    public double calculateTotalAmount(String showId, List<String> seatNumbers) {
        Show show = shows.get(showId);
        if (show == null) return 0;

        double total = 0;
        for (String seatNum : seatNumbers) {
            String category = getSeatCategory(seatNum);
            double basePrice = show.getScreen().getBasePrice(category);
            double finalPrice = pricingStrategy.calculatePrice(basePrice, show);
            total += finalPrice;
        }
        return total;
    }

    private String getSeatCategory(String seatNum) {
        char row = seatNum.charAt(0);
        if (row <= 'C') return "gold";
        if (row <= 'F') return "silver";
        return "platinum";
    }

    public boolean processPayment(String userEmail, double amount, String paymentMethod) {

        System.out.println("Processing " + paymentMethod + " payment of Rs. " + amount + "...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public synchronized Booking bookTickets(String userEmail, String showId,
                                            List<String> seatNumbers, double totalAmount) {
        Show show = shows.get(showId);
        if (show == null) return null;

        Set<String> bookedForShow = bookedSeats.getOrDefault(showId, new HashSet<>());

        for (String seat : seatNumbers) {
            if (bookedForShow.contains(seat)) {
                System.out.println("Seat " + seat + " is already booked!");
                return null;
            }
        }

        for (String seat : seatNumbers) {
            bookedForShow.add(seat);
        }
        bookedSeats.put(showId, bookedForShow);

        String bookingId = "BKG" + System.currentTimeMillis();
        Booking booking = new Booking(bookingId, userEmail, showId, show.getMovie().getName(),
                show.getTheater().getName(), show.getShowTime(),
                seatNumbers, totalAmount, "CONFIRMED");
        bookings.put(bookingId, booking);

        userBookings.computeIfAbsent(userEmail, k -> new ArrayList<>()).add(booking);

        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null || booking.getStatus().equals("CANCELLED")) {
            return false;
        }

        Set<String> bookedForShow = bookedSeats.get(booking.getShowId());
        if (bookedForShow != null) {
            bookedForShow.removeAll(booking.getSeatNumbers());
        }

        booking.setStatus("CANCELLED");
        return true;
    }

    public List<Booking> getUserBookings(String userEmail) {
        return userBookings.getOrDefault(userEmail, new ArrayList<>());
    }

    public void addMovie(String name, String language) {
        String movieId = "M" + (movies.size() + 1);
        Movie movie = new Movie(movieId, name, language);
        movies.put(movieId, movie);
    }

    public void addTheater(String name, String city, String address, int numScreens) {
        String theaterId = "T" + (theaters.size() + 1);
        Theater theater = new Theater(theaterId, name, city, address);

        for (int i = 1; i <= numScreens; i++) {
            Screen screen = new Screen(i, 8, 10);
            screen.setBasePrice("gold", 300);
            screen.setBasePrice("silver", 200);
            screen.setBasePrice("platinum", 500);
            theater.addScreen(screen);
        }

        theaters.put(theaterId, theater);
    }

    public void addShow(String theaterId, int screenNum, String movieId, String showTime) {
        Theater theater = theaters.get(theaterId);
        Movie movie = movies.get(movieId);

        if (theater != null && movie != null) {
            Screen screen = theater.getScreen(screenNum);
            String showId = "S" + (shows.size() + 1);
            Show show = new Show(showId, movie, theater, screen, showTime);
            shows.put(showId, show);
            bookedSeats.putIfAbsent(showId, new HashSet<>());
        }
    }
}