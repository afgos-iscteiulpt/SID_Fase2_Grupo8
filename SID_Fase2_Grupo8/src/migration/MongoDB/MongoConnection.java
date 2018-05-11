package migration.MongoDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import static migration.DataConfig.*;
import migration.DataConverter;
import migration.DataStack;

public class MongoConnection extends Thread {

	private MongoDatabase db;

	@Override
	public void run() {
		db = mongoConnect();
		new MongoSuccessfulThread().start();
		try {
			while (true) {
				MongoCollection<Document> collection = db.getCollection(SENSOR_COLLECTION_NAME);
				collection.find().forEach(printBlock);
				sleep(REQUEST_PERIODICITY);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public MongoDatabase mongoConnect() {
		MongoCredential credential = MongoCredential.createCredential(MONGO_USERNAME, "admin",
				MONGO_PASSWORD.toCharArray());
		MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
				.applyToSslSettings(builder -> builder.enabled(false))
				.applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		return mongoClient.getDatabase(MONGO_DBNAME);

	}
	
	

	Block<Document> printBlock = new Block<Document>() {

		@Override
		public void apply(final Document document) {
			System.out.println(document);
			String[] temp = DataConverter.convertJsonToStringArray(document.toJson());
			if (temp != null)
				DataStack.pushToSQLA(temp);
			else {
				MongoCollection<Document> collection = db.getCollection(SENSOR_COLLECTION_NAME);
				JSONObject obj = new JSONObject(document.toJson());
				collection.deleteOne(eq("_id", new ObjectId(obj.getJSONObject("_id").getString("$oid"))));
			}
		}
	};

	private class MongoSuccessfulThread extends Thread {

		@Override
		public void run() {
			while (true) {
				MongoCollection<Document> collection = db.getCollection(SENSOR_COLLECTION_NAME);
				MongoCollection<Document> migratedCollection = db.getCollection(MIGRATED_COLLECTION_NAME);
				saveAndDeleteSucessfulData(collection, migratedCollection);
			}
		}

		public void saveAndDeleteSucessfulData(MongoCollection<Document> collection,
				MongoCollection<Document> migratedCollection) {
			ArrayList<String[]> list = DataStack.popAllFromSQLAToMongo();
			List<Document> documents = new ArrayList<Document>();
			if (list != null) {
				for (String[] data : list) {
					System.out.println("Adding " + data[4]);
					documents.add(new Document().append("date", data[0]).append("time", data[1])
							.append("temperatura", data[2]).append("humidade", data[3]));
				}
				migratedCollection.insertMany(documents);
				for (String[] data : list) {
					collection.deleteOne(eq("_id", new ObjectId(data[4])));
				}
			}

		}

	}
}
