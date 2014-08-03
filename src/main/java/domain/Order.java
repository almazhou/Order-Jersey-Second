package domain;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private  ObjectId userId;
    private  String deliveryAddress;
    private  double price;
    private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
    private ObjectId id;

    public Order(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Order(String orderId, String userId, String deliveryAddress, double price){
        this.id = new ObjectId(orderId);
        this.userId = new ObjectId(userId);
        this.deliveryAddress = deliveryAddress;
        this.price = price;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItemList.add(orderItem);
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
}
