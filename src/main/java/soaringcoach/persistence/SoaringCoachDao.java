package soaringcoach.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import soaringcoach.analysis.GNSSPoint;

public class SoaringCoachDao {
	private Connection conn = null;
	
	public SoaringCoachDao(Connection connection) {
		this.conn = connection;
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
			    		gnssPoint.getOther() + "');";
			    
				stmt.executeUpdate(sql);	
			}
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
	
	public ArrayList<GNSSPoint> getIgcBRecords(String filename) throws Exception {
		PreparedStatement stmt = null;
		ArrayList<GNSSPoint> db_points = new ArrayList<GNSSPoint>(); 
		
		ResultSet rs = null;
		try {
			conn.setAutoCommit(true);
			
		    stmt = conn.prepareStatement(
		    		"select `timestamp`, `latitude`, `longitude`, `altitude_ok`, " + 
		    		"`pressure_altitude`, `gnss_altitude`, `other` from igc_b_record " +
		    		"where file_id = ?");
		    int param = 1;
			
		    stmt.setString(param++, filename);
			
			stmt.executeQuery();
			
			rs  = stmt.getResultSet();
			
			while (rs.next()) {
				int col = 1;
				
				Date timestamp = rs.getTime(col++);
				double latitude = rs.getDouble(col++);
				double longitude = rs.getDouble(col++);
				String altitude_ok = rs.getString(col++);
				int pressure_altitude = rs.getInt(col++);
				int gnss_altitude = rs.getInt(col++);
				String other = rs.getString(col++);
				
				GNSSPoint pt = GNSSPoint.createGNSSPoint(
						filename, 
						timestamp,
						latitude,
						longitude,
						altitude_ok,
						pressure_altitude,
						gnss_altitude,
						other);
				
				db_points.add(pt);
			}
		}
		catch (SQLException ex){
		    // handle any errors
		    System.err.println("SQLException: " + ex.getMessage());
		    System.err.println("SQLState: " + ex.getSQLState());
		    System.err.println("VendorError: " + ex.getErrorCode());
		    throw new Exception("Could not load file from database", ex);
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed
		    
	        if (rs != null) {
	        	try {
	        		rs.close();
	        	} catch (SQLException sqlEx) { } //ignore
	        	
	        	rs = null;
	        }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
		
		return db_points;
	}
}
