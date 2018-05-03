package migration;

import java.sql.*;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;

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
	           System.out.println(document.toJson());
	           DataConverter.convertJsonToString(document.toJson());
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
