package inventorymanagementsystem;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PurchaseDAO {

    // ── Add Purchase ─────────────────────────────────────────────────────────
    public boolean addPurchase(int productId, int quantityAdded, String addedBy) {
        String checkProduct   = "SELECT price FROM products WHERE productid = ?";
        String insertPurchase = "INSERT INTO purchases (productid, quantityadded, totalcost, purchasedate, addedby) VALUES (?,?,?,?,?)";
        String updateStock    = "UPDATE products SET quantity = quantity + ? WHERE productid = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            double price;
            try (PreparedStatement cs = conn.prepareStatement(checkProduct)) {
                cs.setInt(1, productId);
                try (ResultSet rs = cs.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    price = rs.getDouble("price");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertPurchase)) {
                ps.setInt(1, productId); ps.setInt(2, quantityAdded);
                ps.setDouble(3, price * quantityAdded); ps.setObject(4, LocalDateTime.now());
                ps.setString(5, addedBy); ps.executeUpdate();
            }
            try (PreparedStatement us = conn.prepareStatement(updateStock)) {
                us.setInt(1, quantityAdded); us.setInt(2, productId); us.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Purchase failed: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ── Get All Purchases ────────────────────────────────────────────────────
    public ArrayList<Purchase> getAllPurchases() {
        return queryPurchases("SELECT * FROM purchases ORDER BY purchasedate DESC", null, null);
    }

    // ── Get Purchases By Date Range ───────────────────────────────────────────
    public ArrayList<Purchase> getPurchasesByDateRange(LocalDate from, LocalDate to) {
        return queryPurchases(
                "SELECT * FROM purchases WHERE DATE(purchasedate) BETWEEN ? AND ? ORDER BY purchasedate DESC",
                from, to);
    }

    // ── Internal Query Helper ─────────────────────────────────────────────────
    private ArrayList<Purchase> queryPurchases(String sql, LocalDate from, LocalDate to) {
        ArrayList<Purchase> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (from != null) stmt.setDate(1, java.sql.Date.valueOf(from));
            if (to   != null) stmt.setDate(2, java.sql.Date.valueOf(to));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Purchase(
                            rs.getInt("purchaseid"),    rs.getInt("productid"),
                            rs.getInt("quantityadded"), rs.getDouble("totalcost"),
                            rs.getObject("purchasedate", LocalDateTime.class),
                            rs.getString("addedby")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching purchases: " + e.getMessage());
        }
        return list;
    }
}