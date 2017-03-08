/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach.persistence;

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
