package inventorymanagementsystem;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {

    // ── Get All Products ─────────────────────────────────────────────────────
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT p.productid, p.productname, p.categoryid, c.categoryname, " +
                "p.price, p.quantity, s.suppliername " +
                "FROM products p " +
                "JOIN categories c ON p.categoryid = c.categoryid " +
                "JOIN suppliers  s ON p.supplierid  = s.supplierid " +
                "ORDER BY p.productid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) products.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    // ── Get Low Stock ────────────────────────────────────────────────────────
    public ArrayList<Product> getLowStockProducts(int threshold) {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT p.productid, p.productname, p.categoryid, c.categoryname, " +
                "p.price, p.quantity, s.suppliername " +
                "FROM products p " +
                "JOIN categories c ON p.categoryid = c.categoryid " +
                "JOIN suppliers  s ON p.supplierid  = s.supplierid " +
                "WHERE p.quantity <= ? ORDER BY p.quantity ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching low stock: " + e.getMessage());
        }
        return products;
    }

    // ── Add Product ──────────────────────────────────────────────────────────
    public boolean addProduct(String productName, int categoryId, double price,
                              int quantity, int supplierId) {
        String sql = "INSERT INTO products (productname, categoryid, price, quantity, supplierid) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setInt(2, categoryId);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setInt(5, supplierId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    // ── Update Product ───────────────────────────────────────────────────────
    public boolean updateProduct(int productId, String productName,
                                 double price, int quantity) {
        String sql = "UPDATE products SET productname=?, price=?, quantity=? WHERE productid=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setInt(4, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    // ── Simple Delete (no linked records) ────────────────────────────────────
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE productid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    // ── Check How Many Sales are Linked ──────────────────────────────────────
    public int getSalesCount(int productId) {
        String sql = "SELECT COUNT(*) FROM sales WHERE productid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error checking sales count: " + e.getMessage());
        }
        return 0;
    }

    // ── Check How Many Purchases are Linked ──────────────────────────────────
    public int getPurchasesCount(int productId) {
        String sql = "SELECT COUNT(*) FROM purchases WHERE productid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error checking purchases count: " + e.getMessage());
        }
        return 0;
    }

    // ── Force Delete: removes sales + purchases + product in one transaction ─
    public boolean forceDeleteProduct(int productId) {
        String deleteSales     = "DELETE FROM sales     WHERE productid = ?";
        String deletePurchases = "DELETE FROM purchases WHERE productid = ?";
        String deleteProduct   = "DELETE FROM products  WHERE productid = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement s = conn.prepareStatement(deleteSales)) {
                s.setInt(1, productId); s.executeUpdate();
            }
            try (PreparedStatement s = conn.prepareStatement(deletePurchases)) {
                s.setInt(1, productId); s.executeUpdate();
            }
            try (PreparedStatement s = conn.prepareStatement(deleteProduct)) {
                s.setInt(1, productId); s.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Force delete failed: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ── Search Products ──────────────────────────────────────────────────────
    public ArrayList<Product> searchProducts(String keyword) {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT p.productid, p.productname, p.categoryid, c.categoryname, " +
                "p.price, p.quantity, s.suppliername " +
                "FROM products p " +
                "JOIN categories c ON p.categoryid = c.categoryid " +
                "JOIN suppliers  s ON p.supplierid  = s.supplierid " +
                "WHERE p.productname LIKE ? OR c.categoryname LIKE ? OR s.suppliername LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String p = "%" + keyword + "%";
            stmt.setString(1, p); stmt.setString(2, p); stmt.setString(3, p);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching: " + e.getMessage());
        }
        return products;
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("productid"), rs.getString("productname"),
                rs.getInt("categoryid"), rs.getString("categoryname"),
                rs.getDouble("price"), rs.getInt("quantity"), rs.getString("suppliername")
        );
    }
}