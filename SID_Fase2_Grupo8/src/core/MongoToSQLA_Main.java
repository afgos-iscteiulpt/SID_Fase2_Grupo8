package core;

import migration.DataConfig;
import migration.MongoDB.MongoConnection;
import migration.SQLA.AnywhereDaemon;

public class MongoToSQLA_Main {
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
