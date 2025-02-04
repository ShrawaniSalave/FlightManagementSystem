package FlightManagementSystem;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.xdevapi.Statement;

public class User {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/FlightManagement?useSSL=false";
    static final String USER = "root";
    static final String PASS = "SS18@salave";
    
    private Scanner scanner = new Scanner(System.in);
    private Connection connection;
    
    // Constructor to initialize the connection
    public User() throws SQLException, ClassNotFoundException {
        this.connection = getConnection();  // Get the connection when User object is created
    }

    // Method to establish a connection to the database
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // User login method
    public int userLogin() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userId = -1;
        
        try {
            // Prepare the SQL query
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = connection.prepareStatement(query);

            // Get user credentials from the user
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Set the parameters for the query
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute the query
            rs = pstmt.executeQuery();

            // Check if credentials are valid
            if (rs.next()) {
                userId = rs.getInt("user_id"); // Retrieve the user ID
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return userId; // Return the userId or -1 if login failed
    }

    // User registration method
    public boolean registerUser() {
        PreparedStatement pstmt = null;
        
        try {
            // Get user details for registration
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            
            // Prepare the SQL query for inserting a new user
            String query = "INSERT INTO users (username, email, password, phone_number, age, address) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = connection.prepareStatement(query);
            
            // Set the parameters for the query
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phoneNumber);
            pstmt.setInt(5, age);
            pstmt.setString(6, address);

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Registration successful!");
                return true;
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    // User menu method
    public void userMenu(int userId) {
        boolean isRunning = true;
        
        while (isRunning) {
            System.out.println("+---------------------------------------------+");
            System.out.println("|                USER MENU                    |");
            System.out.println("+---------------------------------------------+");
            System.out.println("|                                             |");
            System.out.println("|        1. Search Flight                     |");
            System.out.println("|        2. Book a Flight                     |");
            System.out.println("|        3. View Booking History              |");
            System.out.println("|        4. Cancel Reservation                |");
            System.out.println("|        5. View Profile                      |");
            System.out.println("|        6. Logout                            |");
            System.out.println("+---------------------------------------------+");
            System.out.print("Enter your choice: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (userChoice) {
                case 1:
                    searchFlight();  
                    break;
                case 2:
                    bookFlight(userId);  // Book a flight
                    displayBookingDetails(userId);
                    break;
                case 3:
                	viewReservationHistory(userId);
                    break;
                case 4:
                	System.out.print("Enter the Flight ID to cancel reservation: ");
                    int flightId = scanner.nextInt();
                	cancelReservation(userId, flightId);
                    break;
                case 5:
                    viewProfile(userId); 
                    break;
                case 6:
                    System.out.println("Logging out...");
                    isRunning = false;  // Logout
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    public void viewProfile(int userId) {
        // SQL query to fetch user details by userId
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set the userId parameter in the query
            pstmt.setInt(1, userId);
            
            // Execute the query
            ResultSet resultSet = pstmt.executeQuery();
            
            // Check if a user with the provided userId exists
            if (resultSet.next()) {
                // Fetch user details from the result set
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phone_number");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                
                // Display user details in a formatted manner
                System.out.println("+-----------------------------------------------+");
                System.out.println("|               User Profile                    |");
                System.out.println("+-----------------------------------------------+");
                System.out.printf("| %-45s |%n", "User ID        : " + userId);
                System.out.printf("| %-45s |%n", "Username       : " + username);
                System.out.printf("| %-45s |%n", "Email          : " + email);
                System.out.printf("| %-45s |%n", "Phone Number   : " + phoneNumber);
                System.out.printf("| %-45s |%n", "Age            : " + age);
                System.out.printf("| %-45s |%n", "Address        : " + address);
                System.out.println("+-----------------------------------------------+");

            } else {
                System.out.println("User not found.");
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching user profile: " + e.getMessage());
        }
    }
    
 // Cancel reservation for a logged-in user
    public void cancelReservation(int userId, int flightId) {
        // SQL query to check if the reservation exists for the logged-in user and flight ID
        String queryCheckReservation = "SELECT seat_number, seat_type FROM reservations WHERE user_id = ? AND flight_id = ?";
        
        // SQL query to delete the reservation from the reservations table
        String queryDeleteReservation = "DELETE FROM reservations WHERE user_id = ? AND flight_id = ?";
        
        // SQL query to update available seats in the flights table
        String queryUpdateSeats = "UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?";

        try (Connection connection = getConnection()) {
            // Check if the reservation exists for the user and flight
            try (PreparedStatement checkReservationStmt = connection.prepareStatement(queryCheckReservation)) {
                checkReservationStmt.setInt(1, userId);
                checkReservationStmt.setInt(2, flightId);
                
                try (ResultSet resultSet = checkReservationStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        // No reservation found for this user and flight
                        System.out.println("No reservation found for the specified flight.");
                    } else {
                        // Reservation exists, get the seat number and seat type
                        int seatNumber = resultSet.getInt("seat_number");
                        String seatType = resultSet.getString("seat_type");

                        // Step 1: Delete the reservation
                        try (PreparedStatement deleteReservationStmt = connection.prepareStatement(queryDeleteReservation)) {
                            deleteReservationStmt.setInt(1, userId);
                            deleteReservationStmt.setInt(2, flightId);
                            
                            int rowsAffected = deleteReservationStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                // Step 2: Update the available seats for the flight
                                try (PreparedStatement updateSeatsStmt = connection.prepareStatement(queryUpdateSeats)) {
                                    updateSeatsStmt.setInt(1, flightId);
                                    updateSeatsStmt.executeUpdate();
                                    
                                    // Display confirmation
                                    System.out.println("Reservation for Flight ID " + flightId + " (Seat " + seatNumber + ", " + seatType + ") has been successfully canceled.");
                                }
                            } else {
                                System.out.println("Failed to cancel the reservation.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    
 // View Reservation History for a logged-in user
    public void viewReservationHistory(int userId) {
        // SQL query to fetch reservation history for the logged-in user
        String query = "SELECT r.reservation_id, f.flight_id, f.airline, f.origin, f.destination, r.seat_number, r.seat_type, r.status " +
                       "FROM reservations r " +
                       "JOIN flights f ON r.flight_id = f.flight_id " +
                       "WHERE r.user_id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                // Set the user_id parameter in the query
                stmt.setInt(1, userId);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    // Check if there are any reservations
                    if (!resultSet.next()) {
                        System.out.println("No reservations found for this user.");
                    } else {
                        // Display reservation history with all columns
                        System.out.println("+---------------------------------------------------------------+");
                        System.out.println("|                Reservation History                             |");
                        System.out.println("+-----------+-----------+-----------+--------------+------------+-----------+-------------+");
                        System.out.println("| Flight ID | Airline   | Origin    | Destination  | Seat No.   | Seat Type | Status     |");
                        System.out.println("+-----------+-----------+-----------+--------------+------------+-----------+-------------+");

                        do {
                            // Fetch the details from the result set
                            int flightId = resultSet.getInt("flight_id");
                            String airline = resultSet.getString("airline");
                            String origin = resultSet.getString("origin");
                            String destination = resultSet.getString("destination");
                            int seatNumber = resultSet.getInt("seat_number");
                            String seatType = resultSet.getString("seat_type");
                            String status = resultSet.getString("status");

                            // Display the reservation details with proper formatting
                            System.out.printf("| %-9d | %-9s | %-9s | %-12s | %-10d | %-9s | %-11s |\n", flightId, airline, origin, destination, seatNumber, seatType, status);
                            System.out.println("+-----------+-----------+-----------+--------------+------------+-----------+-------------+------------+");
                        } while (resultSet.next());
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    
 // Search for flights based on origin, destination, and departure time
    public void searchFlight() {
        System.out.print("Enter Origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter Departure Time (YYYY-MM-DD HH:MM:SS): ");
        String departureTime = scanner.nextLine();

        // SQL query to search flights
        String query = "SELECT * FROM Flights WHERE origin = ? AND destination = ? AND departure_time >= ? AND available_seats > 0";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the prepared statement
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departureTime);

            // Execute query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Available Flights:");
                System.out.println("+-----------+----------+------------+------------------+---------------------+------------+");
                System.out.println("| Flight ID | Airline  | Origin     | Destination      | Departure Time      | Price      |");
                System.out.println("+-----------+----------+------------+------------------+---------------------+------------+");

                // Process the result set
                boolean foundFlights = false;
                while (resultSet.next()) {
                    foundFlights = true;
                    int flightId = resultSet.getInt("flight_id");
                    String airline = resultSet.getString("airline");
                    String flightOrigin = resultSet.getString("origin");
                    String flightDestination = resultSet.getString("destination");
                    String flightDeparture = resultSet.getString("departure_time");
                    double price = resultSet.getDouble("base_price");

                    System.out.printf("| %-9d | %-8s | %-10s | %-16s | %-16s | %-10.2f |\n",
                            flightId, airline, flightOrigin, flightDestination, flightDeparture, price);
                }

                System.out.println("+-----------+----------+------------+------------------+---------------------+------------+");

                if (!foundFlights) {
                    System.out.println("No flights available for the given criteria.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for flights: " + e.getMessage());
        }
    }
    
    
 // Display available flights
    public void displayAvailableFlights() {
        String query = "SELECT * FROM Flights WHERE available_seats > 0";

        try (Connection connection = getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                System.out.println("+--------------------------------------------+");
                System.out.println("|           Available Flight Details         |");
                System.out.println("+-----------+-----------+-----------+-------------+---------------------+---------------------+------------+-------------+-----------------+----------+");
                System.out.println("| Flight ID | Airline   | Origin    | Destination | Departure Time      | Arrival Time        | Base Price | Total Seats | Available Seats | Admin ID |");
                System.out.println("+-----------+-----------+-----------+-------------+---------------------+---------------------+------------+-------------+-----------------+----------+");

                while (rs.next()) {
                    int flightId = rs.getInt("flight_id");
                    String airline = rs.getString("airline");
                    String origin = rs.getString("origin");
                    String destination = rs.getString("destination");
                    String departureTime = rs.getString("departure_time");
                    String arrivalTime = rs.getString("arrival_time");
                    double basePrice = rs.getDouble("base_price");
                    int totalSeats = rs.getInt("total_seats");
                    int availableSeats = rs.getInt("available_seats");
                    int adminId = rs.getInt("admin_id");

                    System.out.printf("| %-9d | %-9s | %-9s | %-11s | %-19s | %-19s | %-10.2f | %-11d | %-15d | %-8d |\n",
                            flightId, airline, origin, destination, departureTime, arrivalTime, basePrice, totalSeats, availableSeats, adminId);
                }

                System.out.println("+-----------+-----------+-----------+-------------+---------------------+---------------------+------------+-------------+-----------------+----------+");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
 
    public void displayBookingDetails(int userId) {
        String queryViewBookings = "SELECT reservation_id, flight_id, seat_number, seat_type, booking_time, status, flight_name FROM reservations WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement viewBookingsStmt = connection.prepareStatement(queryViewBookings)) {
                viewBookingsStmt.setInt(1, userId);
                try (ResultSet resultSet = viewBookingsStmt.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println("Reservation ID: " + resultSet.getInt("reservation_id"));
                        System.out.println("Flight ID: " + resultSet.getInt("flight_id"));
                        System.out.println("Seat Number: " + resultSet.getInt("seat_number"));
                        System.out.println("Seat Type: " + resultSet.getString("seat_type"));
                        System.out.println("Booking Time: " + resultSet.getTimestamp("booking_time"));
                        System.out.println("Status: " + resultSet.getString("status"));
                        System.out.println("Flight Name: " + resultSet.getString("flight_name"));
                        System.out.println("-----------------------------------");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public String selectSeat() {
        System.out.println("Select seat type:");
        System.out.println("1. Economy");
        System.out.println("2. Business");
        System.out.println("3. First Class");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return "Economy";
            case 2:
                return "Business";
            case 3:
                return "First Class";
            default:
                return "Economy";  // Default seat type
        }
    }


 // Book a flight for a user
    public void bookFlight(int userId) {
        // Step 1: Display available flights to the user
        displayAvailableFlights();

        // Step 2: Ask for the Flight ID to book
        System.out.println("Enter Flight ID to book:");
        int flightId = scanner.nextInt();

        // Step 3: Check the availability of seats
        String queryCheckSeats = "SELECT available_seats, airline, origin, destination FROM Flights WHERE flight_id = ?";
        String queryBookFlight = "INSERT INTO reservations (user_id, flight_id, booking_time, seat_number, seat_type, status, flight_name) VALUES (?, ?, NOW(), ?, ?, 'Confirmed', ?)";
        String queryUpdateSeats = "UPDATE Flights SET available_seats = available_seats - 1 WHERE flight_id = ?";

        String seatType = "";  // Declare the seatType variable here

        try (Connection connection = getConnection()) {
            // Check seat availability for the selected flight
            try (PreparedStatement checkSeatsStmt = connection.prepareStatement(queryCheckSeats)) {
                checkSeatsStmt.setInt(1, flightId);
                try (ResultSet resultSet = checkSeatsStmt.executeQuery()) {
                    if (resultSet.next()) {
                        int availableSeats = resultSet.getInt("available_seats");
                        String flightName = resultSet.getString("airline") + " (" + resultSet.getString("origin") + " to " + resultSet.getString("destination") + ")";

                        if (availableSeats > 0) {
                            // Step 4: Let the user select a seat type
                            System.out.println("Select seat type:");
                            System.out.println("1. Economy");
                            System.out.println("2. Business");
                            System.out.println("3. First Class");
                            int seatTypeChoice = scanner.nextInt();
                            
                            // Set seatType based on user input
                            if (seatTypeChoice == 1) {
                                seatType = "Economy";
                            } else if (seatTypeChoice == 2) {
                                seatType = "Business";
                            } else if (seatTypeChoice == 3) {
                                seatType = "First Class";
                            } else {
                                System.out.println("Invalid seat type. Defaulting to Economy.");
                                seatType = "Economy";
                            }

                            // Step 5: Ask for the seat number
                            System.out.println("Enter the seat number:");
                            int seatNumber = scanner.nextInt();

                            // Step 6: Book the flight and update seat availability
                            try (PreparedStatement bookFlightStmt = connection.prepareStatement(queryBookFlight)) {
                                bookFlightStmt.setInt(1, userId);
                                bookFlightStmt.setInt(2, flightId);
                                bookFlightStmt.setInt(3, seatNumber);
                                bookFlightStmt.setString(4, seatType);  // Now seatType is correctly initialized
                                bookFlightStmt.setString(5, flightName);

                                if (bookFlightStmt.executeUpdate() > 0) {
                                    // Step 7: Update the seat availability in the Flights table
                                    try (PreparedStatement updateSeatsStmt = connection.prepareStatement(queryUpdateSeats)) {
                                        updateSeatsStmt.setInt(1, flightId);
                                        updateSeatsStmt.executeUpdate();
                                        System.out.println("Flight " + flightName + " booked successfully!");
                                    }
                                } else {
                                    System.out.println("Failed to book the flight.");
                                }
                            }
                        } else {
                            System.out.println("No available seats for the selected flight.");
                        }
                    } else {
                        System.out.println("Flight ID not found.");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
