package inventorymanagementsystem;

public class Supplier {

    private int    supplierId;
    private String supplierName;
    private String contact;

    public Supplier(int supplierId, String supplierName, String contact) {
        this.supplierId   = supplierId;
        this.supplierName = supplierName;
        this.contact      = contact;
    }

    public int    getSupplierId()   { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getContact()      { return contact; }

    public void setSupplierId(int supplierId)        { this.supplierId   = supplierId; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public void setContact(String contact)           { this.contact      = contact; }

    @Override
    public String toString() {
        return "[ID: " + supplierId + "] " + supplierName;
    }
}