package FlightManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
	 static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	    static final String DB_URL = "jdbc:mysql://localhost:3306/FlightManagement?useSSL=false";
	    static final String USER = "root";
	    static final String PASS = "SS18@salave";
	    
	    private Scanner scanner = new Scanner(System.in);
	    private Connection connection;

	    // Constructor to initialize the connection
	    public Admin() throws SQLException, ClassNotFoundException {
	        this.connection = getConnection();  // Get the connection when Admin object is created
	    }

	    // Method to establish a connection to the database
	    private Connection getConnection() throws SQLException, ClassNotFoundException {
	        Class.forName(JDBC_DRIVER);
	        return DriverManager.getConnection(DB_URL, USER, PASS);
	    }

	    // Admin login method now returns adminId
	    public int adminLogin() {
	    	 PreparedStatement pstmt = null;
	    	    ResultSet rs = null;
	    	    int adminId = -1;

	    	    try {
	    	        // Prepare the SQL query
	    	        String query = "SELECT * FROM admins WHERE admin_name = ? AND password = ?";
	    	        pstmt = connection.prepareStatement(query);

	    	        // Get admin credentials from the user
	    	        System.out.print("Enter admin username: ");
	    	        String username = scanner.nextLine();
	    	        System.out.print("Enter admin password: ");
	    	        String password = scanner.nextLine();

	    	        // Set the parameters for the query
	    	        pstmt.setString(1, username);
	    	        pstmt.setString(2, password);

	    	        // Execute the query
	    	        rs = pstmt.executeQuery();

	    	        // Check if credentials are valid
	    	        if (rs.next()) {
	    	            adminId = rs.getInt("admin_id"); // Retrieve the admin ID
	    	            
	    	        } else {
	    	            System.out.println("Invalid admin credentials. Please try again.");
	    	        }
	    	    } catch (Exception e) {
	    	        System.out.println("Error: " + e.getMessage());
	    	    }

	    	    return adminId; // Return the adminId or -1 if login failed
	    }

	    public boolean adminMenu(int adminId) {
	        boolean isRunning = true;
	        
	        while (isRunning) {
	            System.out.println("+---------------------------------------------+");
	            System.out.println("|                ADMIN MENU                   |");
	            System.out.println("+---------------------------------------------+");
	            System.out.println("|                                             |");
	            System.out.println("|        1. Add Flight                        |");
	            System.out.println("|        2. Update Flight                     |");
	            System.out.println("|        3. Delete Flight                     |");
	            System.out.println("|        4. View All Reservations             |");
	            System.out.println("|        5. View Booking Trends               |");
	            System.out.println("|        6. Create Discount Code              |");
	            System.out.println("|        7. Exit to Main Menu                 |");
	            System.out.println("+---------------------------------------------+");
	            System.out.print("Enter your choice: ");
	            int adminChoice = scanner.nextInt();

	            switch (adminChoice) {
	                case 1:
	                    addFlight(adminId); // Pass adminId to addFlight method
	                    break;
	                case 2:
	                    updateFlight(); // Call the Update Flight method
	                    break;
	                case 3:
	                    deleteFlight(); // Call the Delete Flight method
	                    break;
	                case 4:
	                    viewAllReservations(); // Call the View All Reservations method
	                    break;
	                case 5:
	                    viewBookingTrends(); // Call the View Booking Trends method
	                    break;
	                case 6:
	                    createDiscountCode(adminId); // Pass adminId to createDiscountCode method
	                    break;
	                case 7:
	                    isRunning = false; // Exit the admin menu
	                    break;
	                default:
	                    System.out.println("Invalid choice. Please try again.");
	            }
	        }
	        
	        return false; // Return false to signal exit from admin menu
	    }
        
//	    public boolean adminMenu(int adminId) {
//	        boolean isRunning = true;
//	        
//	        while (isRunning) {
//	            System.out.println("+---------------------------------------------+");
//	            System.out.println("|                ADMIN MENU                   |");
//	            System.out.println("+---------------------------------------------+");
//	            System.out.println("|                                             |");
//	            System.out.println("|        1. Add Flight                        |");
//	            System.out.println("|        2. Update Flight                     |");
//	            System.out.println("|        3. Delete Flight                     |");
//	            System.out.println("|        4. View All Reservations             |");
//	            System.out.println("|        5. View Booking Trends               |");
//	            System.out.println("|        6. Create Discount Code              |");
//	            System.out.println("|        7. Exit to Main Menu                 |");
//	            System.out.println("+---------------------------------------------+");
//	            System.out.print("Enter your choice: ");
//	            
//	            int adminChoice = -1;
//	            try {
//	                adminChoice = scanner.nextInt();
//	            } catch (InputMismatchException e) {
//	                scanner.nextLine(); // Clear the invalid input
//	                System.out.println("Invalid input, please enter a valid option.");
//	                continue;
//	            }
//
//	            switch (adminChoice) {
//	                case 1:
//	                    addFlight(adminId);
//	                    break;
//	                case 2:
//	                    updateFlight();
//	                    break;
//	                case 3:
//	                    deleteFlight();
//	                    break;
//	                case 4:
//	                    viewAllReservations();
//	                    break;
//	                case 5:
//	                    viewBookingTrends();
//	                    break;
//	                case 6:
//	                    createDiscountCode(adminId);
//	                    break;
//	                case 7:
//	                    isRunning = false; // Properly exit the menu
//	                    System.out.println("Exiting to main menu...");
//	                    break;
//	                default:
//	                    System.out.println("Invalid choice. Please try again.");
//	            }
//	        }
//	        return false; // This will signal the exit and return to the main menu or previous menu
//	    }

	    // Add a flight
	    public void addFlight(int adminId) {
	        scanner.nextLine();  // To consume the leftover newline character
	        System.out.println("Enter Airline Name:");
	        String airline = scanner.nextLine();

	        System.out.println("Enter Origin:");
	        String origin = scanner.nextLine();

	        System.out.println("Enter Destination:");
	        String destination = scanner.nextLine();

	        System.out.println("Enter Departure Time (YYYY-MM-DD HH:MM:SS):");
	        String departureTime = scanner.nextLine();

	        System.out.println("Enter Arrival Time (YYYY-MM-DD HH:MM:SS):");
	        String arrivalTime = scanner.nextLine();

	        double basePrice = 0;
	        boolean validBasePrice = false;
	        while (!validBasePrice) {
	            System.out.println("Enter Base Price:");
	            try {
	                basePrice = scanner.nextDouble();
	                validBasePrice = true;
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input for base price. Please enter a valid number (e.g., 1000.50).");
	                scanner.nextLine(); // Consume invalid input
	            }
	        }

	        System.out.println("Enter Total Seats:");
	        int totalSeats = scanner.nextInt();
	        System.out.println("Enter Available Seats:");
	        int availableSeats = scanner.nextInt();

	        try {
	            String query = "INSERT INTO Flights (airline, origin, destination, departure_time, arrival_time, base_price, total_seats, available_seats, admin_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, airline);
	            preparedStatement.setString(2, origin);
	            preparedStatement.setString(3, destination);
	            preparedStatement.setString(4, departureTime);
	            preparedStatement.setString(5, arrivalTime);
	            preparedStatement.setDouble(6, basePrice);
	            preparedStatement.setInt(7, totalSeats);
	            preparedStatement.setInt(8, availableSeats);
	            preparedStatement.setInt(9, adminId); // Use the adminId here

	            int affectedRows = preparedStatement.executeUpdate();
	            if (affectedRows > 0) {
	                System.out.println("Flight added successfully!");
	            } else {
	                System.out.println("Failed to add flight.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public void createDiscountCode(int adminId) {
	        scanner.nextLine(); // Consume leftover newline
	        System.out.println("Enter Discount Code:");
	        String discountCode = scanner.nextLine();
	        System.out.println("Enter Discount Percentage:");
	        double discountPercentage = scanner.nextDouble();
	        
	        System.out.println("Enter Discount Start Date (YYYY-MM-DD):");
	        String startDateStr = scanner.next();
	        System.out.println("Enter Discount End Date (YYYY-MM-DD):");
	        String endDateStr = scanner.next();

	        try {
	            // Convert string dates to Date objects
	            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
	            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

	            String query = "INSERT INTO DiscountCodes (discount_code, discount_percentage, admin_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, discountCode);
	            preparedStatement.setDouble(2, discountPercentage);
	            preparedStatement.setInt(3, adminId); // Use the adminId here
	            preparedStatement.setDate(4, startDate);  // Set the start date
	            preparedStatement.setDate(5, endDate);    // Set the end date

	            int affectedRows = preparedStatement.executeUpdate();
	            if (affectedRows > 0) {
	                System.out.println("Discount code created successfully!");
	            } else {
	                System.out.println("Failed to create discount code.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	 // View all reservations
	    public void viewAllReservations() {
	        try {
	            // Query to fetch reservations data
	            String query = "SELECT r.reservation_id, r.user_id, r.flight_id, r.booking_time, " +
	                           "r.seat_number, r.status, r.discount_code " +
	                           "FROM Reservations r";

	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query);

	            // Display reservations in a table format
	            System.out.println("+----------------+---------+-----------+---------------------+-------------+-----------+---------------+");
	            System.out.println("| Reservation ID | User ID | Flight ID | Booking Time        | Seat Number | Status    | Discount Code |");
	            System.out.println("+----------------+---------+-----------+---------------------+-------------+-----------+---------------+");

	            while (rs.next()) {
	                int reservationId = rs.getInt("reservation_id");
	                int userId = rs.getInt("user_id");
	                int flightId = rs.getInt("flight_id");
	                String bookingTime = rs.getString("booking_time");
	                int seatNumber = rs.getInt("seat_number");
	                String status = rs.getString("status");
	                String discountCode = rs.getString("discount_code");

	                // Format the data to align with the table
	                System.out.printf("| %-14d | %-7d | %-9d | %-19s | %-11d | %-9s | %-13s |\n",
	                                  reservationId, userId, flightId, bookingTime, seatNumber, status,
	                                  (discountCode != null ? discountCode : "N/A"));
	            }

	            System.out.println("+----------------+---------+-----------+---------------------+-------------+-----------+---------------+");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	 // View booking trends
	    public void viewBookingTrends() {
	        try {
	            String query = "SELECT flight_id, COUNT(*) AS booking_count FROM Reservations GROUP BY flight_id ORDER BY booking_count DESC";
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query);

	            // Display the booking trends in a table format
	            System.out.println("+-----------+-----------------+");
	            System.out.println("| Flight ID | Booking Count   |");
	            System.out.println("+-----------+-----------------+");

	            while (rs.next()) {
	                int flightId = rs.getInt("flight_id");
	                int bookingCount = rs.getInt("booking_count");

	                // Print the data in a formatted table
	                System.out.printf("| %-9d | %-15d |\n", flightId, bookingCount);
	            }

	            System.out.println("+-----------+-----------------+");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	    public void updateFlight() {
	        System.out.println("Enter the flight ID to update:");
	        int flightId = scanner.nextInt();
	        scanner.nextLine(); // Consume leftover newline

	        boolean continueUpdating = true;

	        while (continueUpdating) {
	            // Display update options
	            System.out.println("\nWhat would you like to update?");
	            System.out.println("1. Airline");
	            System.out.println("2. Origin");
	            System.out.println("3. Destination");
	            System.out.println("4. Departure Time");
	            System.out.println("5. Arrival Time");
	            System.out.println("6. Base Price");
	            System.out.println("7. Exit and return to Admin Menu");

	            System.out.println("Enter your choice:");
	            int choice = scanner.nextInt();
	            scanner.nextLine(); // Consume leftover newline

	            String airline = "";
	            String origin = "";
	            String destination = "";
	            String departureTime = "";
	            String arrivalTime = "";
	            double basePrice = 0;

	            try {
	                switch (choice) {
	                    case 1:
	                        System.out.println("Enter new Airline Name:");
	                        airline = scanner.nextLine();
	                        break;
	                    case 2:
	                        System.out.println("Enter new Origin:");
	                        origin = scanner.nextLine();
	                        break;
	                    case 3:
	                        System.out.println("Enter new Destination:");
	                        destination = scanner.nextLine();
	                        break;
	                    case 4:
	                        System.out.println("Enter new Departure Time:");
	                        departureTime = scanner.nextLine();
	                        break;
	                    case 5:
	                        System.out.println("Enter new Arrival Time:");
	                        arrivalTime = scanner.nextLine();
	                        break;
	                    case 6:
	                        boolean validBasePrice = false;
	                        while (!validBasePrice) {
	                            System.out.println("Enter new Base Price:");
	                            try {
	                                basePrice = scanner.nextDouble();
	                                validBasePrice = true;
	                            } catch (InputMismatchException e) {
	                                System.out.println("Invalid input for base price.");
	                                scanner.nextLine(); // Consume invalid input
	                            }
	                        }
	                        break;
	                    case 7:
	                        continueUpdating = false;
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please select a valid option.");
	                        continue;
	                }

	                // Only update the flight details if a valid input was provided
	                if (choice >= 1 && choice <= 6) {
	                    StringBuilder queryBuilder = new StringBuilder("UPDATE Flights SET ");
	                    if (!airline.isEmpty()) queryBuilder.append("airline = ?, ");
	                    if (!origin.isEmpty()) queryBuilder.append("origin = ?, ");
	                    if (!destination.isEmpty()) queryBuilder.append("destination = ?, ");
	                    if (!departureTime.isEmpty()) queryBuilder.append("departure_time = ?, ");
	                    if (!arrivalTime.isEmpty()) queryBuilder.append("arrival_time = ?, ");
	                    if (basePrice > 0) queryBuilder.append("base_price = ?, ");

	                    queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove trailing comma
	                    queryBuilder.append(" WHERE flight_id = ?");

	                    PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());

	                    int index = 1;
	                    if (!airline.isEmpty()) preparedStatement.setString(index++, airline);
	                    if (!origin.isEmpty()) preparedStatement.setString(index++, origin);
	                    if (!destination.isEmpty()) preparedStatement.setString(index++, destination);
	                    if (!departureTime.isEmpty()) preparedStatement.setString(index++, departureTime);
	                    if (!arrivalTime.isEmpty()) preparedStatement.setString(index++, arrivalTime);
	                    if (basePrice > 0) preparedStatement.setDouble(index++, basePrice);
	                    preparedStatement.setInt(index, flightId);

	                    int affectedRows = preparedStatement.executeUpdate();
	                    if (affectedRows > 0) {
	                        System.out.println("Flight updated successfully!");
	                    } else {
	                        System.out.println("Failed to update flight.");
	                    }
	                }

	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        // Once the admin is done, return to the admin menu
	        adminMenu(flightId);
	    }


	    // Delete a flight
	    public void deleteFlight() {
	        System.out.println("Enter the flight ID to delete:");
	        int flightId = scanner.nextInt();

	        try {
	            String query = "DELETE FROM Flights WHERE flight_id = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setInt(1, flightId);

	            int affectedRows = preparedStatement.executeUpdate();
	            if (affectedRows > 0) {
	                System.out.println("Flight deleted successfully!");
	            } else {
	                System.out.println("Failed to delete flight.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    } 
}
