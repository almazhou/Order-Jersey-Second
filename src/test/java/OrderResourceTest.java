import domain.*;
import exception.RecordNotFoundExceptionHandler;
import org.bson.types.ObjectId;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ProductRepository;
import repository.UserRepository;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderResourceTest extends JerseyTest{
    public static final String PRODUCT_ID = "1234567890abcdef12345678";
    public static final String ORDER_ID = "1234567890abcdef123456ef";
    private static final String  USER_ID = "1234567890abcdef123456ab";
    public static final String ORDER_NOT_EXIST = "123456789009876543212345";
    public static final String PAYMENT_ID = "126789090909090909090909";
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user;
    private Order order;

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .packages("resource")
                .register(new MoxyXmlFeature())
                .register(new MoxyJsonFeature())
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(userRepository).to(UserRepository.class);
                        bind(productRepository).to(ProductRepository.class);
                    }
                })
                .register(App.createMoxyJsonResolver())
                .register(RecordNotFoundExceptionHandler.class);

    }
    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        user = UserBuilder.BuildUser(new ObjectId(USER_ID));
        Product buildProduct = ProductBuilder.buildProduct(new ObjectId(PRODUCT_ID), "product one", 78.9);
        OrderItem orderItem = new OrderItem(buildProduct, 2);
        order = OrderBuilder.buildOrder(ORDER_ID, USER_ID, "street one", 560.0);
        order.addOrderItem(orderItem);
        Payment payment = PaymentBuilder.buildPayment(new ObjectId(PAYMENT_ID),789);
        order.pay(payment);
        user.placeOrder(order);
    }

    @Test
    public void should_return_200_for_get_all_orders() throws Exception {
        when(userRepository.getUserById(any())).thenReturn(user);

        Response response = target("/users/"+USER_ID+"/orders").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertThat(response.getStatus(),is(200));

        List list = response.readEntity(List.class);

        Map orderJson = (Map) list.get(0);

        assertThat(orderJson.get("id"),is(ORDER_ID));
        assertThat(orderJson.get("userId"),is(USER_ID));
        assertThat(orderJson.get("price"),is(560.0));
        assertThat(orderJson.get("deliveryAddress"),is("street one"));
        assertThat(((String)orderJson.get("uri")).contains("/users/"+USER_ID+"/orders/"+ORDER_ID),is(true));
    }
    @Test
    public void should_return_200_for_get_one_order() throws Exception {
        when(userRepository.getUserById(any())).thenReturn(user);

        Response response = target("/users/"+USER_ID+"/orders/"+ORDER_ID).request(MediaType.APPLICATION_JSON_TYPE).get();

        assertThat(response.getStatus(),is(200));

        Map orderJson = response.readEntity(Map.class);

        assertThat(orderJson.get("id"),is(ORDER_ID));
        assertThat(orderJson.get("userId"),is(USER_ID));
        assertThat(orderJson.get("price"),is(560.0));
        assertThat(orderJson.get("deliveryAddress"),is("street one"));
        assertThat(((String)orderJson.get("uri")).contains("/users/"+USER_ID+"/orders/"+ORDER_ID),is(true));
    }

    @Test
    public void should_return_200_for_get_all_order_xml() throws Exception {
        when(userRepository.getUserById(any())).thenReturn(user);

        Response response = target("/users/"+USER_ID+"/orders").request(MediaType.APPLICATION_XML_TYPE).get();

        assertThat(response.getStatus(),is(200));

        String responseString = response.readEntity(String.class);

        assertThat(responseString.contains("street one"),is(true));
        assertThat(responseString.contains(ORDER_ID),is(true));
        assertThat(responseString.contains(USER_ID),is(true));
        assertThat(responseString.contains("560"),is(true));
    }

    @Test
    public void should_return_404_when_order_not_exit() throws Exception {
        when(userRepository.getUserById(any())).thenReturn(user);

        Response response = target("/users/"+USER_ID+"/orders/"+ ORDER_NOT_EXIST).request(MediaType.APPLICATION_XML_TYPE).get();

        assertThat(response.getStatus(),is(404));
    }

    @Test
    @Ignore
    public void should_return_201_for_post_one_order() throws Exception {
        Product testProduct = ProductBuilder.buildProduct(new ObjectId(PRODUCT_ID), "productOne", 670.2);
        when(userRepository.getUserById(any())).thenReturn(user);
        when(productRepository.getProductById(any())).thenReturn(testProduct);

        Map createdOrder = new HashMap(){{
            put("deliveryAddress","street one zhouxuan");
            Map orderItem = new HashMap(){{
                put("id",PRODUCT_ID);
                put("quantity",2);
            }};
            List orderItems = new ArrayList();
            orderItems.add(orderItem);
            put("orderItems", orderItems);
        }};


        Response response = target("/users/" + USER_ID + "/orders").request().post(Entity.entity(createdOrder, MediaType.APPLICATION_JSON));
        Map vair = new HashMap<String,String>();
        verify(userRepository).placeOrder(userArgumentCaptor.capture(),orderArgumentCaptor.capture());

        assertThat(response.getStatus(), is(201));

        assertThat(orderArgumentCaptor.getValue().getDeliveryAddress(),is("street one zhouxuan"));

    }

    @Test
    public void should_return_200_for_get_payment() throws Exception {
        when(userRepository.getUserById(any())).thenReturn(user);

        Response response = target("/users/"+USER_ID+"/orders/"+ORDER_ID+"/payment").request(MediaType.APPLICATION_XML_TYPE).get();

        assertThat(response.getStatus(),is(200));
    }
}
