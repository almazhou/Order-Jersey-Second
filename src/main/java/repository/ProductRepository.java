package repository;

import domain.Product;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProductRepository {
    List<Product> getProducts();

    Product getProductById(ObjectId id);
}
