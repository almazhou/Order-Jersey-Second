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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.UserRepository;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderResourceTest extends JerseyTest{
    public static final String PRODUCT_ID = "1234567890abcdef12345678";
    public static final String ORDER_ID = "1234567890abcdef123456ef";
    private static final String  USER_ID = "1234567890abcdef123456ab";
    public static final String ORDER_NOT_EXIST = "123456789009876543212345";
    @Mock
    private UserRepository userRepository;
    private User user;
    private Order order;
    private ObjectId objectId;

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
        OrderItem orderItem = new OrderItem(PRODUCT_ID, 2);
        order = OrderBuilder.buildOrder(ORDER_ID, USER_ID, "street one", 560.0);
        order.addOrderItem(orderItem);
        user.placeOrder(order);
        objectId = new ObjectId(PRODUCT_ID);
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
}
