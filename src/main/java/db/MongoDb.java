package db;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.UnknownHostException;

public class MongoDb {
    public static Datastore createDataStore(String databaseName){
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
            final Datastore datastore = new Morphia().createDatastore(mongoClient, databaseName);
            return datastore;
        } catch (UnknownHostException e) {
            return null;
        }

    }


}
