package server.Database;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import security.Helpers;

public class DatabaseConnection {

    public static MongoClient conn=null;
    public static MongoClient openDBConnection() {
        String connectionString = "mongodb+srv://lungeloshabangu101ot:860KWBysK5Xhyyj9@cluster0.lpw8agg.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        try {
            if (conn == null) {
                conn = MongoClients.create(connectionString);
            }
        } catch (Exception e) {
            System.out.println("Unable to connect to the database due to an error: " + e);
        }

        return conn;
    }
    public static MongoCollection<Document> getCollection(String collectionName) {
        MongoCollection<Document> collection = null;
        try {
            MongoDatabase database = openDBConnection().getDatabase("security");
            collection = database.getCollection(collectionName);
        } catch (Exception e) {
            System.out.println("Unable to connect to the database due to an error: " + e);
        }

        return collection;
    }

    public static void closeDBConnection() {
        openDBConnection().close();
    }
}