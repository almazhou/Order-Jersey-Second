package repository;

import domain.Order;
import domain.User;
import org.bson.types.ObjectId;

public interface UserRepository {
    User getUserById(ObjectId id);

    void placeOrder(User user, Order order);
}
