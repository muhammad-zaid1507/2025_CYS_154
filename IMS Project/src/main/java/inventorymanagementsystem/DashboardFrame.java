package inventorymanagementsystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.print.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class DashboardFrame extends JFrame {

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final User         loggedInUser;
    private final ProductDAO   productDAO   = new ProductDAO();
    private final SaleDAO      saleDAO      = new SaleDAO();
    private final PurchaseDAO  purchaseDAO  = new PurchaseDAO();
    private final CategoryDAO  categoryDAO  = new CategoryDAO();
    private final SupplierDAO  supplierDAO  = new SupplierDAO();
    private final UserDAO      userDAO      = new UserDAO();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    // ── Table Models ──────────────────────────────────────────────────────────
    private DefaultTableModel productsModel;
    private DefaultTableModel salesModel;
    private DefaultTableModel purchasesModel;
    private DefaultTableModel lowStockModel;
    private DefaultTableModel categoriesModel;
    private DefaultTableModel suppliersModel;
    private DefaultTableModel usersModel;

    // ── Row Sorters ───────────────────────────────────────────────────────────
    private TableRowSorter<DefaultTableModel> productsSorter;
    private TableRowSorter<DefaultTableModel> salesSorter;
    private TableRowSorter<DefaultTableModel> purchasesSorter;
    private TableRowSorter<DefaultTableModel> lowStockSorter;
    private TableRowSorter<DefaultTableModel> categoriesSorter;
    private TableRowSorter<DefaultTableModel> suppliersSorter;
    private TableRowSorter<DefaultTableModel> usersSorter;

    // ── Dashboard Metric Labels ───────────────────────────────────────────────
    private JLabel lblTotalProducts, lblTotalSales, lblTotalPurchases;
    private JLabel lblLowStock, lblRevenue, lblProfit;

    // ── Reports Labels ────────────────────────────────────────────────────────
    private JLabel lblRevenueReport, lblCostReport, lblProfitReport;

    // ── Chart + Status ────────────────────────────────────────────────────────
    private SalesChartPanel salesChart;
    private JLabel          statusLbl;
    private JLabel          clockLbl;

    private JTabbedPane tabs;

    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════
    public DashboardFrame(User user) {
        this.loggedInUser = user;
        setTitle("Inventory Management System — " + user.getUsername());
        setSize(1200, 740);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_PRIMARY);
        setLayout(new BorderLayout());
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildTabs(),      BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            loadDashboard();
            showLowStockAlert();
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    // HEADER
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_HEADER);
        header.setPreferredSize(new Dimension(1200, 58));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(0, 22, 0, 22)));

        // Left — accent dot + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JPanel dot = new JPanel();
        dot.setBackground(Theme.ACCENT);
        dot.setPreferredSize(new Dimension(10, 10));
        JLabel title = new JLabel("Inventory Management System");
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        left.add(dot); left.add(title);

        // Right — badge + user + buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JLabel roleBadge = new JLabel("  " + loggedInUser.getRole().toUpperCase() + "  ");
        roleBadge.setFont(new Font("Arial", Font.BOLD, 10));
        roleBadge.setForeground(Theme.ACCENT);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(new Color(99, 102, 241, 30));
        roleBadge.setBorder(BorderFactory.createLineBorder(Theme.ACCENT));

        JLabel userLbl = new JLabel(loggedInUser.getUsername());
        userLbl.setForeground(Theme.TEXT_SECONDARY);
        userLbl.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton changePwdBtn = makeBtn("Change Password", Theme.BG_TERTIARY);
        JButton aboutBtn     = makeBtn("About",           Theme.BG_TERTIARY);
        JButton logoutBtn    = makeBtn("Logout",          Theme.DANGER);

        changePwdBtn.addActionListener(e -> showChangePasswordDialog());
        aboutBtn.addActionListener(e -> showAboutDialog());
        logoutBtn.addActionListener(e -> {
            FileHandler.writeLog("User [" + loggedInUser.getUsername() + "] logged out.");
            dispose(); new LoginFrame();
        });

        right.add(roleBadge); right.add(userLbl);
        right.add(changePwdBtn); right.add(aboutBtn); right.add(logoutBtn);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ════════════════════════════════════════════════════════════════════════
    // STATUS BAR
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG_HEADER);
        bar.setPreferredSize(new Dimension(1200, 26));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        statusLbl = new JLabel("  Ready");
        statusLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLbl.setForeground(Theme.TEXT_MUTED);

        clockLbl = new JLabel(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("  HH:mm:ss  ")));
        clockLbl.setFont(new Font("Monospaced", Font.BOLD, 11));
        clockLbl.setForeground(Theme.ACCENT);
        new javax.swing.Timer(1000, e ->
            clockLbl.setText(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("  HH:mm:ss  ")))
        ).start();

        JLabel versionLbl = new JLabel("IMS v1.0  |  Java Swing + MySQL  ");
        versionLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        versionLbl.setForeground(Theme.TEXT_MUTED);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 3));
        right.setOpaque(false);
        right.add(clockLbl);
        right.add(versionLbl);

        bar.add(statusLbl, BorderLayout.WEST);
        bar.add(right,     BorderLayout.EAST);
        return bar;
    }

    private void setStatus(String msg) {
        SwingUtilities.invokeLater(() -> statusLbl.setText("  " + msg));
    }

    // ════════════════════════════════════════════════════════════════════════
    // TABS
    // ════════════════════════════════════════════════════════════════════════
    private JTabbedPane buildTabs() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 12));
        tabs.setBackground(Theme.BG_SECONDARY);
        tabs.setForeground(Theme.TEXT_PRIMARY);

        tabs.addTab("  Dashboard  ",  buildDashboardTab());
        tabs.addTab("  Products  ",   buildProductsTab());
        tabs.addTab("  Sales  ",      buildSalesTab());
        tabs.addTab("  Purchases  ",  buildPurchasesTab());
        tabs.addTab("  Reports  ",    buildReportsTab());

        if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
            tabs.addTab("  Categories  ", buildCategoriesTab());
            tabs.addTab("  Suppliers  ",  buildSuppliersTab());
            tabs.addTab("  Users  ",      buildUsersTab());
        }

        tabs.addChangeListener(e -> {
            String t = tabs.getTitleAt(tabs.getSelectedIndex()).trim();
            switch (t) {
                case "Dashboard"  -> loadDashboard();
                case "Products"   -> loadProducts();
                case "Sales"      -> loadSales();
                case "Purchases"  -> loadPurchases();
                case "Reports"    -> loadReports();
                case "Categories" -> loadCategories();
                case "Suppliers"  -> loadSuppliers();
                case "Users"      -> loadUsers();
            }
        });
        return tabs;
    }

    // ════════════════════════════════════════════════════════════════════════
    // DASHBOARD TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 24));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JPanel welcome = new JPanel(new BorderLayout(0, 5));
        welcome.setOpaque(false);
        JLabel welcomeLbl = new JLabel("Welcome back, " + loggedInUser.getUsername() + "");
        welcomeLbl.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLbl.setForeground(Theme.TEXT_PRIMARY);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));
        JLabel dateLbl = new JLabel(date);
        dateLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        dateLbl.setForeground(Theme.TEXT_SECONDARY);
        welcome.add(welcomeLbl, BorderLayout.NORTH);
        welcome.add(dateLbl,    BorderLayout.SOUTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        lblTotalProducts  = metricValueLabel(Theme.ACCENT);
        lblTotalSales     = metricValueLabel(Theme.SUCCESS);
        lblTotalPurchases = metricValueLabel(Theme.INFO);
        lblLowStock       = metricValueLabel(Theme.WARNING);
        lblRevenue        = metricValueLabel(Theme.SUCCESS);
        lblProfit         = metricValueLabel(Theme.SUCCESS);
        grid.add(buildMetricCard("Total Products",   lblTotalProducts,  Theme.ACCENT,   "products"));
        grid.add(buildMetricCard("Total Sales",      lblTotalSales,     Theme.SUCCESS,  "transactions"));
        grid.add(buildMetricCard("Total Purchases",  lblTotalPurchases, Theme.INFO,     "restocks"));
        grid.add(buildMetricCard("Low Stock Items",  lblLowStock,       Theme.WARNING,  "need restock"));
        grid.add(buildMetricCard("Total Revenue",    lblRevenue,        Theme.SUCCESS,  "from sales"));
        grid.add(buildMetricCard("Net Profit",       lblProfit,         Theme.SUCCESS,  "revenue - cost"));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottom.setOpaque(false);
        JButton refreshBtn = makeBtn("Refresh Dashboard", Theme.ACCENT);
        refreshBtn.addActionListener(e -> loadDashboard());
        bottom.add(refreshBtn);

        panel.add(welcome, BorderLayout.NORTH);
        panel.add(grid,    BorderLayout.CENTER);
        panel.add(bottom,  BorderLayout.SOUTH);
        return panel;
    }

    private JLabel metricValueLabel(Color c) {
        JLabel lbl = new JLabel("...");
        lbl.setFont(new Font("Arial", Font.BOLD, 30));
        lbl.setForeground(c);
        return lbl;
    }

    private JPanel buildMetricCard(String title, JLabel valueLabel, Color accent, String sub) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Theme.BG_CARD);
        outer.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        JPanel bar = new JPanel();
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(4, 0));
        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.setBackground(Theme.BG_CARD);
        content.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLbl.setForeground(Theme.TEXT_SECONDARY);
        JPanel bot = new JPanel(new BorderLayout());
        bot.setOpaque(false);
        JLabel subLbl = new JLabel(sub.toUpperCase());
        subLbl.setFont(new Font("Arial", Font.BOLD, 10));
        subLbl.setForeground(Theme.TEXT_MUTED);
        bot.add(subLbl, BorderLayout.WEST);
        content.add(titleLbl,   BorderLayout.NORTH);
        content.add(valueLabel, BorderLayout.CENTER);
        content.add(bot,        BorderLayout.SOUTH);
        outer.add(bar,     BorderLayout.WEST);
        outer.add(content, BorderLayout.CENTER);
        return outer;
    }

    // ════════════════════════════════════════════════════════════════════════
    // PRODUCTS TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildProductsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setBackground(Theme.BG_PRIMARY);
        JTextField searchField = darkTextField(16);
        searchField.setPreferredSize(new Dimension(180, 32));
        JButton searchBtn  = makeBtn("Search",     Theme.INFO);
        JButton addBtn     = makeBtn("Add",         Theme.SUCCESS);
        JButton editBtn    = makeBtn("Edit",        Theme.WARNING);
        JButton deleteBtn  = makeBtn("Delete",      Theme.DANGER);
        JButton refreshBtn = makeBtn("Show All",    Theme.BG_TERTIARY);
        JButton csvBtn     = makeBtn("Export CSV",  new Color(55, 65, 81));
        JLabel sl = darkLabel("Search:"); sl.setForeground(Theme.TEXT_SECONDARY);
        top.add(sl); top.add(searchField); top.add(searchBtn);
        top.add(Box.createHorizontalStrut(15));
        top.add(addBtn); top.add(editBtn); top.add(deleteBtn); top.add(refreshBtn); top.add(csvBtn);

        String[] cols = {"ID", "Name", "Category", "Price (Rs.)", "Quantity", "Supplier"};
        productsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        // ── Products table with low-stock highlighting ────────────────────────
        JTable table = new JTable(productsModel) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    try {
                        int modelRow = convertRowIndexToModel(row);
                        int qty = Integer.parseInt(getModel().getValueAt(modelRow, 4).toString());
                        if (qty <= 5) { c.setBackground(Theme.LOW_STOCK_BG); c.setForeground(Theme.LOW_STOCK_FG); }
                        else { c.setBackground(row % 2 == 0 ? Theme.ROW_EVEN : Theme.ROW_ODD); c.setForeground(Theme.TEXT_PRIMARY); }
                    } catch (Exception ex) { c.setBackground(Theme.ROW_EVEN); c.setForeground(Theme.TEXT_PRIMARY); }
                }
                return c;
            }
        };
        styleTable(table);

        // ── Column sorting ────────────────────────────────────────────────────
        productsSorter = new TableRowSorter<>(productsModel);
        table.setRowSorter(productsSorter);

        // ── Button actions ────────────────────────────────────────────────────
        searchBtn.addActionListener(e -> {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) { productsSorter.setRowFilter(null); return; }
            try { productsSorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw)); }
            catch (Exception ex) { showMsg("Invalid search term."); }
        });
        searchField.addActionListener(e -> searchBtn.doClick());
        refreshBtn.addActionListener(e -> { searchField.setText(""); productsSorter.setRowFilter(null); loadProducts(); });
        addBtn.addActionListener(e -> showAddProductDialog());
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showMsg("Select a product to edit."); return; }
            showEditProductDialog(table.convertRowIndexToModel(row));
        });
        csvBtn.addActionListener(e -> exportTableToCSV(productsModel, "products_export.csv"));

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showMsg("Select a product to delete."); return; }
            int mrow = table.convertRowIndexToModel(row);
            int id   = (int)    productsModel.getValueAt(mrow, 0);
            String name = (String) productsModel.getValueAt(mrow, 1);
            int sc = productDAO.getSalesCount(id), pc = productDAO.getPurchasesCount(id);
            if (sc > 0 || pc > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "\"" + name + "\" has linked records:\n\n  Sales: " + sc + "   Purchases: " + pc + "\n\nDelete everything permanently?",
                        "Force Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (productDAO.forceDeleteProduct(id)) { FileHandler.writeLog("Force deleted: " + name); loadProducts(); loadDashboard(); showMsg("Product and all records deleted."); }
                    else { showErr("Force delete failed."); }
                }
            } else {
                if (JOptionPane.showConfirmDialog(this, "Delete \"" + name + "\"?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (productDAO.deleteProduct(id)) { FileHandler.writeLog("Deleted: " + name); loadProducts(); loadDashboard(); showMsg("Deleted."); }
                    else { showErr("Delete failed."); }
                }
            }
        });

        JLabel hint = new JLabel("   Red rows = quantity ≤ 5  |  Click column headers to sort");
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(Theme.TEXT_MUTED);

        panel.add(top, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    private void showAddProductDialog() {
        ArrayList<Category> categories = categoryDAO.getAllCategories();
        ArrayList<Supplier> suppliers   = supplierDAO.getAllSuppliers();
        if (categories.isEmpty()) { showMsg("Please add at least one Category first (Admin → Categories tab)."); return; }
        if (suppliers.isEmpty())  { showMsg("Please add at least one Supplier first (Admin → Suppliers tab)."); return; }

        JDialog dlg = darkDialog("Add New Product", 420, 320);
        GridBagConstraints g = formGbc();

        JTextField nameF  = darkTextField(18);
        JTextField priceF = darkTextField(18);
        JTextField qtyF   = darkTextField(18);

        JComboBox<String> catBox = styledComboBox();
        for (Category c : categories) catBox.addItem("[" + c.getCategoryId() + "]  " + c.getCategoryName());

        JComboBox<String> supBox = styledComboBox();
        for (Supplier s : suppliers) supBox.addItem("[" + s.getSupplierId() + "]  " + s.getSupplierName());

        String[]     labels = {"Product Name:", "Category:", "Supplier:", "Price (Rs.):", "Quantity:"};
        java.awt.Component[] fields = {nameF, catBox, supBox, priceF, qtyF};
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i; g.gridx = 0; g.gridwidth = 1; dlg.add(darkLabel(labels[i]), g);
            g.gridx = 1; dlg.add(fields[i], g);
        }

        JButton sv = makeBtn("Save Product", Theme.SUCCESS);
        g.gridy = 5; g.gridx = 0; g.gridwidth = 2; g.insets = new Insets(18, 14, 14, 14); dlg.add(sv, g);
        sv.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                if (name.isEmpty()) { showMsg("Name cannot be empty."); return; }
                int catId = categories.get(catBox.getSelectedIndex()).getCategoryId();
                int supId = suppliers.get(supBox.getSelectedIndex()).getSupplierId();
                if (productDAO.addProduct(name, catId,
                        Double.parseDouble(priceF.getText().trim()),
                        Integer.parseInt(qtyF.getText().trim()), supId)) {
                    FileHandler.writeLog("Added product: " + name); loadProducts(); dlg.dispose(); showMsg("Product added!");
                } else { showErr("Failed to add product."); }
            } catch (NumberFormatException ex) { showErr("Enter valid numbers for Price and Quantity."); }
        });
        dlg.setVisible(true);
    }

    private void showEditProductDialog(int modelRow) {
        int id = (int) productsModel.getValueAt(modelRow, 0);
        JDialog dlg = darkDialog("Edit Product", 375, 270);
        GridBagConstraints g = formGbc();
        JTextField nameF  = darkTextField(18); nameF.setText((String) productsModel.getValueAt(modelRow, 1));
        JTextField priceF = darkTextField(18); priceF.setText(productsModel.getValueAt(modelRow, 3).toString());
        JTextField qtyF   = darkTextField(18); qtyF.setText(String.valueOf(productsModel.getValueAt(modelRow, 4)));
        String[] labels = {"Product Name:", "Price (Rs.):", "Quantity:"};
        JTextField[] fields = {nameF, priceF, qtyF};
        for (int i = 0; i < labels.length; i++) {
            g.gridy = i; g.gridx = 0; g.gridwidth = 1; dlg.add(darkLabel(labels[i]), g);
            g.gridx = 1; dlg.add(fields[i], g);
        }
        JButton btn = makeBtn("Update Product", Theme.WARNING);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2; g.insets = new Insets(18, 14, 14, 14); dlg.add(btn, g);
        btn.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                if (name.isEmpty()) { showMsg("Name cannot be empty."); return; }
                if (productDAO.updateProduct(id, name, Double.parseDouble(priceF.getText().trim()),
                        Integer.parseInt(qtyF.getText().trim()))) {
                    FileHandler.writeLog("Updated product ID: " + id); loadProducts(); dlg.dispose(); showMsg("Product updated!");
                } else { showErr("Update failed."); }
            } catch (NumberFormatException ex) { showErr("Enter valid numbers."); }
        });
        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // SALES TAB — date filter + history search + double-click receipt
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildSalesTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── Add Sale Form ────────────────────────────────────────────────────
        JPanel addForm = darkFormPanel("Add New Sale");
        JTextField addPidF = darkTextField(8); addPidF.setPreferredSize(new Dimension(90, 30));
        JTextField addQtyF = darkTextField(8); addQtyF.setPreferredSize(new Dimension(90, 30));
        JButton addSaleBtn = makeBtn("Add Sale", Theme.SUCCESS);
        addForm.add(darkLabel("Product ID:")); addForm.add(addPidF);
        addForm.add(Box.createHorizontalStrut(5));
        addForm.add(darkLabel("Quantity:"));   addForm.add(addQtyF);
        addForm.add(Box.createHorizontalStrut(5)); addForm.add(addSaleBtn);

        // ── History Filter: Date + Search ────────────────────────────────────
        JPanel filterForm = darkFormPanel("Filter Sales History");
        JTextField fromF       = dateField(); JTextField toF = dateField();
        JTextField histSearchF = darkTextField(14); histSearchF.setPreferredSize(new Dimension(140, 30));
        JButton filterBtn  = makeBtn("By Date",   Theme.ACCENT);
        JButton searchBtn  = makeBtn("Search",    Theme.INFO);
        JButton showAllBtn = makeBtn("Show All",  Theme.BG_TERTIARY);

        JButton salesCsvBtn = makeBtn("Export CSV", new Color(55, 65, 81));

        filterForm.add(darkLabel("From:")); filterForm.add(fromF);
        filterForm.add(Box.createHorizontalStrut(3));
        filterForm.add(darkLabel("To:")); filterForm.add(toF);
        filterForm.add(filterBtn);
        filterForm.add(Box.createHorizontalStrut(10));
        filterForm.add(darkLabel("Search:")); filterForm.add(histSearchF);
        filterForm.add(searchBtn); filterForm.add(showAllBtn); filterForm.add(salesCsvBtn);

        JPanel topSection = new JPanel(new GridLayout(2, 1, 0, 6));
        topSection.setOpaque(false);
        topSection.add(addForm); topSection.add(filterForm);

        // ── Sales Table ──────────────────────────────────────────────────────
        String[] cols = {"Sale ID", "Product ID", "Qty Sold", "Total (Rs.)", "Date & Time", "Sold By"};
        salesModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = darkBasicTable(salesModel);

        // Column sorting
        salesSorter = new TableRowSorter<>(salesModel);
        table.setRowSorter(salesSorter);

        // ── Double-click → view receipt ──────────────────────────────────────
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row < 0) return;
                    int saleId = (int) salesModel.getValueAt(table.convertRowIndexToModel(row), 0);
                    String receipt = saleDAO.getSaleReceipt(saleId);
                    if (receipt != null) showReceiptPopup(receipt, "Sale Receipt — ID: " + saleId);
                }
            }
        });

        // ── Actions ──────────────────────────────────────────────────────────
        addSaleBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(addPidF.getText().trim());
                int qty = Integer.parseInt(addQtyF.getText().trim());
                if (qty <= 0) { showMsg("Quantity must be > 0."); return; }
                String result = saleDAO.addSale(pid, qty, loggedInUser.getUsername());
                if      (result == null)                     showErr("Sale failed. Check Product ID.");
                else if (result.equals("PRODUCT_NOT_FOUND")) showMsg("Product ID " + pid + " not found.");
                else if (result.startsWith("INSUFFICIENT"))  showMsg("Not enough stock!\nAvailable: " + result.split(":")[1]);
                else {
                    FileHandler.writeLog("Sale by [" + loggedInUser.getUsername() + "] Product ID: " + pid + " Qty: " + qty);
                    loadSales(); loadProducts(); loadDashboard();
                    addPidF.setText(""); addQtyF.setText("");
                    showReceiptPopup(result, "Sale Receipt");
                }
            } catch (NumberFormatException ex) { showErr("Enter valid numbers."); }
        });

        filterBtn.addActionListener(e -> {
            LocalDate from = parseDate(fromF), to = parseDate(toF);
            if (from == null || to == null) { showMsg("Enter valid dates in dd-MM-yyyy format."); return; }
            if (from.isAfter(to)) { showMsg("'From' date cannot be after 'To' date."); return; }
            setStatus("Filtering sales...");
            salesModel.setRowCount(0);
            for (Sale s : saleDAO.getSalesByDateRange(from, to)) {
                salesModel.addRow(new Object[]{ s.getSaleId(), s.getProductId(), s.getQuantitySold(),
                        String.format("%.2f", s.getTotalAmount()),
                        s.getSaleDate() != null ? s.getSaleDate().format(DISPLAY_FMT) : "",
                        s.getSoldBy() });
            }
            setStatus("Showing " + salesModel.getRowCount() + " filtered sales.");
        });

        searchBtn.addActionListener(e -> {
            String kw = histSearchF.getText().trim();
            if (kw.isEmpty()) { salesSorter.setRowFilter(null); return; }
            try { salesSorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw)); setStatus("Search results for: " + kw); }
            catch (Exception ex) { showMsg("Invalid search term."); }
        });
        histSearchF.addActionListener(e -> searchBtn.doClick());

        showAllBtn.addActionListener(e -> {
            fromF.setText("dd-MM-yyyy"); fromF.setForeground(Theme.TEXT_MUTED);
            toF.setText("dd-MM-yyyy");   toF.setForeground(Theme.TEXT_MUTED);
            histSearchF.setText("");     salesSorter.setRowFilter(null);
            loadSales();
        });
        salesCsvBtn.addActionListener(e -> exportTableToCSV(salesModel, "sales_export.csv"));

        JLabel hint = darkNote("Double-click any row to view its receipt  |  Click column headers to sort");

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // PURCHASES TAB — date filter + sorting
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildPurchasesTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel addForm = darkFormPanel("Add New Purchase");
        JTextField addPidF = darkTextField(8); addPidF.setPreferredSize(new Dimension(90, 30));
        JTextField addQtyF = darkTextField(8); addQtyF.setPreferredSize(new Dimension(90, 30));
        JButton addBtn = makeBtn("Add Purchase", Theme.INFO);
        addForm.add(darkLabel("Product ID:")); addForm.add(addPidF);
        addForm.add(Box.createHorizontalStrut(5));
        addForm.add(darkLabel("Quantity:")); addForm.add(addQtyF);
        addForm.add(Box.createHorizontalStrut(5)); addForm.add(addBtn);

        JPanel filterForm = darkFormPanel("Filter Purchase History");
        JTextField fromF = dateField(); JTextField toF = dateField();
        JButton filterBtn    = makeBtn("By Date",   Theme.ACCENT);
        JButton showAllBtn   = makeBtn("Show All",  Theme.BG_TERTIARY);
        JButton purchCsvBtn  = makeBtn("Export CSV", new Color(55, 65, 81));
        filterForm.add(darkLabel("From:")); filterForm.add(fromF);
        filterForm.add(Box.createHorizontalStrut(3));
        filterForm.add(darkLabel("To:")); filterForm.add(toF);
        filterForm.add(filterBtn); filterForm.add(showAllBtn); filterForm.add(purchCsvBtn);

        JPanel topSection = new JPanel(new GridLayout(2, 1, 0, 6));
        topSection.setOpaque(false);
        topSection.add(addForm); topSection.add(filterForm);

        String[] cols = {"Purchase ID", "Product ID", "Qty Added", "Total Cost (Rs.)", "Date & Time", "Added By"};
        purchasesModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = darkBasicTable(purchasesModel);

        // Column sorting
        purchasesSorter = new TableRowSorter<>(purchasesModel);
        table.setRowSorter(purchasesSorter);

        addBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(addPidF.getText().trim());
                int qty = Integer.parseInt(addQtyF.getText().trim());
                if (qty <= 0) { showMsg("Quantity must be > 0."); return; }
                if (purchaseDAO.addPurchase(pid, qty, loggedInUser.getUsername())) {
                    FileHandler.writeLog("Purchase by [" + loggedInUser.getUsername() + "] Product ID: " + pid);
                    loadPurchases(); loadProducts(); loadDashboard();
                    addPidF.setText(""); addQtyF.setText(""); showMsg("Purchase recorded!");
                } else { showErr("Purchase failed. Check Product ID."); }
            } catch (NumberFormatException ex) { showErr("Enter valid numbers."); }
        });

        filterBtn.addActionListener(e -> {
            LocalDate from = parseDate(fromF), to = parseDate(toF);
            if (from == null || to == null) { showMsg("Enter valid dates in dd-MM-yyyy format."); return; }
            if (from.isAfter(to)) { showMsg("'From' date cannot be after 'To' date."); return; }
            purchasesModel.setRowCount(0);
            for (Purchase p : purchaseDAO.getPurchasesByDateRange(from, to)) {
                purchasesModel.addRow(new Object[]{ p.getPurchaseId(), p.getProductId(), p.getQuantityAdded(),
                        String.format("%.2f", p.getTotalCost()),
                        p.getPurchaseDate() != null ? p.getPurchaseDate().format(DISPLAY_FMT) : "",
                        p.getAddedBy() });
            }
            setStatus("Showing " + purchasesModel.getRowCount() + " filtered purchases.");
        });
        showAllBtn.addActionListener(e -> {
            fromF.setText("dd-MM-yyyy"); fromF.setForeground(Theme.TEXT_MUTED);
            toF.setText("dd-MM-yyyy");   toF.setForeground(Theme.TEXT_MUTED);
            loadPurchases();
        });
        purchCsvBtn.addActionListener(e -> exportTableToCSV(purchasesModel, "purchases_export.csv"));

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(darkNote("Click column headers to sort"), BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // REPORTS TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(Theme.BG_PRIMARY);
        JButton exportBtn       = makeBtn("Export Report",       Theme.ACCENT);
        JButton refreshChartBtn = makeBtn("Refresh Chart",       Theme.INFO);
        JButton refreshStockBtn = makeBtn("Refresh Low Stock",   Theme.DANGER);
        btnRow.add(exportBtn); btnRow.add(refreshChartBtn); btnRow.add(refreshStockBtn);

        JPanel profitGrid = new JPanel(new GridLayout(1, 3, 14, 0));
        profitGrid.setOpaque(false);
        lblRevenueReport = metricValueLabel(Theme.SUCCESS);
        lblCostReport    = metricValueLabel(Theme.DANGER);
        lblProfitReport  = metricValueLabel(Theme.SUCCESS);
        profitGrid.add(buildMetricCard("Total Revenue",   lblRevenueReport, Theme.SUCCESS, "all time"));
        profitGrid.add(buildMetricCard("Total Cost",      lblCostReport,    Theme.DANGER,  "all time"));
        profitGrid.add(buildMetricCard("Net Profit",      lblProfitReport,  Theme.SUCCESS, "revenue - cost"));

        JPanel topSection = new JPanel(new BorderLayout(0, 12));
        topSection.setOpaque(false);
        topSection.add(btnRow,      BorderLayout.NORTH);
        topSection.add(profitGrid,  BorderLayout.CENTER);

        salesChart = new SalesChartPanel("Top 5 Products by Sales Revenue (Rs.)");
        salesChart.setPreferredSize(new Dimension(600, 260));
        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setBackground(Theme.BG_SECONDARY);
        chartWrapper.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        chartWrapper.add(salesChart, BorderLayout.CENTER);

        JLabel lowStockHeading = new JLabel("   Low Stock Products  (Quantity ≤ 5)");
        lowStockHeading.setFont(new Font("Arial", Font.BOLD, 13));
        lowStockHeading.setForeground(Theme.DANGER);

        String[] cols = {"ID", "Name", "Category", "Price (Rs.)", "Quantity", "Supplier"};
        lowStockModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lowStockTable = darkBasicTable(lowStockModel);
        lowStockSorter = new TableRowSorter<>(lowStockModel);
        lowStockTable.setRowSorter(lowStockSorter);

        JPanel lowStockSection = new JPanel(new BorderLayout(0, 6));
        lowStockSection.setBackground(Theme.BG_PRIMARY);
        lowStockSection.add(lowStockHeading, BorderLayout.NORTH);
        lowStockSection.add(darkScrollPane(lowStockTable), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartWrapper, lowStockSection);
        split.setDividerLocation(270);
        split.setDividerSize(5);
        split.setBorder(null);
        split.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(javax.swing.border.Border b) {}
                    public void paint(Graphics g) { g.setColor(Theme.BORDER); g.fillRect(0, 0, getWidth(), getHeight()); }
                };
            }
        });
        split.setBackground(Theme.BG_PRIMARY);

        exportBtn.addActionListener(e -> {
            FileHandler.exportInventoryReport(productDAO.getAllProducts());
            FileHandler.writeLog("User [" + loggedInUser.getUsername() + "] exported report.");
            showMsg("Report saved to: files/inventory_report.txt");
        });
        refreshChartBtn.addActionListener(e -> loadChart());
        refreshStockBtn.addActionListener(e -> loadLowStock());

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(split,      BorderLayout.CENTER);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // CATEGORIES TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildCategoriesTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(Theme.BG_PRIMARY);
        JButton addBtn = makeBtn("Add Category",    Theme.SUCCESS);
        JButton delBtn = makeBtn("Delete Category", Theme.DANGER);
        JButton refBtn = makeBtn("Refresh",         Theme.BG_TERTIARY);
        top.add(addBtn); top.add(delBtn); top.add(refBtn);
        String[] cols = {"Category ID", "Category Name"};
        categoriesModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = darkBasicTable(categoriesModel);
        categoriesSorter = new TableRowSorter<>(categoriesModel);
        table.setRowSorter(categoriesSorter);

        addBtn.addActionListener(e -> {
            JDialog dlg = darkDialog("Add Category", 340, 170);
            GridBagConstraints g = formGbc();
            JTextField nameF = darkTextField(18);
            g.gridy=0; g.gridx=0; g.gridwidth=1; dlg.add(darkLabel("Category Name:"),g); g.gridx=1; dlg.add(nameF,g);
            JButton sv = makeBtn("Add Category", Theme.SUCCESS);
            g.gridy=1; g.gridx=0; g.gridwidth=2; g.insets=new Insets(16,14,14,14); dlg.add(sv,g);
            sv.addActionListener(ev -> {
                String name = nameF.getText().trim();
                if (name.isEmpty()) { showMsg("Name cannot be empty."); return; }
                if (categoryDAO.addCategory(name)) { FileHandler.writeLog("Added category: "+name); loadCategories(); dlg.dispose(); showMsg("Category added!"); }
                else { showErr("Failed."); }
            });
            dlg.setVisible(true);
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showMsg("Select a category."); return; }
            int mrow = table.convertRowIndexToModel(row);
            int id = (int) categoriesModel.getValueAt(mrow, 0);
            String name = (String) categoriesModel.getValueAt(mrow, 1);
            int pc = categoryDAO.getProductCount(id);
            if (pc > 0) { showMsg("Cannot delete \""+name+"\".\n"+pc+" product(s) use this.\nReassign them first."); return; }
            if (JOptionPane.showConfirmDialog(this,"Delete \""+name+"\"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if (categoryDAO.deleteCategory(id)) { FileHandler.writeLog("Deleted category: "+name); loadCategories(); showMsg("Deleted."); }
                else { showErr("Delete failed."); }
            }
        });
        refBtn.addActionListener(e -> loadCategories());
        panel.add(top, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(darkNote("Category IDs are used when adding products."), BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SUPPLIERS TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildSuppliersTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(Theme.BG_PRIMARY);
        JButton addBtn = makeBtn("Add Supplier",    Theme.SUCCESS);
        JButton delBtn = makeBtn("Delete Supplier", Theme.DANGER);
        JButton refBtn = makeBtn("Refresh",         Theme.BG_TERTIARY);
        top.add(addBtn); top.add(delBtn); top.add(refBtn);
        String[] cols = {"Supplier ID", "Supplier Name", "Contact"};
        suppliersModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = darkBasicTable(suppliersModel);
        suppliersSorter = new TableRowSorter<>(suppliersModel);
        table.setRowSorter(suppliersSorter);

        addBtn.addActionListener(e -> {
            JDialog dlg = darkDialog("Add Supplier", 360, 210);
            GridBagConstraints g = formGbc();
            JTextField nameF = darkTextField(18), contactF = darkTextField(18);
            g.gridy=0; g.gridx=0; g.gridwidth=1; dlg.add(darkLabel("Supplier Name:"),g); g.gridx=1; dlg.add(nameF,g);
            g.gridy=1; g.gridx=0; dlg.add(darkLabel("Contact:"),g); g.gridx=1; dlg.add(contactF,g);
            JButton sv = makeBtn("Add Supplier", Theme.SUCCESS);
            g.gridy=2; g.gridx=0; g.gridwidth=2; g.insets=new Insets(16,14,14,14); dlg.add(sv,g);
            sv.addActionListener(ev -> {
                String name = nameF.getText().trim();
                if (name.isEmpty()) { showMsg("Name cannot be empty."); return; }
                if (supplierDAO.addSupplier(name, contactF.getText().trim())) { FileHandler.writeLog("Added supplier: "+name); loadSuppliers(); dlg.dispose(); showMsg("Supplier added!"); }
                else { showErr("Failed."); }
            });
            dlg.setVisible(true);
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showMsg("Select a supplier."); return; }
            int mrow = table.convertRowIndexToModel(row);
            int id = (int) suppliersModel.getValueAt(mrow, 0);
            String name = (String) suppliersModel.getValueAt(mrow, 1);
            int pc = supplierDAO.getProductCount(id);
            if (pc > 0) { showMsg("Cannot delete \""+name+"\".\n"+pc+" product(s) use this.\nReassign them first."); return; }
            if (JOptionPane.showConfirmDialog(this,"Delete \""+name+"\"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if (supplierDAO.deleteSupplier(id)) { FileHandler.writeLog("Deleted supplier: "+name); loadSuppliers(); showMsg("Deleted."); }
                else { showErr("Delete failed."); }
            }
        });
        refBtn.addActionListener(e -> loadSuppliers());
        panel.add(top, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(darkNote("Supplier IDs are used when adding products."), BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // USERS TAB
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildUsersTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(Theme.BG_PRIMARY);
        JButton addBtn = makeBtn("Add User",    Theme.SUCCESS);
        JButton delBtn = makeBtn("Delete User", Theme.DANGER);
        JButton refBtn = makeBtn("Refresh",     Theme.BG_TERTIARY);
        top.add(addBtn); top.add(delBtn); top.add(refBtn);
        String[] cols = {"User ID", "Username", "Role"};
        usersModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = darkBasicTable(usersModel);
        usersSorter = new TableRowSorter<>(usersModel);
        table.setRowSorter(usersSorter);

        addBtn.addActionListener(e -> {
            JDialog dlg = darkDialog("Add New User", 360, 250);
            GridBagConstraints g = formGbc();
            JTextField usernameF = darkTextField(18);
            JPasswordField passwordF = styledPasswordField();
            String[] roles = {"user","admin"};
            JComboBox<String> roleBox = new JComboBox<>(roles);
            roleBox.setBackground(Theme.BG_TERTIARY); roleBox.setForeground(Theme.TEXT_PRIMARY); roleBox.setPreferredSize(new Dimension(200,34));
            roleBox.setRenderer(new javax.swing.DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                    super.getListCellRendererComponent(l,v,i,s,f); setBackground(s?Theme.ACCENT:Theme.BG_TERTIARY); setForeground(Theme.TEXT_PRIMARY); return this;
                }
            });
            g.gridy=0; g.gridx=0; g.gridwidth=1; dlg.add(darkLabel("Username:"),g); g.gridx=1; dlg.add(usernameF,g);
            g.gridy=1; g.gridx=0; dlg.add(darkLabel("Password:"),g); g.gridx=1; dlg.add(passwordF,g);
            g.gridy=2; g.gridx=0; dlg.add(darkLabel("Role:"),g); g.gridx=1; dlg.add(roleBox,g);
            JButton sv = makeBtn("Create User", Theme.SUCCESS);
            g.gridy=3; g.gridx=0; g.gridwidth=2; g.insets=new Insets(18,14,14,14); dlg.add(sv,g);
            sv.addActionListener(ev -> {
                String username = usernameF.getText().trim();
                String password = new String(passwordF.getPassword()).trim();
                String role = (String) roleBox.getSelectedItem();
                if (username.isEmpty()||password.isEmpty()) { showMsg("Username/password cannot be empty."); return; }
                if (password.length()<6) { showMsg("Password must be at least 6 characters."); return; }
                if (userDAO.addUser(username, password, role)) { FileHandler.writeLog("Created user: "+username); loadUsers(); dlg.dispose(); showMsg("User created!"); }
                else { showErr("Failed. Username may already exist."); }
            });
            dlg.setVisible(true);
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showMsg("Select a user."); return; }
            int mrow = table.convertRowIndexToModel(row);
            int id = (int) usersModel.getValueAt(mrow, 0);
            String name = (String) usersModel.getValueAt(mrow, 1);
            if (id==loggedInUser.getUserId()) { showMsg("You cannot delete your own account."); return; }
            if (JOptionPane.showConfirmDialog(this,"Delete user: \""+name+"\"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                if (userDAO.deleteUser(id)) { FileHandler.writeLog("Deleted user: "+name); loadUsers(); showMsg("User deleted."); }
                else { showErr("Delete failed."); }
            }
        });
        refBtn.addActionListener(e -> loadUsers());
        panel.add(top, BorderLayout.NORTH);
        panel.add(darkScrollPane(table), BorderLayout.CENTER);
        panel.add(darkNote("You cannot delete your own account."), BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // DATA LOADERS — SwingWorker for heavy queries
    // ════════════════════════════════════════════════════════════════════════
    private void loadDashboard() {
        new SwingWorker<double[], Void>() {
            protected double[] doInBackground() {
                return new double[]{
                    dashboardDAO.getTotalProducts(),
                    dashboardDAO.getTotalSales(),
                    dashboardDAO.getTotalPurchases(),
                    dashboardDAO.getLowStockCount(),
                    dashboardDAO.getTotalRevenue(),
                    dashboardDAO.getTotalCost()
                };
            }
            protected void done() {
                try {
                    double[] d = get();
                    int tp = (int)d[0], ts = (int)d[1], tpu = (int)d[2], ls = (int)d[3];
                    double rev = d[4], profit = d[4] - d[5];
                    lblTotalProducts.setText(String.valueOf(tp));
                    lblTotalSales.setText(String.valueOf(ts));
                    lblTotalPurchases.setText(String.valueOf(tpu));
                    lblLowStock.setText(String.valueOf(ls));
                    lblLowStock.setForeground(ls > 0 ? Theme.DANGER : Theme.SUCCESS);
                    lblRevenue.setText("Rs. " + String.format("%,.0f", rev));
                    lblProfit.setText("Rs. " + String.format("%,.0f", profit));
                    lblProfit.setForeground(profit >= 0 ? Theme.SUCCESS : Theme.DANGER);
                    setStatus("Dashboard updated.");
                } catch (InterruptedException | ExecutionException e) {
                    setStatus("Error loading dashboard.");
                }
            }
        }.execute();
    }

    private void loadProducts() {
        setStatus("Loading products...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<ArrayList<Product>, Void>() {
            protected ArrayList<Product> doInBackground() { return productDAO.getAllProducts(); }
            protected void done() {
                try {
                    ArrayList<Product> list = get();
                    productsModel.setRowCount(0);
                    for (Product p : list) addProductRow(p);
                    setStatus("Ready  —  " + list.size() + " products loaded.");
                } catch (InterruptedException | ExecutionException e) { setStatus("Error loading products."); }
                setCursor(Cursor.getDefaultCursor());
            }
        }.execute();
    }

    private void addProductRow(Product p) {
        productsModel.addRow(new Object[]{ p.getProductId(), p.getProductName(), p.getCategoryName(),
                String.format("%.2f", p.getPrice()), p.getQuantity(), p.getSupplierName() });
    }

    private void loadSales() {
        setStatus("Loading sales...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<ArrayList<Sale>, Void>() {
            protected ArrayList<Sale> doInBackground() { return saleDAO.getAllSales(); }
            protected void done() {
                try {
                    ArrayList<Sale> list = get();
                    salesModel.setRowCount(0);
                    for (Sale s : list) {
                        salesModel.addRow(new Object[]{ s.getSaleId(), s.getProductId(), s.getQuantitySold(),
                                String.format("%.2f", s.getTotalAmount()),
                                s.getSaleDate() != null ? s.getSaleDate().format(DISPLAY_FMT) : "",
                                s.getSoldBy() });
                    }
                    setStatus("Ready  —  " + list.size() + " sales loaded.");
                } catch (InterruptedException | ExecutionException e) { setStatus("Error loading sales."); }
                setCursor(Cursor.getDefaultCursor());
            }
        }.execute();
    }

    private void loadPurchases() {
        setStatus("Loading purchases...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<ArrayList<Purchase>, Void>() {
            protected ArrayList<Purchase> doInBackground() { return purchaseDAO.getAllPurchases(); }
            protected void done() {
                try {
                    ArrayList<Purchase> list = get();
                    purchasesModel.setRowCount(0);
                    for (Purchase p : list) {
                        purchasesModel.addRow(new Object[]{ p.getPurchaseId(), p.getProductId(), p.getQuantityAdded(),
                                String.format("%.2f", p.getTotalCost()),
                                p.getPurchaseDate() != null ? p.getPurchaseDate().format(DISPLAY_FMT) : "",
                                p.getAddedBy() });
                    }
                    setStatus("Ready  —  " + list.size() + " purchases loaded.");
                } catch (InterruptedException | ExecutionException e) { setStatus("Error loading purchases."); }
                setCursor(Cursor.getDefaultCursor());
            }
        }.execute();
    }

    private void loadReports() {
        loadChart(); loadLowStock(); loadProfitSummary();
    }

    private void loadChart() {
        setStatus("Loading chart...");
        new SwingWorker<LinkedHashMap<String, Double>, Void>() {
            protected LinkedHashMap<String, Double> doInBackground() {
                return dashboardDAO.getTopProductsByRevenue(5);
            }
            protected void done() {
                try {
                    LinkedHashMap<String, Double> data = get();
                    salesChart.setData(new ArrayList<>(data.keySet()), new ArrayList<>(data.values()));
                    setStatus("Chart updated.");
                } catch (InterruptedException | ExecutionException e) {
                    setStatus("Error loading chart.");
                }
            }
        }.execute();
    }

    private void loadProfitSummary() {
        new SwingWorker<double[], Void>() {
            protected double[] doInBackground() {
                return new double[]{dashboardDAO.getTotalRevenue(), dashboardDAO.getTotalCost()};
            }
            protected void done() {
                try {
                    double[] d = get();
                    double rev = d[0], cost = d[1], profit = d[0] - d[1];
                    lblRevenueReport.setText("Rs. " + String.format("%,.0f", rev));
                    lblCostReport.setText("Rs. " + String.format("%,.0f", cost));
                    lblProfitReport.setText("Rs. " + String.format("%,.0f", profit));
                    lblProfitReport.setForeground(profit >= 0 ? Theme.SUCCESS : Theme.DANGER);
                } catch (InterruptedException | ExecutionException e) {
                    setStatus("Error loading profit summary.");
                }
            }
        }.execute();
    }

    private void loadLowStock() {
        new SwingWorker<ArrayList<Product>, Void>() {
            protected ArrayList<Product> doInBackground() {
                return productDAO.getLowStockProducts(5);
            }
            protected void done() {
                try {
                    lowStockModel.setRowCount(0);
                    for (Product p : get()) {
                        lowStockModel.addRow(new Object[]{ p.getProductId(), p.getProductName(), p.getCategoryName(),
                                String.format("%.2f", p.getPrice()), p.getQuantity(), p.getSupplierName() });
                    }
                } catch (InterruptedException | ExecutionException e) {
                    setStatus("Error loading low stock.");
                }
            }
        }.execute();
    }

    private void loadCategories() {
        categoriesModel.setRowCount(0);
        for (Category c : categoryDAO.getAllCategories())
            categoriesModel.addRow(new Object[]{c.getCategoryId(), c.getCategoryName()});
        setStatus("Ready  —  " + categoriesModel.getRowCount() + " categories.");
    }

    private void loadSuppliers() {
        suppliersModel.setRowCount(0);
        for (Supplier s : supplierDAO.getAllSuppliers())
            suppliersModel.addRow(new Object[]{s.getSupplierId(), s.getSupplierName(), s.getContact()});
        setStatus("Ready  —  " + suppliersModel.getRowCount() + " suppliers.");
    }

    private void loadUsers() {
        usersModel.setRowCount(0);
        for (User u : userDAO.getAllUsers())
            usersModel.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getRole()});
        setStatus("Ready  —  " + usersModel.getRowCount() + " users.");
    }

    private void showLowStockAlert() {
        new SwingWorker<ArrayList<Product>, Void>() {
            protected ArrayList<Product> doInBackground() {
                return productDAO.getLowStockProducts(5);
            }
            protected void done() {
                try {
                    ArrayList<Product> low = get();
                    if (!low.isEmpty()) {
                        StringBuilder msg = new StringBuilder("Low Stock Alert!\n\nThese products need restocking:\n\n");
                        for (Product p : low)
                            msg.append(String.format("  •  %-22s  (ID: %d)  —  %d left\n", p.getProductName(), p.getProductId(), p.getQuantity()));
                        JOptionPane.showMessageDialog(DashboardFrame.this, msg.toString(), "Low Stock Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Low stock alert error: " + e.getMessage());
                }
            }
        }.execute();
    }

    // ════════════════════════════════════════════════════════════════════════
    // CHANGE PASSWORD DIALOG
    // ════════════════════════════════════════════════════════════════════════
    private void showChangePasswordDialog() {
        JDialog dlg = darkDialog("Change Password", 375, 280);
        GridBagConstraints g = formGbc();

        JPasswordField currentF = styledPasswordField();
        JPasswordField newF     = styledPasswordField();
        JPasswordField confirmF = styledPasswordField();

        g.gridy=0; g.gridx=0; g.gridwidth=1; dlg.add(darkLabel("Current Password:"), g); g.gridx=1; dlg.add(currentF, g);
        g.gridy=1; g.gridx=0; dlg.add(darkLabel("New Password:"),     g); g.gridx=1; dlg.add(newF, g);
        g.gridy=2; g.gridx=0; dlg.add(darkLabel("Confirm Password:"), g); g.gridx=1; dlg.add(confirmF, g);

        JLabel ruleNote = new JLabel("  Minimum 6 characters");
        ruleNote.setFont(new Font("Arial", Font.ITALIC, 11));
        ruleNote.setForeground(Theme.TEXT_MUTED);
        g.gridy=3; g.gridx=0; g.gridwidth=2; g.insets=new Insets(0, 14, 0, 14); dlg.add(ruleNote, g);

        JButton changeBtn = makeBtn("Change Password", Theme.ACCENT);
        g.gridy=4; g.insets=new Insets(18, 14, 14, 14); dlg.add(changeBtn, g);

        changeBtn.addActionListener(e -> {
            String current = new String(currentF.getPassword()).trim();
            String newPwd  = new String(newF.getPassword()).trim();
            String confirm = new String(confirmF.getPassword()).trim();

            if (current.isEmpty() || newPwd.isEmpty() || confirm.isEmpty()) { showMsg("All fields are required."); return; }
            if (!PasswordUtil.verifyPassword(current, loggedInUser.getPassword())) { showMsg("Current password is incorrect."); return; }
            if (!newPwd.equals(confirm)) { showMsg("New passwords do not match."); return; }
            if (newPwd.length() < 6)    { showMsg("New password must be at least 6 characters."); return; }
            if (newPwd.equals(current)) { showMsg("New password must be different from current password."); return; }

            if (userDAO.changePassword(loggedInUser.getUserId(), newPwd)) {
                FileHandler.writeLog("User [" + loggedInUser.getUsername() + "] changed their password.");
                dlg.dispose();
                showMsg("Password changed successfully!\nPlease remember your new password.");
                setStatus("Password changed.");
            } else {
                showErr("Failed to change password. Please try again.");
            }
        });

        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // ABOUT DIALOG
    // ════════════════════════════════════════════════════════════════════════
    private void showAboutDialog() {
        JDialog dlg = new JDialog(this, "About", true);
        dlg.setSize(400, 320);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_SECONDARY);
        dlg.setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Theme.BG_SECONDARY);
        content.setBorder(BorderFactory.createEmptyBorder(24, 36, 12, 36));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5, 0, 5, 0);

        // Accent bar at top
        JPanel accentBar = new JPanel();
        accentBar.setBackground(Theme.ACCENT);
        accentBar.setPreferredSize(new Dimension(400, 4));
        dlg.add(accentBar, BorderLayout.NORTH);

        JLabel appName = new JLabel("Inventory Management System");
        appName.setFont(new Font("Arial", Font.BOLD, 17));
        appName.setForeground(Theme.ACCENT);
        appName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel versionLbl = new JLabel("Version 1.0  —  Final Release");
        versionLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLbl.setForeground(Theme.TEXT_SECONDARY);
        versionLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);

        g.gridy=0; content.add(appName,    g);
        g.gridy=1; content.add(versionLbl, g);
        g.gridy=2; g.insets=new Insets(10,0,10,0); content.add(sep, g);
        g.insets=new Insets(5,0,5,0);

        String[][] info = {
                {"Developer",    "OOP Final Project"},
                {"Built With",   "Java Swing + MySQL"},
                {"Database",     "MySQL 8.0"},
                {"Architecture", "MVC + DAO Pattern"},
                {"Java Version", System.getProperty("java.version")},
        };

        for (int i = 0; i < info.length; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            JLabel key = new JLabel(info[i][0] + " :");
            key.setFont(new Font("Arial", Font.PLAIN, 13));
            key.setForeground(Theme.TEXT_SECONDARY);
            JLabel val = new JLabel(info[i][1]);
            val.setFont(new Font("Arial", Font.BOLD, 13));
            val.setForeground(Theme.TEXT_PRIMARY);
            row.add(key, BorderLayout.WEST);
            row.add(val, BorderLayout.EAST);
            g.gridy = 3 + i; content.add(row, g);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Theme.BG_SECONDARY);
        JButton closeBtn = makeBtn("Close", Theme.ACCENT);
        closeBtn.addActionListener(e2 -> dlg.dispose());
        btnPanel.add(closeBtn);

        dlg.add(content,  BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // RECEIPT — save to file + print
    // ════════════════════════════════════════════════════════════════════════
    private void showReceiptPopup(String receipt, String title) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(460, 400);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_SECONDARY);
        dlg.setLayout(new BorderLayout(0, 0));

        JTextArea area = new JTextArea(receipt);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(Theme.BG_TERTIARY);
        area.setForeground(Theme.TEXT_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane sp = new JScrollPane(area);
        sp.getViewport().setBackground(Theme.BG_TERTIARY);
        sp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 12, 0, 12),
                BorderFactory.createLineBorder(Theme.BORDER)));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnPanel.setBackground(Theme.BG_SECONDARY);
        JButton saveBtn  = makeBtn("Save to File", Theme.SUCCESS);
        JButton printBtn = makeBtn("Print",         Theme.INFO);
        JButton closeBtn = makeBtn("Close",         Theme.BG_TERTIARY);
        btnPanel.add(saveBtn); btnPanel.add(printBtn); btnPanel.add(closeBtn);

        saveBtn.addActionListener(e  -> saveReceiptToFile(receipt));
        printBtn.addActionListener(e -> printReceipt(receipt));
        closeBtn.addActionListener(e -> dlg.dispose());

        dlg.add(sp,       BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void saveReceiptToFile(String receipt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Receipt");
        chooser.setSelectedFile(new java.io.File("receipt_" + System.currentTimeMillis() + ".txt"));
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
                fw.write(receipt);
                setStatus("Receipt saved: " + chooser.getSelectedFile().getName());
                showMsg("Receipt saved to:\n" + chooser.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                showErr("Failed to save receipt:\n" + e.getMessage());
            }
        }
    }

    private void printReceipt(String receipt) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Sale Receipt");
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g2.setColor(Color.BLACK);
            String[] lines = receipt.split("\n");
            int y = (int) pageFormat.getImageableY() + 20;
            int x = (int) pageFormat.getImageableX() + 10;
            for (String line : lines) {
                g2.drawString(line, x, y);
                y += 16;
            }
            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
                setStatus("Receipt sent to printer.");
                showMsg("Receipt sent to printer successfully!");
            } catch (PrinterException e) {
                showErr("Print failed:\n" + e.getMessage());
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DARK THEME HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false); btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JTextField darkTextField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(Theme.BG_TERTIARY); f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.TEXT_PRIMARY); f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(200, 34));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField(18);
        f.setBackground(Theme.BG_TERTIARY); f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.TEXT_PRIMARY); f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(200, 34));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JTextField dateField() {
        JTextField f = darkTextField(10);
        f.setPreferredSize(new Dimension(112, 30));
        f.setText("dd-MM-yyyy"); f.setForeground(Theme.TEXT_MUTED);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals("dd-MM-yyyy")) { f.setText(""); f.setForeground(Theme.TEXT_PRIMARY); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText("dd-MM-yyyy"); f.setForeground(Theme.TEXT_MUTED); }
            }
        });
        return f;
    }

    private LocalDate parseDate(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty() || text.equals("dd-MM-yyyy")) return null;
        try { return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MM-yyyy")); }
        catch (Exception e) { return null; }
    }

    private JLabel darkLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Theme.TEXT_SECONDARY); lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        return lbl;
    }

    private JLabel darkNote(String text) {
        JLabel lbl = new JLabel("   " + text);
        lbl.setFont(new Font("Arial", Font.ITALIC, 11)); lbl.setForeground(Theme.TEXT_MUTED);
        return lbl;
    }

    private JPanel darkFormPanel(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        p.setBackground(Theme.BG_SECONDARY);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), title);
        tb.setTitleColor(Theme.TEXT_SECONDARY); p.setBorder(tb);
        return p;
    }

    private JDialog darkDialog(String title, int w, int h) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(w, h); dlg.setLocationRelativeTo(this);
        dlg.setLayout(new GridBagLayout()); dlg.getContentPane().setBackground(Theme.BG_SECONDARY);
        return dlg;
    }

    private JScrollPane darkScrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.BG_PRIMARY);
        sp.setBackground(Theme.BG_PRIMARY); sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return sp;
    }

    private JTable darkBasicTable(DefaultTableModel model) {
        JTable t = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Theme.ROW_EVEN : Theme.ROW_ODD);
                    c.setForeground(Theme.TEXT_PRIMARY);
                }
                return c;
            }
        };
        styleTable(t); return t;
    }

    private void styleTable(JTable t) {
        t.setBackground(Theme.BG_PRIMARY); t.setForeground(Theme.TEXT_PRIMARY);
        t.setFont(new Font("Arial", Font.PLAIN, 13)); t.setRowHeight(30);
        t.setGridColor(Theme.BORDER);
        t.setSelectionBackground(Theme.ROW_SELECTED); t.setSelectionForeground(Theme.TEXT_PRIMARY);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        t.getTableHeader().setBackground(Theme.BG_TERTIARY);
        t.getTableHeader().setForeground(Theme.TEXT_PRIMARY);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        t.getTableHeader().setPreferredSize(new Dimension(0, 38));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
    }

    private GridBagConstraints formGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.gridx = 0; g.insets = new Insets(7, 14, 7, 14);
        return g;
    }

    private void showMsg(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void showErr(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    // ════════════════════════════════════════════════════════════════════════
    // CSV EXPORT — exports any table model to a user-chosen .csv file
    // ════════════════════════════════════════════════════════════════════════
    private void exportTableToCSV(DefaultTableModel model, String defaultFilename) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export as CSV");
        chooser.setSelectedFile(new java.io.File(defaultFilename));
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col > 0) sb.append(",");
                sb.append("\"").append(model.getColumnName(col)).append("\"");
            }
            sb.append("\n");
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col > 0) sb.append(",");
                    Object val = model.getValueAt(row, col);
                    sb.append("\"").append(val != null ? val.toString().replace("\"", "\"\"") : "").append("\"");
                }
                sb.append("\n");
            }
            fw.write(sb.toString());
            FileHandler.writeLog("User [" + loggedInUser.getUsername() + "] exported CSV: " + chooser.getSelectedFile().getName());
            setStatus("Exported: " + chooser.getSelectedFile().getName());
            showMsg("CSV exported to:\n" + chooser.getSelectedFile().getAbsolutePath());
        } catch (IOException e) {
            showErr("Failed to export CSV:\n" + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // STYLED COMBO BOX — dark-themed dropdown helper
    // ════════════════════════════════════════════════════════════════════════
    private JComboBox<String> styledComboBox() {
        JComboBox<String> box = new JComboBox<>();
        box.setBackground(Theme.BG_TERTIARY);
        box.setForeground(Theme.TEXT_PRIMARY);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setPreferredSize(new Dimension(200, 34));
        box.setRenderer(new javax.swing.DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setBackground(s ? Theme.ACCENT : Theme.BG_TERTIARY);
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        return box;
    }
}