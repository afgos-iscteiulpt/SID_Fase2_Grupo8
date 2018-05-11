package migration.MongoDB;

import migration.DataConfig;

public class MongoConnectionTester {

	public static void main(String[] args) {
		new DataConfig().readProperties();
		new MongoConnection().start();
	}

}
