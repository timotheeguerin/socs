package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLConnection {
	// bendodev.no-ip.org:3306/comp421project?" + "user=root&password=madremia350");
	private final String MYSQL_DRIVER_CLASS = "org.gjt.mm.mysql.Driver";

	public Connection db;

	public SQLConnection(String url, String dbName, String user, String password) {
		try {
			Class.forName(MYSQL_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			System.out.println("Class: " + MYSQL_DRIVER_CLASS + " not found.");
			e.printStackTrace();
		}

		try {
			db = DriverManager.getConnection("jdbc:mysql://" + url + "/"
					+ dbName + "?" + "user=" + user + "&password=" + password);
		} catch (SQLException e) {
			System.out.println("Couldn't connect to " + url + ".");
			e.printStackTrace();
		}
	}

	public void CloseConnection() {
		try {
			db.close();
		} catch (SQLException e) {
			System.out.println("Couldnt close DB Connection");
			e.printStackTrace();
		}
	}

	public List<HashMap<String, String>> ExecuteQuery(String query, String... args) {
		try {
			PreparedStatement st = db.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				st.setString(i + 1, args[i]);
			}
			ResultSet rs = st.executeQuery();
			List<HashMap<String, String>> a =  resultSetToHashList(rs);
			st.close();
			return a;
		} catch (SQLException e) {
			System.out.println("Couldn't execute query");
			e.printStackTrace();
			return null;
		}
	}
	public int ExecuteUpdate(String query, String... args) {
		try {
			PreparedStatement st = db.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				st.setString(i + 1, args[i]);
			}
			int a = st.executeUpdate();
			st.close();
			return a;
		} catch (SQLException e) {
			System.out.println("Couldn't execute query");
			e.printStackTrace();
			return -1;
		}
	}
	private List<HashMap<String, String>> resultSetToHashList(ResultSet rs) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String colName = rsmd.getColumnName(i+1);
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
