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
            System.out.println("3. View All Users");
            System.out.println("4. Update User Role/Status");
            System.out.println("5. Delete User");
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
                case 3:
                    viewAllUsers(db);
                    break;
                case 4:
                    updateUser(db, sc);
                    break;
                case 5:
                    deleteUser(db, sc);
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
        String sql = "SELECT * FROM tbl_user WHERE u_status = 'Pending'";
        String[] headers = {"ID", "Username", "Role", "Status"};
        String[] cols = {"u_id", "u_name", "u_role", "u_status"};
        db.viewRecords(sql, headers, cols);
    }

    public static void viewAllUsers(config db) {
        String sql = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Username", "Role", "Status"};
        String[] cols = {"u_id", "u_name", "u_role", "u_status"};
        db.viewRecords(sql, headers, cols);
    }

    public static void approveUser(config db, Scanner sc) {
        System.out.print("Enter User ID to approve: ");
        int id = sc.nextInt();

        db.updateRecord("UPDATE tbl_user SET u_status = 'Active' WHERE u_id = ?", id);
        System.out.println("✅ User approved successfully (Status set to 'Active')!");
    }

    public static void updateUser(config db, Scanner sc) {
        System.out.print("Enter User ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("New Role (Admin/Staff/Rider): ");
        String role = sc.nextLine();
        System.out.print("New Status (Active/Inactive): ");
        String status = sc.nextLine();

        db.updateRecord("UPDATE tbl_user SET u_role = ?, u_status = ? WHERE u_id = ?",
                role, status, id);
        System.out.println("✅ User role/status updated successfully!");
    }

    public static void deleteUser(config db, Scanner sc) {
        System.out.print("Enter User ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Are you sure you want to delete this user? (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            db.updateRecord("DELETE FROM tbl_user WHERE u_id = ?", id);
            System.out.println("🗑️ User deleted successfully!");
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }
}
