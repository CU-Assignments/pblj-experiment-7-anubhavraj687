import java.sql.*;
import java.util.Scanner;

public class ProductApp {
    static final String DB_URL = "jdbc:mysql://localhost:3306/yourdbname";
    static final String USER = "root";
    static final String PASS = "password";
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            con.setAutoCommit(false); // Start transaction
            
            while (true) {
                System.out.println("\n1. Create\n2. Read\n3. Update\n4. Delete\n5. Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                
                switch (choice) {
                    case 1:
                        System.out.print("Product ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        
                        try (PreparedStatement pst = con.prepareStatement(
                                "INSERT INTO Product VALUES (?, ?, ?, ?)")) {
                            pst.setInt(1, id);
                            pst.setString(2, name);
                            pst.setDouble(3, price);
                            pst.setInt(4, qty);
                            pst.executeUpdate();
                            con.commit();
                            System.out.println("Product added.");
                        } catch (SQLException e) {
                            con.rollback();
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    
                    case 2:
                        try (Statement st = con.createStatement()) {
                            ResultSet rs = st.executeQuery("SELECT * FROM Product");
                            while (rs.next()) {
                                System.out.println(rs.getInt(1) + " | " + rs.getString(2)
                                        + " | " + rs.getDouble(3) + " | " + rs.getInt(4));
                            }
                        }
                        break;
                    
                    case 3:
                        System.out.print("Enter Product ID to update: ");
                        id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        name = sc.nextLine();
                        System.out.print("New Price: ");
                        price = sc.nextDouble();
                        System.out.print("New Quantity: ");
                        qty = sc.nextInt();

                        try (PreparedStatement pst = con.prepareStatement(
                                "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?")) {
                            pst.setString(1, name);
                            pst.setDouble(2, price);
                            pst.setInt(3, qty);
                            pst.setInt(4, id);
                            int rows = pst.executeUpdate();
                            con.commit();
                            System.out.println(rows > 0 ? "Updated successfully." : "No such product.");
                        } catch (SQLException e) {
                            con.rollback();
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Enter Product ID to delete: ");
                        id = sc.nextInt();
                        try (PreparedStatement pst = con.prepareStatement(
                                "DELETE FROM Product WHERE ProductID=?")) {
                            pst.setInt(1, id);
                            int rows = pst.executeUpdate();
                            con.commit();
                            System.out.println(rows > 0 ? "Deleted successfully." : "No such product.");
                        } catch (SQLException e) {
                            con.rollback();
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 5:
                        return;

                    default:
                        System.out.println("Invalid option!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
