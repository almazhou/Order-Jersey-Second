package domain;

public class OrderBuilder {

    public static Order buildOrder(String orderId, String userId, String deliveryAddress, double price) {
        return new Order(orderId,userId,deliveryAddress,price);
    }
}
