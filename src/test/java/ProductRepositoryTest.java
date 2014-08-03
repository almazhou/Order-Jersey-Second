import db.MongoDb;
import domain.Product;
import mapper.ProductMapper;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ProductRepositoryTest {

    private Datastore dataStore;
    private ProductMapper productMapper;

    @Before
    public void setUp() throws Exception {
        dataStore = MongoDb.createDataStore("test");
        productMapper = new ProductMapper(dataStore);
    }

    @Test
    public void test_get_save_product() throws Exception {
        Product product = new Product("test",78.0);
        productMapper.saveProduct(product);

        ObjectId id = product.getId();
        assertNotNull(id);
    }

    @Test
    public void test_get_all_products(){
        Product product = new Product("test",89.2);
        productMapper.saveProduct(product);
        List<Product> products = productMapper.getProducts();
        assertThat(products.get(0).getId(),is(product.getId()));
    }
    @After
    public void tearDown() throws Exception {
        dataStore.getCollection(Product.class).drop();
    }
}
