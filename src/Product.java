public class Product {
    private String category;
    private String description;
    private String packaging;
    private double price;
    private int productID;
    private String productName;

    public Product(String category, String description, String packaging, double price, int productID,
            String productName) {
        this.category = category;
        this.description = description;
        this.packaging = packaging;
        this.price = price;
        this.productID = productID;
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String displayProduct() {
        return "Product name:" + productName + "; price: " + price + "; packaging: " + packaging + "; \n description: "
                + description;
    }

    public String displayProduct1() {
        return category + ";" + description + ";" + packaging + ";" + price + ";" + productID + ";" + productName + ";";
    }
}