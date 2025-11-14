import java.sql.*;
import java.util.Scanner;

public class CRUDExample {

    static final String url = "jdbc:mysql://localhost:3306/testdb";
    static final String user = "root";
    static final String pass = "root";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            while (true) {
                System.out.println("\n--- CRUD MENU ---");
                System.out.println("1. Insert Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product Price");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int ch = sc.nextInt();

                switch (ch) {
                    case 1: insert(con, sc); break;
                    case 2: read(con); break;
                    case 3: update(con, sc); break;
                    case 4: delete(con, sc); break;
                    case 5:
                        con.close();
                        System.out.println("Exited.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void insert(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter ProductID: ");
        int id = sc.nextInt();

        System.out.print("Enter Product Name: ");
        String name = sc.next();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        PreparedStatement pst = con.prepareStatement(
                "INSERT INTO Product(ProductID, ProductName, Price) VALUES(?,?,?)");

        pst.setInt(1, id);
        pst.setString(2, name);
        pst.setDouble(3, price);

        pst.executeUpdate();
        System.out.println("Product Inserted Successfully.");
    }

    static void read(Connection con) throws Exception {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Product");

        System.out.println("\nProductID  ProductName  Price");
        System.out.println("------------------------------------");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + "   " +
                    rs.getString(2) + "   " +
                    rs.getDouble(3));
        }
    }

    static void update(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter ProductID to Update: ");
        int id = sc.nextInt();

        System.out.print("Enter New Price: ");
        double newPrice = sc.nextDouble();

        PreparedStatement pst = con.prepareStatement(
                "UPDATE Product SET Price=? WHERE ProductID=?");

        pst.setDouble(1, newPrice);
        pst.setInt(2, id);

        int rows = pst.executeUpdate();

        if (rows > 0)
            System.out.println("Product Updated.");
        else
            System.out.println("Product Not Found.");
    }

    static void delete(Connection con, Scanner sc) throws Exception {
        System.out.print("Enter ProductID to Delete: ");
        int id = sc.nextInt();

        PreparedStatement pst = con.prepareStatement(
                "DELETE FROM Product WHERE ProductID=?");

        pst.setInt(1, id);
        int rows = pst.executeUpdate();

        if (rows > 0)
            System.out.println("Product Deleted.");
        else
            System.out.println("Product Not Found.");
    }
}
