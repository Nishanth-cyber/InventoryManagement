import java.sql.*;
import java.util.*;

class Item {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String personName;

    public Item(int id, String name, int quantity, double price, String personName) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.personName = personName;
    }

    public Item(String name, int quantity, double price, String personName) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.personName = personName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}

public class Inventory {
    private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:sqlite:C:/Users/nisha/23ALR060/InventoryManagement/sqlite/inventory.db");
    }


    public static void addItem(Item item) throws SQLException {
        String query = "INSERT INTO items (name, quantity, price, personName) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getPersonName());
            stmt.executeUpdate();
        }
    }

    public static List<Item> getAllItems() throws SQLException {
        String query = "SELECT * FROM items";
        List<Item> items = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("personName")
                ));
            }
        }
        return items;
    }

    public static void updateItem(Item item) throws SQLException {
        String query = "UPDATE items SET name = ?, quantity = ?, price = ?, personName = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getPersonName());
            stmt.setInt(5, item.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteItem(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        Scanner u = new Scanner(System.in);
        while (true) {
            System.out.println("Inventory Management System");
            System.out.println("1. Add Item");
            System.out.println("2. View All Items");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = u.nextInt();
            u.nextLine();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter person's name: ");
                        String personName = u.nextLine();
                        System.out.print("Enter item name: ");
                        String name = u.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity = u.nextInt();
                        System.out.print("Enter price: ");
                        double price = u.nextDouble();
                        addItem(new Item(name, quantity, price, personName));
                        System.out.println("Item added successfully.");
                        break;
                    case 2:
                        List<Item> items = getAllItems();
                        System.out.println("Items in Inventory:");
                        for (Item item : items) {
                            System.out.println(item.getId() + " - " + item.getName() + " - " +item.getQuantity() + " - " + item.getPrice() + " - " + item.getPersonName());
                        }
                        break;
                    case 3:
                        System.out.print("Enter item ID to update: ");
                        int updateId = u.nextInt();
                        List<Item> allItems = getAllItems();
                        Item itemToUpdate = null;
                        for (Item item : allItems) {
                            if (item.getId() == updateId) {
                                itemToUpdate = item;
                                break;
                            }
                        }
                        if (itemToUpdate != null) {
                            
                            System.out.print("Enter new person's name: ");
                            itemToUpdate.setPersonName(u.nextLine());
                            System.out.print("Enter new item name: ");
                            itemToUpdate.setName(u.nextLine());
                            System.out.print("Enter new quantity: ");
                            itemToUpdate.setQuantity(u.nextInt());
                            System.out.print("Enter new price: ");
                            itemToUpdate.setPrice(u.nextDouble());
                            updateItem(itemToUpdate);
                            System.out.println("Item updated successfully.");
                        } else {
                            System.out.println("Item with ID " + updateId + " not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter item ID to delete: ");
                        int deleteId = u.nextInt();
                        deleteItem(deleteId);
                        System.out.println("Item deleted successfully.");
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
