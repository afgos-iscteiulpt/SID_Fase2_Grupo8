package migration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class DataConfig {
	
	//MongoDB Config
	public static String MONGO_DBNAME;
	public static String MONGO_URI;
	public static String SENSOR_COLLECTION_NAME;
	public static String MIGRATED_COLLECTION_NAME;
	public static String MONGO_AUTH_DB;
	public static String MONGO_USERNAME;
	public static String MONGO_PASSWORD;
	public static int REQUEST_PERIODICITY;
	
	//Sybase Config
	public static String SQLA_DBNAME;
	public static String SQLA_USERNAME;
	public static String SQLA_PASSWORD;
	public static String SQLA_DBURL;
	public static String SQLA_TABLE;
	
	//misc config
	public static String BROKER_TOPIC;
	public static String BROKER_URL;
	
	public DataConfig() {
		//readProperties();
	}
	
	public void writeProperties() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("config.properties");

			// set the properties value
			prop.setProperty("Mongo_DBName", "databaseTest");
			prop.setProperty("Mongo_Sensor_Collection_Name", "HumidadeTemperatura");
			prop.setProperty("Mongo_Migrated_Collection_Name", "HumidadeTemperaturaMigrated");
			prop.setProperty("Mongo_Username", "SGD");
			prop.setProperty("Mongo_Password", "grupo8");
			prop.setProperty("Request_Periodicity", "10000");
			prop.setProperty("Mongo_URI", "localhost");
			prop.setProperty("Mongo_Auth_DB", "admin");
			
			prop.setProperty("SQLA_DBName", "test_database");
			prop.setProperty("SQLA_Username", "dba");
			prop.setProperty("SQLA_Password", "sql");
			prop.setProperty("SQLA_DB_URL", "jdbc:sqlanywhere:Tds:localhost:2638?eng=");
			prop.setProperty("SQLA_Write_Table", "HumidadeTemperatura");
			
			prop.setProperty("Broker_Topic", "/sid_lab_2018");
			prop.setProperty("Broker_URL", "iot.eclipse.org");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	  }
	
	public void readProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			MONGO_DBNAME = prop.getProperty("Mongo_DBName");
			SENSOR_COLLECTION_NAME = prop.getProperty("Mongo_Sensor_Collection_Name");
			MIGRATED_COLLECTION_NAME = prop.getProperty("Mongo_Migrated_Collection_Name");
			MONGO_USERNAME = prop.getProperty("Mongo_Username");
			MONGO_PASSWORD = prop.getProperty("Mongo_Password");
			REQUEST_PERIODICITY = Integer.parseInt(prop.getProperty("Request_Periodicity"));
			MONGO_URI = prop.getProperty("Mongo_URI");
			MONGO_AUTH_DB = prop.getProperty("Mongo_Auth_DB");
			
			SQLA_DBNAME = prop.getProperty("SQLA_DBName");
			SQLA_USERNAME = prop.getProperty("SQLA_Username");
			SQLA_PASSWORD = prop.getProperty("SQLA_Password");
			SQLA_DBURL= prop.getProperty("SQLA_DB_URL");
			SQLA_TABLE = prop.getProperty("SQLA_Write_Table");
			
			BROKER_TOPIC = prop.getProperty("Broker_Topic");
			BROKER_URL = prop.getProperty("Broker_URL");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
