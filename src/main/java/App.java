import domain.Product;
import org.bson.types.ObjectId;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import repository.ProductRepository;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    private static URI getBaseURI() {
        return URI.create("http://localhost:9998/");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer httpServer = startServer();

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                httpServer.shutdownNow();
            }
        }
    }

    private static HttpServer startServer() throws UnknownHostException {
        ProductRepository productRepository = new ProductRepository() {
            @Override
            public List<Product> getProducts() {
                return Arrays.asList(new Product("test"));
            }

            @Override
            public Product getProductById(ObjectId id) {
                return new Product("test");
            }
        };

        final ResourceConfig config = new ResourceConfig()
                .packages("resource")
                .register(App.createMoxyJsonResolver())
                .register(new MoxyXmlFeature())
                .register(JacksonFeature.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(productRepository).to(ProductRepository.class);
                    }
                });
        return GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), config);
    }

    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}