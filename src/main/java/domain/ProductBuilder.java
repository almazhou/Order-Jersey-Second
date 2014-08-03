package domain;

import org.bson.types.ObjectId;

public class ProductBuilder {
    public static Product buildProduct(ObjectId id, String name, double price){
        return new Product(id,name, price);
    }
}
