package parceltracking;

import config.config;
import java.util.*;

public class rider {

    
    public static void riderMenu(config db, Scanner sc, int userId) {
        int choice;
        do {
            System.out.println("\n--- RIDER MENU ---");
            System.out.println("1. View My Parcels");
            System.out.println("2. Add Tracking Update");
            System.out.println("3. View Tracking Records");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewMyParcels(db, userId);
                    break;
                case 2:
                    addTracking(db, sc);
                    break;
                case 3:
                    viewTracking(db);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    
    public static void viewMyParcels(config db, int userId) {
    String sql = "SELECT * FROM tbl_parcel WHERE assigned = " + userId;
    String[] headers = {"Parcel ID", "Name", "Sender", "Recipient", "Status", "Assigned"};
    String[] cols = {"p_id", "p_name", "sender", "recipient", "status", "assigned"};
    db.viewRecords(sql, headers, cols);
}
    public static void addTracking(config db, Scanner sc) {
    sc.nextLine();
    System.out.print("Enter Parcel ID: ");
    int pid = sc.nextInt();
    sc.nextLine();
    System.out.print("Location: ");
    String loc = sc.nextLine();
    System.out.print("Status: ");
    String status = sc.nextLine();
    System.out.print("Timestamp (YYYY-MM-DD HH:MM): ");
    String time = sc.nextLine();

    db.addRecord("INSERT INTO tbl_tracking(p_id, location, status, time) VALUES (?, ?, ?, ?)",
            pid, loc, status, time);

    db.updateRecord("UPDATE tbl_parcel SET status = ? WHERE p_id = ?", status, pid);
    System.out.println("Tracking added successfully!");
}



    public static void viewTracking(config db) {
    String sql = "SELECT * FROM tbl_tracking";
    String[] headers = {"Track ID", "Parcel ID", "Location", "Status", "Timestamp"};
    String[] cols = {"t_id", "p_id", "location", "status", "time"};
    db.viewRecords(sql, headers, cols);
}

}
