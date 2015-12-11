package com.polymorph.soaringcoach.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SoaringCoachDao {
	private Connection conn = null;
	
	public SoaringCoachDao() throws Exception {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            conn = DriverManager.getConnection(
            		"jdbc:mysql://localhost:3306/soaringcoach?" + 
            		"user=root&password=root_pass");
            
        } catch (SQLException ex) {
        	System.err.println("SQLException: " + ex.getMessage());
        	System.err.println("SQLState: " + ex.getSQLState());
        	System.err.println("VendorError: " + ex.getErrorCode());
        	throw new Exception("Problem connecting to database", ex);
        }
	}
	
	public void saveIgcBRecord(ArrayList<IgcBRecord> bo_list) {
	}
	
	public ArrayList<IgcBRecord> getIgcBRecords(String fileID) {
		return null;
	}
}
