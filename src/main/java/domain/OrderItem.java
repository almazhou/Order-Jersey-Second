package domain;

import org.bson.types.ObjectId;

public class OrderItem {
    private  Product productId;
    private  int quantity;

    public OrderItem(Product productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public ObjectId getProductId() {
        return productId.getId();
    }
}
