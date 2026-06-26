package inventorymanagementsystem;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    // ── Login ────────────────────────────────────────────────────────────────
    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password");
                    if (PasswordUtil.verifyPassword(password, stored)) {
                        return new User(rs.getInt("userid"), rs.getString("username"),
                                stored, rs.getString("role"));
                    }
                }
            }
        } catch (SQLException e) { System.out.println("Login error: " + e.getMessage()); }
        return null;
    }

    // ── Get All Users ────────────────────────────────────────────────────────
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY userid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("userid"), rs.getString("username"),
                        rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) { System.out.println("Error fetching users: " + e.getMessage()); }
        return users;
    }

    // ── Add User ─────────────────────────────────────────────────────────────
    public boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, PasswordUtil.hashPassword(password));
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("Error adding user: " + e.getMessage()); return false; }
    }

    // ── Delete User ──────────────────────────────────────────────────────────
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE userid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("Error deleting user: " + e.getMessage()); return false; }
    }

    // ── Change Password ───────────────────────────────────────────────────────
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE userid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, PasswordUtil.hashPassword(newPassword));
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("Error changing password: " + e.getMessage()); return false; }
    }
}