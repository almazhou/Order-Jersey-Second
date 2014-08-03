package mapper;

import domain.Product;
import org.mongodb.morphia.Datastore;
import repository.ProductRepository;

import java.util.List;

public class ProductMapper implements ProductRepository {
    private Datastore dataStore;

    public ProductMapper(Datastore dataStore) {
        this.dataStore = dataStore;
    }

    public void save(Product product) {
        dataStore.save(product);
    }

    @Override
    public List<Product> getProducts() {
        return dataStore.find(Product.class).asList();
    }
}
