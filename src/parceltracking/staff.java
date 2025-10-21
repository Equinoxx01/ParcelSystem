package parceltracking;

import config.config;
import java.util.*;

public class staff {

    public static void staffMenu(config db, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- STAFF MENU ---");
            System.out.println("1. Add Parcel");
            System.out.println("2. Assign Parcel to Rider");
            System.out.println("3. View Parcels");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addParcel(db, sc);
                    break;
                case 2:
                    assignParcel(db, sc);
                    break;
                case 3:
                    viewParcels(db);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    public static void addParcel(config db, Scanner sc) {
        sc.nextLine();
        System.out.print("Parcel name: ");
        String name = sc.nextLine();
        System.out.print("Sender: ");
        String sender = sc.nextLine();
        System.out.print("Recipient: ");
        String recipient = sc.nextLine();
        db.addRecord("INSERT INTO tbl_parcel(p_name, sender, recipient, status) VALUES (?, ?, ?, ?)",
                name, sender, recipient, "Pending");
        System.out.println("Parcel added successfully!");
    }

    public static void assignParcel(config db, Scanner sc) {
        System.out.print("Enter Parcel ID to assign: ");
        int parcelId = sc.nextInt();
        System.out.print("Enter Rider ID: ");
        int riderId = sc.nextInt();
        db.updateRecord("UPDATE tbl_parcel SET assigned_to = ?, status = ? WHERE p_id = ?",
                riderId, "Assigned", parcelId);
        System.out.println("Parcel assigned to rider!");
    }

    public static void viewParcels(config db) {
        String sql = "SELECT * FROM tbl_parcel";
        String[] headers = {"Parcel ID", "Name", "Sender", "Recipient", "Status", "Assigned To"};
        String[] cols = {"p_id", "p_name", "sender", "recipient", "status", "assigned_to"};
        db.viewRecords(sql, headers, cols);
    }
}
