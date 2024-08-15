package server.Database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseController {

// ************* PSN APIs  ***********************************
//    public static void insertPSNReports(String title, String jsonReport, String htmlReport ) {
//        // Insert data
//        try {
//            MongoCollection<Document> collection = DB.conn.getCollection("PSNReports");
//            // Inserts a sample document describing a movie into the collection
//            InsertOneResult result = collection.insertOne(new Document()
//                    .append("_id", new ObjectId())
//                    .append("userId", new Random().nextInt(1000))
//                    .append("TestTitle", title)
//                    .append("Created", new Date())
//                    .append("jsonReport", jsonReport)
//                    .append("htmlReport", htmlReport));
//            // Prints the ID of the inserted document
//            System.out.println("Success! Inserted document id: " + result.getInsertedId());
//            DB.closeDBConnection();
//            // Prints a message if any exceptions occur during the operation
//        } catch (MongoException me) {
//            System.err.println("Unable to insert due to an error: " + me);
//        }
//    }
//
//    public static String getPSNHTMLReports() {
//        String report = "";
//        // Get data
//        try {
//            MongoCollection<Document> collection = DB.getCollection("PSNReports");
//            // Prints the number of documents in the collection
//            System.out.println("Number of documents in the collection: " + collection.countDocuments());
//            // Prints the title of the first document in the collection
//            System.out.println("First document in the collection: " + collection.find().first().get("htmlReport"));
//            report = Objects.requireNonNull(collection.find().first()).get("htmlReport").toString();
//            DB.closeDBConnection();
//            // Prints a message if any exceptions occur during the operation
//        } catch (MongoException me) {
//            System.err.println("Unable to get data due to an error: " + me);
//        }
//        return report;
//    }
//
//    public static String getPSNJSONReports() {
//        String report = "";
//        // Get data
//        try {
//            MongoCollection<Document> collection = DB.getCollection("PSNReports");
//            // Prints the number of documents in the collection
//            System.out.println("Number of documents in the collection: " + collection.countDocuments());
//            // Prints the title of the first document in the collection
//            System.out.println("First document in the collection: " + collection.find().first().get("jsonReport"));
//            report = collection.find().first().get("jsonReport").toString();
//            DB.closeDBConnection();
//            // Prints a message if any exceptions occur during the operation
//        } catch (MongoException me) {
//            System.err.println("Unable to get data due to an error: " + me);
//        }
//        return report;
//    }
//
////    ************* Perfomance APIs  ***********************************
//    public static void insertNewTestPlan(String userId, String title,String comments ) {
//        // Insert data
//        try {
//            MongoCollection<Document> collection = DB.getCollection("PerformanceTestPlans");
//            // Inserts a sample document describing a movie into the collection
//            InsertOneResult result = collection.insertOne(new Document()
//                    .append("_id", new ObjectId())
//                    .append("userId", userId)
//                    .append("title", title)
//                    .append("Comments", comments)
//                    .append("Created", new Date()));
//            // Prints the ID of the inserted document
//            System.out.println("Success! Inserted document id: " + result.getInsertedId());
//            DB.closeDBConnection();
//            // Prints a message if any exceptions occur during the operation
//        } catch (MongoException me) {
//            System.err.println("Unable to insert due to an error: " + me);
//        }
//    }
//
//    public static void insertNewThreadGroups(String userId, String testPlanId, String comments, String title, String threads, String rampUp, String holdTime ) {
//    // Insert data
//    try {
//        MongoCollection<Document> collection = DB.getCollection("PerformanceThreadGroups");
//        // Inserts a sample document describing a movie into the collection
//        InsertOneResult result = collection.insertOne(new Document()
//                .append("_id", new ObjectId())
//                .append("userId", userId)
//                .append("testPlanId", testPlanId)
//                .append("title", title)
//                .append("Comments", comments)
//                .append("Created", new Date())
//                .append("threads", threads)
//                .append("rampUp", rampUp)
//                .append("holdTime", holdTime)
//        );
//        // Prints the ID of the inserted document
//        System.out.println("Success! Inserted document id: " + result.getInsertedId());
//        DB.closeDBConnection();
//        // Prints a message if any exceptions occur during the operation
//    } catch (MongoException me) {
//        System.err.println("Unable to insert due to an error: " + me);
//    }
//}
//
//    public static void insertNewSampler(String testPlanId,  String name,String comments, String protocol, String server, String type, String port, String body ) {
//        // Insert data
//        try {
//            MongoCollection<Document> collection = DB.getCollection("PerformanceSamplers");
//            // Inserts a sample document describing a movie into the collection
//            InsertOneResult result = collection.insertOne(new Document()
//                    .append("_id", new ObjectId())
//                    .append("testPlanId", testPlanId)
//                    .append("name", name)
//                    .append("comments", comments)
//                    .append("protocol", protocol)
//                    .append("server", server)
//                    .append("sampleType", type)
//                    .append("port", port)
//                    .append("body", body));
//            // Prints the ID of the inserted document
//            System.out.println("Success! Inserted document id: " + result.getInsertedId());
//            DB.closeDBConnection();
//            // Prints a message if any exceptions occur during the operation
//        } catch (MongoException me) {
//            System.err.println("Unable to insert due to an error: " + me);
//        }
//    }




}
