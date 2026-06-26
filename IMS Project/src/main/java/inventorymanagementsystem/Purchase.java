package inventorymanagementsystem;

import java.time.LocalDateTime;

public class Purchase {

    private int purchaseId;
    private int productId;
    private int quantityAdded;
    private double totalCost;
    private LocalDateTime purchaseDate;
    private String addedBy;

    public Purchase(int purchaseId, int productId, int quantityAdded,
                    double totalCost, LocalDateTime purchaseDate, String addedBy) {
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.quantityAdded = quantityAdded;
        this.totalCost = totalCost;
        this.purchaseDate = purchaseDate;
        this.addedBy = addedBy;
    }

    // Getters
    public int getPurchaseId()              { return purchaseId; }
    public int getProductId()               { return productId; }
    public int getQuantityAdded()           { return quantityAdded; }
    public double getTotalCost()            { return totalCost; }
    public LocalDateTime getPurchaseDate()  { return purchaseDate; }
    public String getAddedBy()              { return addedBy; }

    @Override
    public String toString() {
        return String.format("[Purchase ID: %d] Product ID: %d | Qty Added: %d | Cost: %.2f | Date: %s | By: %s",
                purchaseId, productId, quantityAdded, totalCost, purchaseDate, addedBy);
    }
}