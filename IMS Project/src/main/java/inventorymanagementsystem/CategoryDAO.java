package inventorymanagementsystem;

import java.sql.*;
import java.util.ArrayList;

public class CategoryDAO {

    // ── Get All Categories ───────────────────────────────────────────────────
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY categoryid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("categoryid"), rs.getString("categoryname")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
        return list;
    }

    // ── Add Category ─────────────────────────────────────────────────────────
    public boolean addCategory(String categoryName) {
        String sql = "INSERT INTO categories (categoryname) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
            return false;
        }
    }

    // ── Delete Category ──────────────────────────────────────────────────────
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE categoryid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    // ── Check How Many Products Use This Category ────────────────────────────
    public int getProductCount(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE categoryid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error checking product count: " + e.getMessage());
        }
        return 0;
    }
}