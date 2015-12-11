package com.polymorph.soaringcoach.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.polymorph.soaringcoach.analysis.GNSSPoint;

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
	
	public void saveIgcBRecord(ArrayList<GNSSPoint> bo_list) throws Exception {
		Statement stmt = null;

		try {
			conn.setAutoCommit(false); //Make committing the full ArrayList an atomic operation
			
			for (GNSSPoint gnssPoint : bo_list) {
			    stmt = conn.createStatement();
			    String sql = 
			    		"INSERT INTO `igc_b_record` " + 
			    		"(`file_id`, `timestamp`, `latitude`, " + 
			    		"`longitude`, `altitude_ok`, `pressure_altitude`, `gnss_altitude`, `other`) " + 
			    		"VALUES ('" + 
			    		gnssPoint.getFilename() + "', '" +
			    		gnssPoint.getTimestamp() + "', " +
			    		gnssPoint.getLatitude() + ", " +
			    		gnssPoint.getLongitude() + ", '" +
			    		gnssPoint.getAltitudeOK() + "', " +
			    		gnssPoint.getPressureAltitude() + ", " +
			    		gnssPoint.getGnssAltitude() + ", '" +
			    		gnssPoint.getOther() + "';";
			    
				stmt.executeUpdate(sql);	
			}

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.err.println("SQLException: " + ex.getMessage());
		    System.err.println("SQLState: " + ex.getSQLState());
		    System.err.println("VendorError: " + ex.getErrorCode());
		    conn.rollback();
		    throw new Exception("Could not save file to database", ex);
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		    conn.commit();
		}
	}
	
	public ArrayList<GNSSPoint> getIgcBRecords(String fileID) {
		return null;
	}
}
