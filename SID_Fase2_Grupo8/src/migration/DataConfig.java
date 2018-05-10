package migration;

import java.util.prefs.Preferences;

public class DataConfig {
	
	//MongoDB Config
	private final String dbName = "databaseTest";
	private final String sensorCollectionName = "HumidadeTemperatura";
	private final String migratedCollectionName = "HumidadeTemperaturaMigracao";
	private final String username = "";
	private final String password = "";
	private final int requestPeriodicity = 10000;
	
	//Sybase Config
	private static final String DATABASE_NAME = "test_database";
	private static final String USERNAME = "dba";
	private static final String PASSWORD = "sql";
	private static final String dbUrl="jdbc:sqlanywhere:Tds:localhost:2638?eng=";
	
	//DataStack Config
	private static final String table = "HumidadeTemperatura";
	
	public DataConfig() {
		 Preferences prefs = Preferences.userNodeForPackage(DataConfig.class);
	}

}
