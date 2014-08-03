package domain;

import org.bson.types.ObjectId;

public class ProductBuilder {
    public static Product buildProduct(ObjectId id, String name){
        return new Product(id,name);
    }
}
