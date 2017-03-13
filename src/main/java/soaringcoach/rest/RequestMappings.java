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

package soaringcoach.rest;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyser;
import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.PolarVector;

@CrossOrigin
@RestController
public class RequestMappings {

	@CrossOrigin
    @RequestMapping(name="/upload", method=RequestMethod.POST)
    public @ResponseBody Flight handleFileUpload(@RequestParam(name="file") MultipartFile file) throws AnalysisException, IOException {
    	FlightAnalyser fa = new FlightAnalyser();
    	
		Flight f = fa.addAndAnalyseFlight(file.getInputStream());
    	
		f.igc_points = new ArrayList<>();
		
        return f;
    }
    
	@CrossOrigin
    @RequestMapping(name="/health", method=RequestMethod.GET)
    public PolarVector handleHealthCheck(@RequestParam(name="echo", defaultValue = "42") long echo) {
    	return new PolarVector(echo, echo);
    }
}
