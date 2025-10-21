package parceltracking;

import config.config;
import java.util.*;

public class ParcelTracking {

    
    public static void main(String[] args) {
        config db = new config();
        db.connectDB();
        Scanner sc = new Scanner(System.in);
        char cont;

        do {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    loginMenu(db, sc);
                    break;
                case 2:
                    registerUser(db, sc);
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }

            System.out.print("\nDo you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);
            sc.nextLine();
        } while (cont == 'Y' || cont == 'y');

        System.out.println("Program Ended. Goodbye!");
    }

   
    public static void loginMenu(config db, Scanner sc) {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        
        List<Map<String, Object>> result = db.fetchRecords("SELECT * FROM tbl_user WHERE u_name = ? AND u_pass = ?", uname, pass);

        if (result.isEmpty()) {
            System.out.println("Invalid credentials!");
            return;
        }

      
        Map<String, Object> user = result.get(0);
        String role = user.get("u_role").toString();
        int userId = Integer.parseInt(user.get("u_id").toString());

        System.out.println("Welcome, " + uname + " (" + role + ")!");

      
        switch (role.toLowerCase()) {
            case "admin":
                admin.adminMenu(db, sc); 
                break;
                
            case "staff":
                staff.staffMenu(db, sc);  
                break;
                
            case "rider":
                rider.riderMenu(db, sc, userId); 
                break;
                
            default:
                System.out.println("Unknown role.");
                break;
        }
    }

    
    public static void registerUser(config db, Scanner sc) {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        System.out.print("Enter role (Admin/Staff/Rider): ");
        String role = sc.nextLine();

        
        db.addRecord("INSERT INTO tbl_user(u_name, u_pass, u_role) VALUES (?, ?, ?)", uname, pass, "Pending");
        System.out.println("User registered successfully with 'Pending' status!");
    }
}
