package migration.MongoDB;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

import migration.DataConverter;
import migration.DataStack;

public class MongoConnection extends Thread {
	private String dbName = "databaseTest";
	private String sensorCollectionName = "HumidadeTemperatura";
	private String migratedCollectionName = "HumidadeTemperaturaMigracao";
	private String username = "";
	private String password = "";
	private final int requestPeriodicity = 10000;

	@Override
	public void run() {
		MongoDatabase db = mongoConnect(username, password);
		try {
			while (true) {
				MongoCollection<Document> collection = db.getCollection(sensorCollectionName);
				collection.find().forEach(printBlock);
				sleep(requestPeriodicity);
				MongoCollection<Document> migratedCollection = db.getCollection(migratedCollectionName);
				saveAndDeleteSucessfulData(collection, migratedCollection);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void saveAndDeleteSucessfulData(MongoCollection<Document> collection, MongoCollection<Document> migratedCollection) {
		ArrayList<String[]> list = DataStack.popAllFromSQLAToMongo();
		List<Document> documents = new ArrayList<Document>();
		if (list != null) {
			for (String[] data : list) {
				System.out.println("Adding " + data[4]);
				documents.add(new Document().append("date", data[0]).append("time", data[1]).append("temperatura", data[2]).append("humidade", data[3]));
			}
			migratedCollection.insertMany(documents);
			for (String[] data : list) {
				collection.deleteOne(eq("_id", new ObjectId(data[4])));
			}
		}
		
	}

	public MongoDatabase mongoConnect(String username, String password) {
		MongoClient mongoClient = MongoClients.create();
		return mongoClient.getDatabase(dbName);

	}

	Block<Document> printBlock = new Block<Document>() {

		@Override
		public void apply(final Document document) {
			System.out.println(document);
			String[] temp = DataConverter.convertJsonToStringArray(document.toJson());
			if (temp != null)
				DataStack.pushToSQLA(temp);
		}
	};

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCollectionName() {
		return sensorCollectionName;
	}

	public void setCollectionName(String collectionName) {
		this.sensorCollectionName = collectionName;
	}
}
