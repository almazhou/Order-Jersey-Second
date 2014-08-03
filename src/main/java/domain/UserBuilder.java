package domain;

import org.bson.types.ObjectId;

public class UserBuilder {
    public static User BuildUser(ObjectId id){
        return new User(id);
    }
}
