package tests;

import migration.DataConfig;
import migration.MongoDB.MongoConnection;
import migration.SQLA.AnywhereDaemon;

public class TestMongoToSQLA {
	public static void main(String[] args) {
		new DataConfig().readProperties();
		new MongoConnection().start();
		new Thread() {
			public void run() {
				AnywhereDaemon.main(null);
			}
		}.start();

		
	}
}
