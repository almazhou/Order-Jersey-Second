package domain;

public class OrderItem {
    private  Product productId;
    private  int quantity;

    public OrderItem(Product productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
