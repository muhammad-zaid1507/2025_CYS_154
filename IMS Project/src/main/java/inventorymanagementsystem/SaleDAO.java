package inventorymanagementsystem;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SaleDAO {

    // ── Add Sale ─────────────────────────────────────────────────────────────
    public String addSale(int productId, int quantitySold, String soldBy) {

        String checkStock  = "SELECT price, quantity, productname FROM products WHERE productid = ?";
        String insertSale  = "INSERT INTO sales (productid, quantitysold, totalamount, saledate, soldby) VALUES (?,?,?,?,?)";
        String updateStock = "UPDATE products SET quantity = quantity - ? WHERE productid = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            double price; int available; String productName;
            try (PreparedStatement cs = conn.prepareStatement(checkStock)) {
                cs.setInt(1, productId);
                try (ResultSet rs = cs.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return "PRODUCT_NOT_FOUND"; }
                    available   = rs.getInt("quantity");
                    price       = rs.getDouble("price");
                    productName = rs.getString("productname");
                }
            }

            if (available < quantitySold) { conn.rollback(); return "INSUFFICIENT_STOCK:" + available; }

            double total = price * quantitySold;

            int generatedSaleId = 0;
            LocalDateTime saleTime = LocalDateTime.now();
            try (PreparedStatement ss = conn.prepareStatement(insertSale, Statement.RETURN_GENERATED_KEYS)) {
                ss.setInt(1, productId); ss.setInt(2, quantitySold);
                ss.setDouble(3, total);  ss.setObject(4, saleTime);
                ss.setString(5, soldBy); ss.executeUpdate();
                try (ResultSet gk = ss.getGeneratedKeys()) {
                    if (gk.next()) generatedSaleId = gk.getInt(1);
                }
            }
            try (PreparedStatement us = conn.prepareStatement(updateStock)) {
                us.setInt(1, quantitySold); us.setInt(2, productId); us.executeUpdate();
            }

            conn.commit();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return buildReceiptString(generatedSaleId, productId, productName, quantitySold, price, total, saleTime, soldBy, fmt);

        } catch (SQLException e) {
            System.out.println("Sale failed: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return null;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ── Get All Sales ────────────────────────────────────────────────────────
    public ArrayList<Sale> getAllSales() {
        return querySales("SELECT * FROM sales ORDER BY saledate DESC", null, null);
    }

    // ── Get Sales By Date Range ───────────────────────────────────────────────
    public ArrayList<Sale> getSalesByDateRange(LocalDate from, LocalDate to) {
        return querySales(
                "SELECT * FROM sales WHERE DATE(saledate) BETWEEN ? AND ? ORDER BY saledate DESC",
                from, to);
    }

    // ── Get Receipt for Existing Sale ─────────────────────────────────────────
    public String getSaleReceipt(int saleId) {
        String sql = "SELECT s.*, p.productname FROM sales s " +
                "JOIN products p ON s.productid = p.productid " +
                "WHERE s.saleid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int    productId   = rs.getInt("productid");
                    String productName = rs.getString("productname");
                    int    qty         = rs.getInt("quantitysold");
                    double total       = rs.getDouble("totalamount");
                    double unitPrice   = qty > 0 ? total / qty : 0;
                    LocalDateTime date = rs.getObject("saledate", LocalDateTime.class);
                    String soldBy      = rs.getString("soldby");
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    return buildReceiptString(saleId, productId, productName, qty, unitPrice, total, date, soldBy, fmt);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching receipt: " + e.getMessage());
        }
        return null;
    }

    // ── Internal Query Helper ─────────────────────────────────────────────────
    private ArrayList<Sale> querySales(String sql, LocalDate from, LocalDate to) {
        ArrayList<Sale> sales = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (from != null) stmt.setDate(1, java.sql.Date.valueOf(from));
            if (to   != null) stmt.setDate(2, java.sql.Date.valueOf(to));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(new Sale(
                            rs.getInt("saleid"),     rs.getInt("productid"),
                            rs.getInt("quantitysold"), rs.getDouble("totalamount"),
                            rs.getObject("saledate", LocalDateTime.class),
                            rs.getString("soldby")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching sales: " + e.getMessage());
        }
        return sales;
    }

    // ── Receipt Builder ───────────────────────────────────────────────────────
    private String buildReceiptString(int saleId, int productId, String productName,
                                      int qty, double unitPrice, double total,
                                      LocalDateTime date, String soldBy,
                                      DateTimeFormatter fmt) {
        return "=========================================\n"
                + "            SALES RECEIPT               \n"
                + "=========================================\n"
                + (saleId > 0 ? "Sale ID    : " + saleId + "\n" : "")
                + "Date       : " + (date != null ? date.format(fmt) : "N/A") + "\n"
                + "Sold By    : " + soldBy + "\n"
                + "-----------------------------------------\n"
                + "Product    : " + productName + "\n"
                + "Product ID : " + productId + "\n"
                + "Quantity   : " + qty + "\n"
                + "Unit Price : Rs. " + String.format("%.2f", unitPrice) + "\n"
                + "-----------------------------------------\n"
                + "TOTAL      : Rs. " + String.format("%.2f", total) + "\n"
                + "=========================================\n"
                + "      Thank you for your business!      \n"
                + "=========================================";
    }
}