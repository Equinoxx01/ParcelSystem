package parceltracking;

import config.config;
import java.util.*;

public class ParcelTracking {

    // ===================== VIEW METHODS ===========================
    public static void viewUsers() {
        String sql = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Username", "Password", "Role"};
        String[] cols = {"u_id", "u_name", "u_pass", "u_role"};
        new config().viewRecords(sql, headers, cols);
    }

    public static void viewParcels() {
        String sql = "SELECT * FROM tbl_parcel";
        String[] headers = {"Parcel ID", "Name", "Sender", "Recipient", "Status", "Assigned To"};
        String[] cols = {"p_id", "p_name", "sender", "recipient", "status", "assigned_to"};
        new config().viewRecords(sql, headers, cols);
    }

    public static void viewTracking() {
        String sql = "SELECT * FROM tbl_tracking";
        String[] headers = {"Track ID", "Parcel ID", "Location", "Status", "Timestamp"};
        String[] cols = {"t_id", "p_id", "location", "status", "timestamp"};
        new config().viewRecords(sql, headers, cols);
    }

    // ===================== MAIN METHOD ===========================
    public static void main(String[] args) {
        config con = new config();
        con.connectDB();
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
                    loginMenu(con, sc);
                    break;
                case 2:
                    registerUser(con, sc);
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

    // ===================== LOGIN ===========================
    public static void loginMenu(config con, Scanner sc) {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        List<Map<String, Object>> result =
        con.fetchRecords("SELECT * FROM tbl_user WHERE u_name = ? AND u_pass = ?", uname, pass);


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
                adminMenu(con, sc);
                break;
            case "staff":
                staffMenu(con, sc);
                break;
            case "rider":
                riderMenu(con, sc, userId);
                break;
            default:
                System.out.println("Unknown role.");
                break;
        }
    }

    // ===================== REGISTER ===========================
    public static void registerUser(config con, Scanner sc) {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        System.out.print("Enter role (Admin/Staff/Rider): ");
        String role = sc.nextLine();

        con.addRecord("INSERT INTO tbl_user(u_name, u_pass, u_role) VALUES (?, ?, ?)", uname, pass, role);
        System.out.println("User registered successfully!");
    }

    // ===================== ADMIN MENU ===========================
    public static void adminMenu(config con, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Parcels");
            System.out.println("3. Manage Tracking");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manageUsers(con, sc);
                    break;
                case 2:
                    manageParcels(con, sc);
                    break;
                case 3:
                    manageTracking(con, sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    // ===================== STAFF MENU ===========================
    public static void staffMenu(config con, Scanner sc) {
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
                    addParcel(con, sc);
                    break;
                case 2:
                    assignParcel(con, sc);
                    break;
                case 3:
                    viewParcels();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    // ===================== RIDER MENU ===========================
    public static void riderMenu(config con, Scanner sc, int userId) {
        int choice;
        do {
            System.out.println("\n--- RIDER MENU ---");
            System.out.println("1. View My Parcels");
            System.out.println("2. Add Tracking Update");
            System.out.println("3. View Tracking");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    String sql = "SELECT * FROM tbl_parcel WHERE assigned_to = ?";
                    String[] headers = {"Parcel ID", "Name", "Sender", "Recipient", "Status"};
                    String[] cols = {"p_id", "p_name", "con.viewRecords(sql, headers, cols, userId);sender", "recipient", "status"};
                    con.viewRecords(sql, headers, cols);

                    break;
                case 2:
                    addTracking(con, sc);
                    break;
                case 3:
                    viewTracking();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    // ===================== USER MANAGEMENT ===========================
    public static void manageUsers(config con, Scanner sc) {
        viewUsers();
        System.out.print("Enter User ID to delete (or 0 to cancel): ");
        int id = sc.nextInt();
        if (id != 0) {
            con.deleteRecord("DELETE FROM tbl_user WHERE u_id = ?", id);
            System.out.println("User deleted!");
        }
    }

    // ===================== PARCEL MANAGEMENT ===========================
    public static void manageParcels(config con, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- MANAGE PARCELS ---");
            System.out.println("1. Add Parcel");
            System.out.println("2. View Parcels");
            System.out.println("3. Update Parcel");
            System.out.println("4. Delete Parcel");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addParcel(con, sc);
                    break;
                case 2:
                    viewParcels();
                    break;
                case 3:
                    updateParcel(con, sc);
                    break;
                case 4:
                    deleteParcel(con, sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    // ===================== TRACKING MANAGEMENT ===========================
    public static void manageTracking(config con, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- MANAGE TRACKING ---");
            System.out.println("1. Add Tracking Update");
            System.out.println("2. View Tracking Records");
            System.out.println("3. Delete Tracking Record");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addTracking(con, sc);
                    break;
                case 2:
                    viewTracking();
                    break;
                case 3:
                    deleteTracking(con, sc);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }

    // ===================== HELPER METHODS (if needed) ===========================
    public static void addParcel(config con, Scanner sc) {
        sc.nextLine();
        System.out.print("Parcel name: ");
        String name = sc.nextLine();
        System.out.print("Sender: ");
        String sender = sc.nextLine();
        System.out.print("Recipient: ");
        String recipient = sc.nextLine();
        con.addRecord("INSERT INTO tbl_parcel(p_name, sender, recipient, status) VALUES (?, ?, ?, ?)",
                name, sender, recipient, "Pending");
        System.out.println("Parcel added successfully!");
    }

    public static void assignParcel(config con, Scanner sc) {
        viewParcels();
        System.out.print("Enter Parcel ID: ");
        int pid = sc.nextInt();
        System.out.print("Enter Rider ID: ");
        int rid = sc.nextInt();
        con.updateRecord("UPDATE tbl_parcel SET assigned_to = ?, status = ? WHERE p_id = ?", rid, "Assigned", pid);
        System.out.println("Parcel assigned!");
    }

    public static void updateParcel(config con, Scanner sc) {
        System.out.print("Enter Parcel ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Recipient: ");
        String rec = sc.nextLine();
        System.out.print("New Status: ");
        String stat = sc.nextLine();
        con.updateRecord("UPDATE tbl_parcel SET recipient = ?, status = ? WHERE p_id = ?", rec, stat, id);
        System.out.println("Parcel updated!");
    }

    public static void deleteParcel(config con, Scanner sc) {
        System.out.print("Enter Parcel ID to delete: ");
        int id = sc.nextInt();
        con.deleteRecord("DELETE FROM tbl_parcel WHERE p_id = ?", id);
        System.out.println("Parcel deleted!");
    }

    public static void addTracking(config con, Scanner sc) {
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

        con.addRecord("INSERT INTO tbl_tracking(p_id, location, status, timestamp) VALUES (?, ?, ?, ?)",
                pid, loc, status, time);
        con.updateRecord("UPDATE tbl_parcel SET status = ? WHERE p_id = ?", status, pid);
        System.out.println("Tracking added successfully!");
    }

    public static void deleteTracking(config con, Scanner sc) {
        System.out.print("Enter Track ID to delete: ");
        int tid = sc.nextInt();
        con.deleteRecord("DELETE FROM tbl_tracking WHERE t_id = ?", tid);
        System.out.println("Tracking record deleted!");
    }
}
