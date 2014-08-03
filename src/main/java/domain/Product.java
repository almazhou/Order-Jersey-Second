package domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("products")
public class Product {
    private String name;
    private double price;
    @Id
    private ObjectId id;

    public Product() {
    }

    public Product(String name,double price) {
        this.name = name;
        this.price = price;
    }

    protected Product(ObjectId id, String name,double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ObjectId getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}
