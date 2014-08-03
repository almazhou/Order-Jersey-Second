package domain;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private  ObjectId userId;
    private  String deliveryAddress;
    private  double price;
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
    private ObjectId id;
    private Payment payment;

    public Order(String deliveryAddress,List<OrderItem> orderItems) {
        this.deliveryAddress = deliveryAddress;
        this.orderItems = orderItems;
    }

    protected Order(String orderId, String userId, String deliveryAddress, double price){
        this.id = new ObjectId(orderId);
        this.userId = new ObjectId(userId);
        this.deliveryAddress = deliveryAddress;
        this.price = price;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public Object getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void pay(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
