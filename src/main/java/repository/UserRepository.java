package repository;

import domain.User;
import org.bson.types.ObjectId;

public interface UserRepository {
    User getUserById(ObjectId id);
}
