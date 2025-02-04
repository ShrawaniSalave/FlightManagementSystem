package FlightManagementSystem;

import java.sql.SQLException;
import java.util.Scanner;

public class FlightManagementSystem {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner scanner = new Scanner(System.in);
        Admin admin = new Admin();
        User user=new User();
        
        while (true) {
           System.out.println("+---------------------------------------------+");
           System.out.println("|                 WELCOME                     |");
           System.out.println("|          FLIGHT MANAGEMENT SYSTEM           |");
           System.out.println("|                                             |");
           System.out.println("|            1. Login as Admin                |");
           System.out.println("|            2. Login as User                 |");
           System.out.println("|            3. New User Registration         |");
           System.out.println("|            4. Exit                          |");
           System.out.println("+---------------------------------------------+");
            System.out.print("Enter your choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Fix scanner issue
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue; // Skip to next iteration
            }

            switch (choice) {
                case 1: // Admin login
                    System.out.println("Admin login selected.");
                    int adminId = admin.adminLogin();
                    if (adminId != -1) {
                        System.out.println("Login successful! Welcome, Admin.");
                        admin.adminMenu(adminId);
                    } else {
                        System.out.println("Invalid admin credentials. Please try again.");
                    }
                    break;

                // Uncomment these cases once the User class is implemented
                case 2: // User login
                    int userId = user.userLogin();
                    if (userId != -1) {
                       System.out.println("Login successful! Welcome, User.");
                       user.userMenu(userId);
                   } else {
                       System.out.println("Invalid user credentials. Please try again.");
                   }
                    break;
                    
                case 3: // User registration
                    user.registerUser();
                  break;

                case 4: // Exit
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}





