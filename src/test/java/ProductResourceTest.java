import domain.Product;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductResourceTest extends JerseyTest {
    @Mock
    ProductRepository productRepository;
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
                .register(App.createMoxyJsonResolver());

    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }

    @Test
    public void should_return_200_for_get_one_product() throws Exception {
        when(productRepository.getProducts()).thenReturn(Arrays.asList(new Product("test")));
        Response response = target("/products").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertThat(response.getStatus(),is(200));

        List list = response.readEntity(List.class);

        Map product = (Map) list.get(0);

        assertThat(product.get("name"),is("test"));

    }

    @Test
    public void should_return_200_for_get_all_products_by_xml() throws Exception {
        when(productRepository.getProducts()).thenReturn(Arrays.asList(new Product("test")));
        Response response = target("/products").request(MediaType.APPLICATION_XML_TYPE).get();

        assertThat(response.getStatus(),is(200));

        String xmlResponse = response.readEntity(String.class);

        assertThat(xmlResponse.contains("test"),is(true));

    }
}
