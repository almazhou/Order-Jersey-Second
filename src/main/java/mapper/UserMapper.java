package mapper;

import domain.Order;
import domain.Payment;
import domain.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import repository.UserRepository;

public class UserMapper implements UserRepository{
    private Datastore dataStore;

    public UserMapper(Datastore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public User getUserById(ObjectId id) {
        return dataStore.get(User.class,id);
    }

    @Override
    public void placeOrder(User user, Order order) {
        user.placeOrder(order);
        dataStore.save(order);
        dataStore.save(user);

    }

    @Override
    public void payOrder(User userById, Order orderById, Payment payment) {
        orderById.pay(payment);
        userById.placeOrder(orderById);
        dataStore.save(payment);
        dataStore.save(orderById);
        dataStore.save(userById);

    }

    @Override
    public void save(User user) {
        dataStore.save(user);
    }
}
