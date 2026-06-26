package inventorymanagementsystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileHandler {

    private static final String FOLDER_NAME = "files";
    private static final String LOG_FILE = "files/system_logs.txt";
    private static final String REPORT_FILE = "files/inventory_report.txt";

    public static void createFolder() {
        File folder = new File(FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static void writeLog(String message) {
        createFolder();
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }

    public static void exportInventoryReport(ArrayList<Product> products) {
        createFolder();
        try (FileWriter writer = new FileWriter(REPORT_FILE)) {
            writer.write("========================================\n");
            writer.write("      Inventory Management System       \n");
            writer.write("            Inventory Report            \n");
            writer.write("Generated At: " + LocalDateTime.now() + "\n");
            writer.write("========================================\n\n");

            writer.write(String.format("%-5s %-20s %-15s %-10s %-10s %-20s\n",
                    "ID", "Name", "Category", "Price", "Qty", "Supplier"));
            writer.write("----------------------------------------------------------------------\n");

            if (products.isEmpty()) {
                writer.write("No products found.\n");
            } else {
                for (Product product : products) {
                    writer.write(String.format("%-5d %-20s %-15s %-10.2f %-10d %-20s\n",
                            product.getProductId(),
                            product.getProductName(),
                            product.getCategoryName(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getSupplierName()));
                }
            }

            writer.write("----------------------------------------------------------------------\n");
            System.out.println("Inventory report exported to: " + REPORT_FILE);

        } catch (IOException e) {
            System.out.println("Error exporting inventory report: " + e.getMessage());
        }
    }
}