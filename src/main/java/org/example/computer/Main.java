package org.example.computer;


import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MovieTicketSystem system = new MovieTicketSystem();
    private static String currentUserEmail = null;

    public static void main(String[] args) {
        initializeData();

        while (true) {
            if (currentUserEmail == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Movie Ticket Booking System ===");
        System.out.println("1. Login with Email");
        System.out.println("2. Exit");
        System.out.print("Choose option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.print("Enter your email: ");
            currentUserEmail = scanner.nextLine();
            System.out.println("Login successful! Welcome " + currentUserEmail);
        } else {
            System.exit(0);
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Select City");
        System.out.println("2. Browse Movies");
        System.out.println("3. Browse Theaters");
        System.out.println("4. My Bookings");
        System.out.println("5. Cancel Booking");
        System.out.println("6. Admin Menu");
        System.out.println("7. Logout");
        System.out.print("Choose option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                selectCity();
                break;
            case 2:
                browseMovies();
                break;
            case 3:
                browseTheaters();
                break;
            case 4:
                viewMyBookings();
                break;
            case 5:
                cancelBooking();
                break;
            case 6:
                adminMenu();
                break;
            case 7:
                currentUserEmail = null;
                System.out.println("Logged out successfully!");
                break;
        }
    }

    private static void selectCity() {
        System.out.println("\nAvailable Cities:");
        List<String> cities = system.getAvailableCities();
        for (int i = 0; i < cities.size(); i++) {
            System.out.println((i+1) + ". " + cities.get(i));
        }
        System.out.print("Select city: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice >= 1 && choice <= cities.size()) {
            system.setCurrentCity(cities.get(choice-1));
            System.out.println("City set to: " + system.getCurrentCity());
        }
    }

    private static void browseMovies() {
        if (system.getCurrentCity() == null) {
            System.out.println("Please select a city first!");
            return;
        }

        List<Movie> movies = system.getMoviesInCity(system.getCurrentCity());
        if (movies.isEmpty()) {
            System.out.println("No movies playing in this city.");
            return;
        }

        System.out.println("\nMovies playing in " + system.getCurrentCity() + ":");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i+1) + ". " + movies.get(i).getName() + " (" + movies.get(i).getLanguage() + ")");
        }

        System.out.print("Select movie (0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= movies.size()) {
            Movie selectedMovie = movies.get(choice-1);
            showShowsForMovie(selectedMovie);
        }
    }

    private static void showShowsForMovie(Movie movie) {
        List<Show> shows = system.getShowsForMovie(movie.getId(), system.getCurrentCity());
        if (shows.isEmpty()) {
            System.out.println("No shows available for this movie.");
            return;
        }

        System.out.println("\nShows for " + movie.getName() + ":");
        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            System.out.println((i+1) + ". Theater: " + show.getTheater().getName() +
                    ", Screen: " + show.getScreen().getScreenNumber() +
                    ", Time: " + show.getShowTime());
        }

        System.out.print("Select show to book tickets (0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= shows.size()) {
            bookTickets(shows.get(choice-1));
        }
    }

    private static void browseTheaters() {
        if (system.getCurrentCity() == null) {
            System.out.println("Please select a city first!");
            return;
        }

        List<Theater> theaters = system.getTheatersInCity(system.getCurrentCity());
        if (theaters.isEmpty()) {
            System.out.println("No theaters in this city.");
            return;
        }

        System.out.println("\nTheaters in " + system.getCurrentCity() + ":");
        for (int i = 0; i < theaters.size(); i++) {
            Theater theater = theaters.get(i);
            System.out.println((i+1) + ". " + theater.getName() + " - " + theater.getAddress());
        }

        System.out.print("Select theater to view shows (0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= theaters.size()) {
            showTheaterShows(theaters.get(choice-1));
        }
    }

    private static void showTheaterShows(Theater theater) {
        List<Show> shows = system.getShowsInTheater(theater.getId());
        if (shows.isEmpty()) {
            System.out.println("No shows in this theater.");
            return;
        }

        System.out.println("\nShows at " + theater.getName() + ":");
        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            System.out.println((i+1) + ". Movie: " + show.getMovie().getName() +
                    ", Screen: " + show.getScreen().getScreenNumber() +
                    ", Time: " + show.getShowTime());
        }

        System.out.print("Select show to book tickets (0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= shows.size()) {
            bookTickets(shows.get(choice-1));
        }
    }

    private static void bookTickets(Show show) {
        System.out.println("\n=== Booking Tickets ===");

        system.displaySeatMap(show.getId());

        System.out.print("Enter seat numbers to book (comma separated, e.g., A1,B1,C1): ");
        String seatsInput = scanner.nextLine();
        String[] seatNumbers = seatsInput.split(",");

        List<String> selectedSeats = new ArrayList<>();
        for (String seatNum : seatNumbers) {
            selectedSeats.add(seatNum.trim().toUpperCase());
        }

        double totalAmount = system.calculateTotalAmount(show.getId(), selectedSeats);
        System.out.println("Total Amount: Rs. " + totalAmount);

        System.out.println("\nPayment Options:");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. Net Banking");
        System.out.print("Select payment method: ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        String paymentMethod = "";
        switch (paymentChoice) {
            case 1: paymentMethod = "UPI"; break;
            case 2: paymentMethod = "CARD"; break;
            case 3: paymentMethod = "NET_BANKING"; break;
        }

        if (system.processPayment(currentUserEmail, totalAmount, paymentMethod)) {
            Booking booking = system.bookTickets(currentUserEmail, show.getId(), selectedSeats, totalAmount);
            if (booking != null) {
                System.out.println("\n=== Booking Confirmed ===");
                System.out.println("Booking ID: " + booking.getId());
                System.out.println("Movie: " + show.getMovie().getName());
                System.out.println("Theater: " + show.getTheater().getName());
                System.out.println("Show Time: " + show.getShowTime());
                System.out.println("Seats: " + String.join(", ", booking.getSeatNumbers()));
                System.out.println("Total Paid: Rs. " + booking.getTotalAmount());
            }
        } else {
            System.out.println("Payment failed! Please try again.");
        }
    }

    private static void viewMyBookings() {
        List<Booking> bookings = system.getUserBookings(currentUserEmail);
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n=== My Bookings ===");
        for (Booking booking : bookings) {
            System.out.println("ID: " + booking.getId() +
                    ", Movie: " + booking.getMovieName() +
                    ", Seats: " + String.join(", ", booking.getSeatNumbers()) +
                    ", Amount: Rs. " + booking.getTotalAmount() +
                    ", Status: " + booking.getStatus());
        }
    }

    private static void cancelBooking() {
        viewMyBookings();
        System.out.print("Enter Booking ID to cancel: ");
        String bookingId = scanner.nextLine();

        if (system.cancelBooking(bookingId)) {
            System.out.println("Booking cancelled successfully! Refund processed.");
        } else {
            System.out.println("Cancellation failed! Booking not found.");
        }
    }

    private static void adminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Add Movie");
        System.out.println("2. Add Theater");
        System.out.println("3. Add Show");
        System.out.print("Choose option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addMovie();
                break;
            case 2:
                addTheater();
                break;
            case 3:
                addShow();
                break;
        }
    }

    private static void addMovie() {
        System.out.print("Movie Name: ");
        String name = scanner.nextLine();
        System.out.print("Language: ");
        String language = scanner.nextLine();

        system.addMovie(name, language);
        System.out.println("Movie added successfully!");
    }

    private static void addTheater() {
        System.out.print("Theater Name: ");
        String name = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Number of Screens: ");
        int numScreens = scanner.nextInt();
        scanner.nextLine();

        system.addTheater(name, city, address, numScreens);
        System.out.println("Theater added successfully!");
    }

    private static void addShow() {
        System.out.print("Theater ID: ");
        String theaterId = scanner.nextLine();
        System.out.print("Screen Number: ");
        int screenNum = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Movie ID: ");
        String movieId = scanner.nextLine();
        System.out.print("Show Time (e.g., 10:00 AM): ");
        String showTime = scanner.nextLine();

        system.addShow(theaterId, screenNum, movieId, showTime);
        System.out.println("Show added successfully!");
    }

    private static void initializeData() {
        // Add movies
        system.addMovie("Inception", "English");
        system.addMovie("Dangal", "Hindi");
        system.addMovie("Parasite", "Korean");

        // Add theaters
        system.addTheater("PVR Cinemas", "Mumbai", "Andheri West", 3);
        system.addTheater("INOX", "Mumbai", "Bandra", 2);
        system.addTheater("Cinepolis", "Delhi", "Select Citywalk", 4);

        // Add shows
        system.addShow("T1", 1, "M1", "10:00 AM");
        system.addShow("T1", 2, "M2", "1:00 PM");
        system.addShow("T2", 1, "M1", "7:00 PM");
        system.addShow("T3", 1, "M3", "9:00 PM");
    }
}