package server.Database;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class DB {

    private static MongoClient conn;
    private String connectionString;
    private static Logger logger;
    JSONParser parser;
    public DB() {
        //NRvj7LkmRjRR9le6  - nsngoveni079
        connectionString = "mongodb+srv://nsngoveni079:NRvj7LkmRjRR9le6@speed.2cqgs.mongodb.net/?retryWrites=true&w=majority&appName=speed";
        logger = org.slf4j.LoggerFactory.getLogger(DB.class);
        parser = new JSONParser();
        try {
            if (conn == null) {
                conn = MongoClients.create(connectionString);
            }
        } catch (Exception e) {
            logger.debug("Unable to connect to the database due to an error: " + e);
        }
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        MongoDatabase database = conn.getDatabase("speed");
        return database.getCollection(collectionName);
    }

    // Perfomance queries
    public String createTestPlan(String userId, String title, String description, String type) {
        MongoCollection<Document> collection = getCollection("testPlans");
        Document document = new Document("name", title)
                .append("description", description)
                .append("type", type)
                .append("userId", userId);
        InsertOneResult result = collection.insertOne(document);
        System.out.println("Success! Inserted TestPlan id: " + result.getInsertedId());
        return Objects.requireNonNull(result.getInsertedId()).toString();
    }

    public void createThreadGroup(String testPlanId,String name,  String desciption, int threads, int rampUp, int holdTime) {
        MongoCollection<Document> collection = getCollection("threadGroups");
        Document document = new Document("name", name)
                .append("testPlanId", testPlanId)
                .append("description", desciption)
                .append("threads", threads)
                .append("rampUp", rampUp)
                .append("holdTime", holdTime);
        InsertOneResult result  = collection.insertOne(document);
        System.out.println("Success! Inserted threadGroup id: " + result.getInsertedId());
    }

    public void createSampler(String testPlanId,String title, String  comment,  String domain, String path, String method) {
        MongoCollection<Document> collection = getCollection("samplers");
        Document document = new Document("testPlanId", testPlanId)
                .append("title", title)
                .append("comment", comment)
                .append("domain", domain)
                .append("path", path)
                .append("method", method);
        InsertOneResult result = collection.insertOne(document);
        System.out.println("Success! Inserted sampler id: " + result.getInsertedId());
    }

    public void storeReport(JSONArray data, String testPlanId, String DBName) {
        MongoCollection<Document> collection = getCollection(DBName);

        // Get today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (int i = 0; i < data.size(); i++) {
            Document document = Document.parse(data.get(i).toString())
                    .append("testPlanId", testPlanId)
                    .append("date", today.format(formatter));
            collection.insertOne(document);
        }
    }

    //Will add user ID argument in future to protect data
    public JSONObject getTestPlan(String testPlanId) {
        try {
            //get test plan
            MongoCollection<Document> testPlanCollection = getCollection("testPlans");
            ObjectId objectId = new ObjectId(testPlanId);
            // Find the document by ID
            Document document = testPlanCollection.find(eq("_id", objectId)).first();
            System.out.println(document.toJson());
            return (JSONObject) parser.parse(document.toJson());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    public JSONObject getThreadGroup(String testPlanId) {
        //get thread groups
        MongoCollection<Document> threadGroupsCollection = getCollection("threadGroups");
        Document document = threadGroupsCollection.find(eq("testPlanId", testPlanId)).first();
        try{
            System.out.println(document.toJson());
            return (JSONObject) parser.parse(document.toJson());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<JSONObject> getSamplers(String testPlanId) {
        //get samplers
        MongoCollection<Document> samplersCollection = getCollection("samplers");
        List<JSONObject> samplers = new ArrayList<>();
        try (MongoCursor<Document> cursor = samplersCollection.find(eq("testPlanId", testPlanId)).iterator()) {
            while (cursor.hasNext()) {
                Document res = cursor.next();
                System.out.println(res.toJson());
                JSONObject doc = (JSONObject) parser.parse(res.toJson());
                samplers.add(doc);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return samplers;
    }

    public JSONObject getPerformanceSummary(String testPlanId) {
        //get run summary
        MongoCollection<Document> runSummaryCollection = getCollection("performanceSummary");
        Document document = runSummaryCollection.find(eq("testPlanId", testPlanId)).first();
        try {
            System.out.println(document.toJson());
            return (JSONObject) parser.parse(document.toJson());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getReports(String testPlanId, String DBName) {
        //get reports
        MongoCollection<Document> reportsCollection = getCollection(DBName);
        JSONArray reports = new JSONArray();
        try (MongoCursor<Document> cursor = reportsCollection.find(eq("testPlanId", testPlanId)).iterator()) {
            while (cursor.hasNext()) {
                Document res = cursor.next();
                System.out.println(res.toJson());
                JSONObject doc = (JSONObject) parser.parse(res.toJson());
                reports.add(doc);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }
}