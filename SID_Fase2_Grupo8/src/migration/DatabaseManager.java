package migration;

import java.sql.*;

public class DatabaseManager {
	static Connection conn;

	public DatabaseManager(String db, String user, String passwd, Object openserver, String databasepath) {
		String dbUrl;
		dbUrl = "jdbc:sqlanywhere:Tds:localhost:2638?eng=" + db;
		try {
			conn = DriverManager.getConnection(dbUrl, user, passwd);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println("Server down, unable to make the connection. ");
			Boolean openServer = (Boolean) openserver;
			if (openServer.booleanValue())
				try {
					String engcommand = "dbeng12 " + databasepath + db + ".db";
					Runtime.getRuntime().exec(engcommand);
					long t0, t1;
					t0 = System.currentTimeMillis();
					do {
						t1 = System.currentTimeMillis();
					} while (t1 - t0 < 5000);
					conn = DriverManager.getConnection(dbUrl, user, passwd);
					conn.setAutoCommit(false);
				} catch (Exception ex) {
					System.out.println("Unable to start server. " + ex);
				}
		}
	}

	public void SelectStatement(String sql, ConnectionHandler ch) {
		ch.r = null;
		try {
			ch.s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ch.r = ch.s.executeQuery(sql);
		} catch (Exception e) {
			ch.returnStatus = e.toString();
			System.out.println("Unable to execute the SelectStatement. " + e);
		}
	}

	public void updateStatement(String sql, ConnectionHandler ch) {
		Integer result = new Integer(0);
		String message = null;
		if (sql.trim().substring(0, 1).equalsIgnoreCase("U"))
			message = "Number of rows updated: ";
		else if (sql.trim().substring(0, 1).equalsIgnoreCase("I"))
			message = "Number of rows inserted: ";
		else
			message = " ";
		try {
			ch.s = conn.createStatement();
			result = new Integer(ch.s.executeUpdate(sql));
			ch.returnStatus = message + result;
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
