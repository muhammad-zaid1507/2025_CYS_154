package inventorymanagementsystem;

import java.sql.*;
import java.util.LinkedHashMap;

public class DashboardDAO {

    public int getTotalProducts()  { return queryInt("SELECT COUNT(*) FROM products"); }
    public int getTotalSales()     { return queryInt("SELECT COUNT(*) FROM sales"); }
    public int getTotalPurchases() { return queryInt("SELECT COUNT(*) FROM purchases"); }
    public int getLowStockCount()  { return queryInt("SELECT COUNT(*) FROM products WHERE quantity <= 5"); }
    public double getTotalRevenue(){ return queryDouble("SELECT COALESCE(SUM(totalamount),0) FROM sales"); }
    public double getTotalCost()   { return queryDouble("SELECT COALESCE(SUM(totalcost),0) FROM purchases"); }

    // ── Top N Products by Sales Revenue ──────────────────────────────────────
    public LinkedHashMap<String, Double> getTopProductsByRevenue(int limit) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT p.productname, COALESCE(SUM(s.totalamount), 0) AS revenue " +
                "FROM products p " +
                "LEFT JOIN sales s ON p.productid = s.productid " +
                "GROUP BY p.productname " +
                "ORDER BY revenue DESC " +
                "LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("productname"), rs.getDouble("revenue"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Chart data error: " + e.getMessage());
        }
        return result;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private int queryInt(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println("Dashboard error: " + e.getMessage()); }
        return 0;
    }

    private double queryDouble(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { System.out.println("Dashboard error: " + e.getMessage()); }
        return 0.0;
    }
}