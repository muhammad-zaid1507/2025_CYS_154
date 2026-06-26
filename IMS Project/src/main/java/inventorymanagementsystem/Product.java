package inventorymanagementsystem;

public class Product {

    private int productId;
    private String productName;
    private int categoryId;
    private String categoryName;
    private double price;
    private int quantity;
    private String supplierName;

    public Product(int productId, String productName, int categoryId,
                   String categoryName, double price, int quantity, String supplierName) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
    }

    // Getters
    public int getProductId()       { return productId; }
    public String getProductName()  { return productName; }
    public int getCategoryId()      { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public double getPrice()        { return price; }
    public int getQuantity()        { return quantity; }
    public String getSupplierName() { return supplierName; }

    // Setters
    public void setProductId(int productId)           { this.productId = productId; }
    public void setProductName(String productName)    { this.productName = productName; }
    public void setCategoryId(int categoryId)         { this.categoryId = categoryId; }
    public void setCategoryName(String categoryName)  { this.categoryName = categoryName; }
    public void setPrice(double price)                { this.price = price; }
    public void setQuantity(int quantity)             { this.quantity = quantity; }
    public void setSupplierName(String supplierName)  { this.supplierName = supplierName; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Category: %s | Price: %.2f | Qty: %d | Supplier: %s",
                productId, productName, categoryName, price, quantity, supplierName);
    }
}