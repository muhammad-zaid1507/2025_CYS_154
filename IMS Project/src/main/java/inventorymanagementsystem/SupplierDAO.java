package inventorymanagementsystem;

import java.sql.*;
import java.util.ArrayList;

public class SupplierDAO {

    // ── Get All Suppliers ────────────────────────────────────────────────────
    public ArrayList<Supplier> getAllSuppliers() {
        ArrayList<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY supplierid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Supplier(
                        rs.getInt("supplierid"),
                        rs.getString("suppliername"),
                        rs.getString("contact")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }
        return list;
    }

    // ── Add Supplier ─────────────────────────────────────────────────────────
    public boolean addSupplier(String supplierName, String contact) {
        String sql = "INSERT INTO suppliers (suppliername, contact) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplierName);
            stmt.setString(2, contact);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding supplier: " + e.getMessage());
            return false;
        }
    }

    // ── Delete Supplier ──────────────────────────────────────────────────────
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplierid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

    // ── Check How Many Products Use This Supplier ────────────────────────────
    public int getProductCount(int supplierId) {
        String sql = "SELECT COUNT(*) FROM products WHERE supplierid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error checking product count: " + e.getMessage());
        }
        return 0;
    }
}