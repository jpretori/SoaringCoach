package com.polymorph.soaringcoach.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

	private DaoFactory() {
		// make this non-instantiable
	}
	
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            conn  = DriverManager.getConnection(
            		"jdbc:mysql://localhost:3306/soaringcoach?" + 
            		"user=root&password=root_pass");
            
        } catch (SQLException ex) {
        	System.err.println("SQLException: " + ex.getMessage());
        	System.err.println("SQLState: " + ex.getSQLState());
        	System.err.println("VendorError: " + ex.getErrorCode());
        	throw new Exception("Could not connect to database", ex);
        }
		
		return conn;
	}

}
