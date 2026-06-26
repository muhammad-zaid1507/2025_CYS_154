package inventorymanagementsystem;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         statusLabel;
    private final UserDAO  userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Inventory Management System");
        setSize(460, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_PRIMARY);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Theme.BG_PRIMARY);
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        card.setPreferredSize(new Dimension(380, 430));

        card.add(buildCardHeader(), BorderLayout.NORTH);
        card.add(buildCardForm(),   BorderLayout.CENTER);

        return card;
    }

    private JPanel buildCardHeader() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG_CARD);

        // Top accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(Theme.ACCENT);
        accentLine.setPreferredSize(new Dimension(380, 4));
        wrapper.add(accentLine, BorderLayout.NORTH);

        // Header text
        JPanel header = new JPanel();
        header.setBackground(Theme.BG_CARD);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(28, 0, 20, 0));

        JLabel titleLbl = new JLabel("Inventory Management");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 22));
        titleLbl.setForeground(Theme.TEXT_PRIMARY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("Sign in to your account");
        subLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        subLbl.setForeground(Theme.TEXT_SECONDARY);
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(titleLbl);
        header.add(Box.createVerticalStrut(6));
        header.add(subLbl);

        wrapper.add(header, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCardForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Username
        usernameField = darkField();
        usernameField.setPreferredSize(new Dimension(300, 40));

        // Password
        passwordField = new JPasswordField();
        passwordField.setBackground(Theme.BG_TERTIARY);
        passwordField.setForeground(Theme.TEXT_PRIMARY);
        passwordField.setCaretColor(Theme.TEXT_PRIMARY);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        // Login Button
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(Theme.ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(300, 42));
        loginBtn.setBorder(BorderFactory.createEmptyBorder());
        loginBtn.setOpaque(true);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Theme.DANGER);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout
        gbc.gridy = 0; form.add(fieldLabel("Username"), gbc);
        gbc.gridy = 1; form.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(14, 0, 5, 0);
        form.add(fieldLabel("Password"), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        form.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(22, 0, 8, 0);
        form.add(loginBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 0, 0, 0);
        form.add(statusLabel, gbc);

        // Listeners
        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        return form;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(Theme.TEXT_SECONDARY);
        return lbl;
    }

    private JTextField darkField() {
        JTextField f = new JTextField();
        f.setBackground(Theme.BG_TERTIARY);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.TEXT_PRIMARY);
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter username and password.", Theme.DANGER);
            return;
        }

        showStatus("Authenticating...", Theme.ACCENT);
        User user = userDAO.loginUser(username, password);

        if (user != null) {
            FileHandler.writeLog("User [" + user.getUsername() + "] logged in.");
            dispose();
            new DashboardFrame(user);
        } else {
            showStatus("Invalid username or password.", Theme.DANGER);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void showStatus(String msg, Color color) {
        statusLabel.setForeground(color);
        statusLabel.setText(msg);
    }
}