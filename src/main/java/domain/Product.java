package domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("products")
public class Product {
    private String name;
    @Id
    private ObjectId id;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    protected Product(ObjectId id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ObjectId getId() {
        return id;
    }
}
