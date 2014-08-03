package domain;

import org.bson.types.ObjectId;

public class PaymentBuilder {
    public static Payment buildPayment(ObjectId id, double amount){
        return new Payment(id,amount);
    }
}
