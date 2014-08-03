import domain.Product;
import domain.ProductBuilder;
import exception.RecordNotFoundException;
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
import repository.ProductRepository;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductResourceTest extends JerseyTest {
    public static final String PRODUCT_ID = "1234567890abcdef12345678";
    @Mock
    ProductRepository productRepository;
    private ObjectId id;
    private Product product;

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .packages("resource")
                .register(new MoxyXmlFeature())
                .register(new MoxyJsonFeature())
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
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

    @Before
    public void setUp() throws Exception {
        super.setUp();
        id = new ObjectId(PRODUCT_ID);
        product = ProductBuilder.buildProduct(id, "test", 45.0);
    }

    @Test
    public void should_return_200_for_get_all_product_by_json() throws Exception {
        when(productRepository.getProducts()).thenReturn(Arrays.asList(product));
        Response response = target("/products").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertThat(response.getStatus(),is(200));

        List list = response.readEntity(List.class);

        Map product = (Map) list.get(0);

        assertThat(product.get("name"),is("test"));
        assertThat(product.get("price"),is(45.0));

    }

    @Test
    public void should_return_200_for_get_all_products_by_xml() throws Exception {
        when(productRepository.getProducts()).thenReturn(Arrays.asList(product));
        Response response = target("/products").request(MediaType.APPLICATION_XML_TYPE).get();

        assertThat(response.getStatus(),is(200));

        String xmlResponse = response.readEntity(String.class);

        assertThat(xmlResponse.contains("test"),is(true));

    }

    @Test
    public void should_return_200_for_get_one_product() throws Exception {
        when(productRepository.getProductById(eq(id))).thenReturn(product);

        String path = "/products/" + PRODUCT_ID;
        Response response = target(path).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(),is(200));

        Map given = response.readEntity(Map.class);

        assertThat(given.get("name"),is("test"));
        assertThat(given.get("id"),is(PRODUCT_ID));
        assertThat(((String)given.get("uri")).contains(path),is(true));

    }
   @Test
    public void should_return_404_for_get_one_product_not_exist() throws Exception {
        when(productRepository.getProductById(eq(id))).thenThrow(RecordNotFoundException.class);

        String path = "/products/" + PRODUCT_ID;
        Response response = target(path).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(),is(404));

    }
}
