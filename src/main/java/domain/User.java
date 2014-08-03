package domain;

import exception.RecordNotFoundException;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Order getOrderById(ObjectId orderId) {
        List<Order> filteredOrder = orders.stream().filter(order -> order.getId().toString().equals(orderId.toString())).collect(Collectors.toList());
        if(filteredOrder.size() == 0){
            throw new RecordNotFoundException();
        }

        return filteredOrder.get(0);
    }
}
