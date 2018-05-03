package migration;

import java.sql.*;

public class DatabaseManager {
	static Connection conn;
	

	public DatabaseManager(String db, String user, String passwd) {
		String dbUrl;
		dbUrl = "jdbc:sqlanywhere:Tds:localhost:2638?eng=" + db;
		try {
			conn = DriverManager.getConnection(dbUrl, user, passwd);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println("Server down, unable to make the connection. ");
		}
	}
	
	//TODO
	public void insertStatement(String table, String fields, String values, ConnectionHandler ch) {
		String message = "Inserted: " + values + "; into: " + table;
		
		try {
			ch.s=conn.createStatement();
			ch.s.executeUpdate("INSERT INTO " + table + "(" + fields + ")" + "VALUES(" + values + ")");
			ch.returnStatus = message;
		} catch (Exception e) {
			System.out.println("Unable to execute the insert/update/delete statement. " + e);
			ch.returnStatus = "Error:" + e.toString();
		}
	}

	public void returnResultSetSelectStatement(String sql, int resultsetType, ConnectionHandler ch) {
		ch.r = null;
		try {
			if (resultsetType == 1)
				ch.s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			else
				ch.s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ch.r = ch.s.executeQuery(sql);
		} catch (Exception e) {
			ch.returnStatus = e.toString();
			System.out.println("Unable to execute the returnSelectStatement. " + e);
		}
	}

	public void setIsolationLevel(String i, ConnectionHandler ch) {
		try {
			ch.s.executeUpdate("Set Option Isolation_Level = " + i);
		} catch (Exception e) {
			System.out.println("Unable to change the Isolation Level" + e);
		}
	}

	public boolean closeDatabase() {
		try {
			conn.close();
			return (true);
		} catch (Exception e) {
			System.out.println("Unable to close the database. " + e);
			return (false);
		}
	}

	public boolean isCloseDatabase() {
		boolean result = true;
		try {
			if (conn.isClosed())
				result = true;
			else
				result = false;
		} catch (Exception e) {
			System.out.println("Unable to make the connection. " + e);
		}
		return (result);
	}

	public void setCommit(ConnectionHandler ch) {
		try {
			ch.s.executeUpdate("Commit");
		} catch (Exception e) {
			System.out.println("Unable to Commit the transaction" + e);
		}
	}

}
