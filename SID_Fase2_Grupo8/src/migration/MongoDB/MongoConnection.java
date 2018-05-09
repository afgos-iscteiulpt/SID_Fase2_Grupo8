package migration.MongoDB;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import migration.DataConverter;
import migration.DataStack;

public class MongoConnection extends Thread {
	private String dbName = "databaseTest";
	private String collectionName = "HumidadeTemperatura";
	private String username = "";
	private String password = "";
	private final int requestPeriodicity = 10000;

	@Override
	public void run() {
		MongoDatabase db = mongoConnect(username, password);
		try {
			while (true) {
				MongoCollection<Document> collection = db.getCollection(collectionName);
				collection.find().forEach(printBlock);
				sleep(requestPeriodicity);
				saveAndDeleteSucessfullData(collection);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void saveAndDeleteSucessfullData(MongoCollection<Document> collection) {
		
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
			if (temp!=null)
				DataStack.push(temp);
		}
	};

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}
