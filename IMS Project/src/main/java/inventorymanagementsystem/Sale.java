package inventorymanagementsystem;

import java.time.LocalDateTime;

public class Sale {

    private int saleId;
    private int productId;
    private int quantitySold;
    private double totalAmount;
    private LocalDateTime saleDate;
    private String soldBy;

    public Sale(int saleId, int productId, int quantitySold,
                double totalAmount, LocalDateTime saleDate, String soldBy) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.soldBy = soldBy;
    }

    // Getters
    public int getSaleId()            { return saleId; }
    public int getProductId()         { return productId; }
    public int getQuantitySold()      { return quantitySold; }
    public double getTotalAmount()    { return totalAmount; }
    public LocalDateTime getSaleDate(){ return saleDate; }
    public String getSoldBy()         { return soldBy; }

    @Override
    public String toString() {
        return String.format("[Sale ID: %d] Product ID: %d | Qty: %d | Total: %.2f | Date: %s | By: %s",
                saleId, productId, quantitySold, totalAmount, saleDate, soldBy);
    }
}