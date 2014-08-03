package domain;

import exception.RecordNotFoundException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Entity("users")
public class User {
    private List<Order> orders = new ArrayList<Order>();
    @Id
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

    public User() {
    }

    public Order getOrderById(ObjectId orderId) {
        List<Order> filteredOrder = orders.stream().filter(order -> order.getId().toString().equals(orderId.toString())).collect(Collectors.toList());
        if(filteredOrder.size() == 0){
            throw new RecordNotFoundException();
        }

        return filteredOrder.get(0);
    }
}
