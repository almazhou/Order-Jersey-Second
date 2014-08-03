package repository;

import domain.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getProducts();
}
