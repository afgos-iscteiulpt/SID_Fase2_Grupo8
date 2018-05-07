package migration.MongoDB;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import migration.DataConverter;
import migration.DataStack;

public class MongoConnection {
	private String dbName = "databaseTest";
	private String collectionName = "HumidadeTemperatura";

	public MongoConnection(String username, String password) {
			MongoClient mongoClient = MongoClients.create();
			MongoDatabase db = mongoClient.getDatabase(dbName);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.find().forEach(printBlock);
	}
	
	
	Block<Document> printBlock = new Block<Document>() {
	       @Override
	       public void apply(final Document document) {
	           DataStack.push(DataConverter.convertJsonToStringArray(document.toJson()));
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
