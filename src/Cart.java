import java.time.LocalDateTime;

public class Cart {
    private LocalDateTime addTime;
    private Product product;
    private int quantity;

    public Cart(LocalDateTime addTime, Product product, int quantity) {
        this.addTime = addTime;
        this.product = product;
        this.quantity = quantity;
    }

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String displayCart() {
        return product.getProductName() + " x " + quantity + product.getPackaging() + " = $"
                + (product.getPrice() * quantity);
    }
}