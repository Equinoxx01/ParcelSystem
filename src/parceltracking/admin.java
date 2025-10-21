package parceltracking;

import config.config;
import java.util.*;

public class admin {

    public static void adminMenu(config db, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View Pending Users");
            System.out.println("2. Approve User");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewPendingUsers(db);
                    break;
                case 2:
                    approveUser(db, sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    public static void viewPendingUsers(config db) {
        String sql = "SELECT * FROM tbl_user WHERE u_role = 'Pending'";
        String[] headers = {"ID", "Username", "Role"};
        String[] cols = {"u_id", "u_name", "u_role"};
        db.viewRecords(sql, headers, cols);
    }

    public static void approveUser(config db, Scanner sc) {
        System.out.print("Enter User ID to approve: ");
        int id = sc.nextInt();
        sc.nextLine();  

        System.out.print("Enter new role (Admin/Staff/Rider): ");
        String role = sc.nextLine();

        db.updateRecord("UPDATE tbl_user SET u_role = ? WHERE u_id = ?", role, id);
        System.out.println("User approved and role assigned!");
    }
}
