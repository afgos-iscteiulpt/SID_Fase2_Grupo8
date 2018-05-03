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

	public MongoConnection(String username, String password) {
			MongoClient mongoClient = MongoClients.create();
			MongoDatabase db = mongoClient.getDatabase("databaseTest");
			MongoCollection<Document> collection = db.getCollection("TABELAWOW");
			collection.find().forEach(printBlock);
	}
	
	
	Block<Document> printBlock = new Block<Document>() {
	       @Override
	       public void apply(final Document document) {
	           System.out.println(document.toJson());
	       }
	};
}
