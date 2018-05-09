package tests;

import migration.DataStack;
import migration.MongoDB.MongoConnection;
import migration.SQLA.AnywhereDaemon;

public class TestMongoToSQLA {
	public static void main(String[] args) {
		new MongoConnection().start();
		new Thread() {
			public void run() {
				AnywhereDaemon.main(null);
			}
		}.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] thing = {"12-02-1997", "16:00:00", "69", "69"};
		DataStack.pushToSQLA(thing);
		
	}
}
