package domain;

import org.bson.types.ObjectId;

public class Payment {
    private ObjectId id;
    private double amount;

    public Payment(double amount) {
        this.amount = amount;
    }

    public Payment(ObjectId id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public ObjectId getId() {
        return id;
    }
}
