package inventorymanagementsystem;

public class Category {

    private int    categoryId;
    private String categoryName;

    public Category(int categoryId, String categoryName) {
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
    }

    public int    getCategoryId()   { return categoryId; }
    public String getCategoryName() { return categoryName; }

    public void setCategoryId(int categoryId)        { this.categoryId   = categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return "[ID: " + categoryId + "] " + categoryName;
    }
}