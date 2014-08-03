package domain;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Order> orders = new ArrayList<Order>();
    private ObjectId id;

    protected User(ObjectId id) {
        this.id = id;
    }

    public void placeOrder(Order order) {
        this.orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public ObjectId getId() {
        return id;
    }
}
