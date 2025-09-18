import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {

    static final String DB_URL = "jdbc:mysql://localhost:3306/BankDB";
    static final String USER = ""; // your MySQL username
    static final String PASS = ""; // your MySQL password

    static Connection conn;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database.");

            while (true) {
                System.out.println("\n====== Bank Management System ======");
                System.out.println("1. Add User");
                System.out.println("2. Search User by Name");
                System.out.println("3. Update User Details");
                System.out.println("4. Delete User");
                System.out.println("5. Show All User");
                System.out.println("6. Exit");
                System.out.print("\n Choose an option: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1: addUser(); break;
                    case 2: searchUser(); break;
                    case 3: updateUser(); break;
                    case 4: deleteUser(); break;
                    case 5: showAllUsers(); break;
                    case 6:
                        System.out.println("Exiting...");
                        conn.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addUser() {
    try {
        System.out.println("\n\n---~~ ADD USER ~~---\n");
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Balance: ");
        double balance = Double.parseDouble(sc.nextLine());

        String sql = "INSERT INTO Users (name, email, phone, balance) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setString(2, email);
        pstmt.setString(3, phone);
        pstmt.setDouble(4, balance);

        pstmt.executeUpdate();

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            int newId = generatedKeys.getInt(1);
            System.out.println("User added successfully with ID: " + newId);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private static void searchUser() {
    try {
        System.out.println("\n\n ---~~ SEARCH USER ~~---\n");
        System.out.print("Enter Name to Search: ");
        String name = sc.nextLine();

        String sql = "SELECT * FROM Users WHERE LOWER(name) LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + name.toLowerCase() + "%");

        ResultSet rs = pstmt.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("ID: " + rs.getInt("id") +
                               ", Name: " + rs.getString("name") +
                               ", Email: " + rs.getString("email") +
                               ", Phone: " + rs.getString("phone") +
                               ", Balance: " + rs.getDouble("balance"));
        }

        if (!found) {
            System.out.println("No users found with that name.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private static void updateUser() {
    try {
        System.out.println("\n\n ---~~ UPDATE THE USER ~~---\n");
        System.out.print("Enter Name of the User to Update: ");
        String searchName = sc.nextLine();

        System.out.print("Enter New Name: ");
        String newName = sc.nextLine();

        System.out.print("Enter New Email: ");
        String newEmail = sc.nextLine();

        System.out.print("Enter New Phone: ");
        String newPhone = sc.nextLine();

        System.out.print("Enter New Balance: ");
        double newBalance = Double.parseDouble(sc.nextLine());

        String sql = "UPDATE Users SET name=?, email=?, phone=?, balance=? WHERE LOWER(name)=LOWER(?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newName);
        pstmt.setString(2, newEmail);
        pstmt.setString(3, newPhone);
        pstmt.setDouble(4, newBalance);
        pstmt.setString(5, searchName);

        int updated = pstmt.executeUpdate();
        if (updated > 0) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("No user found with that name.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


   private static void deleteUser() {
    try {
        System.out.println("\n\n ---~~ DELETE THE USER ~~---\n");
        System.out.print("Enter Name of the User to Delete: ");
        String name = sc.nextLine();

        String sql = "DELETE FROM Users WHERE LOWER(name) = LOWER(?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);

        int deleted = pstmt.executeUpdate();
        if (deleted > 0) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("No user found with that name.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private static void showAllUsers() {
    try {
        System.out.println("\n\n ---~~ SHOWING ALL THE USERS ~~---\n");
        String sql = "SELECT * FROM Users";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("ID: " + rs.getInt("id") +
                               ", Name: " + rs.getString("name") +
                               ", Email: " + rs.getString("email") +
                               ", Phone: " + rs.getString("phone") +
                               ", Balance: " + rs.getDouble("balance"));
        }

        if (!found) {
            System.out.println("No users found in the database.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}

