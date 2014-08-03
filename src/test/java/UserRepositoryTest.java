import db.MongoDb;
import domain.*;
import mapper.UserMapper;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class UserRepositoryTest {
    private Datastore dataStore;
    private UserMapper userMapper;

    @Before
    public void setUp() throws Exception {
        dataStore = MongoDb.createDataStore("test");
        userMapper = new UserMapper(dataStore);
    }

    @Test
    public void test_get_user_by_id() throws Exception {
        User user = new User();
        userMapper.save(user);

        User userById = userMapper.getUserById(user.getId());

        assertNotNull(userById);

    }

    @Test
    public void test_place_order() throws Exception {
        User user = new User();
        userMapper.save(user);

        OrderItem orderItem = new OrderItem(ProductBuilder.buildProduct(new ObjectId("123454545454545454545454"), "test", 78.9), 2);
        Order order = new Order("test address", Arrays.asList(orderItem));
        userMapper.placeOrder(user,order);

        assertNotNull(user.getOrders());
    }

    @Test
    public void test_pay_order() throws Exception {
        User user = new User();
        userMapper.save(user);

        OrderItem orderItem = new OrderItem(ProductBuilder.buildProduct(new ObjectId("123454545454545454545454"), "test", 78.9), 2);
        Order order = new Order("test address", Arrays.asList(orderItem));
        Payment payment = PaymentBuilder.buildPayment(new ObjectId("123454545454545454545454"), 89.0);
        userMapper.payOrder(user,order,payment);
        assertNotNull(order.getPayment());
    }



    @After
    public void tearDown() throws Exception {
        dataStore.getCollection(User.class).drop();
    }
}
