package repository;

import domain.Order;
import domain.Payment;
import domain.User;
import org.bson.types.ObjectId;

public interface UserRepository {
    User getUserById(ObjectId id);

    void placeOrder(User user, Order order);

    void payOrder(User userById, Order orderById, Payment payment);
}
