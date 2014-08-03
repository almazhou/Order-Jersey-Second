package mapper;

import domain.Product;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import repository.ProductRepository;

import java.util.List;

public class ProductMapper implements ProductRepository {
    private Datastore dataStore;

    public ProductMapper(Datastore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void saveProduct(Product product) {
        dataStore.save(product);
    }

    @Override
    public List<Product> getProducts() {
        return dataStore.find(Product.class).asList();
    }

    @Override
    public Product getProductById(ObjectId id) {
        return dataStore.get(Product.class,id);
    }

}
