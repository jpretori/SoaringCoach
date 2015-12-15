package com.polymorph.soaringcoach.hello;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by russel on 15/12/10.
 */
@CrossOrigin(origins = "*")
@RestController
public class FileUploadController {

    ArrayList<GNSSPoint> gnssPointList;

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload( @RequestParam("file") MultipartFile file){

        gnssPointList = new ArrayList<>();

        if (!file.isEmpty()) {
            try {
                GNSSPoint gnssPoint;
                File convFile = new File(file.getOriginalFilename());
                convFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(file.getBytes());
                try (BufferedReader br = new BufferedReader(new FileReader(convFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        gnssPoint = GNSSPoint.createGNSSPoint("", line);
                        if(gnssPoint != null){
                            gnssPointList.add(gnssPoint);
                        }
                    }
                }

                FlightAnalyser flightAnalyser = new FlightAnalyser(gnssPointList);
             double totalDistance =   flightAnalyser.calcTotalDistance();

                return "Total Distance travelled = "+String.valueOf(totalDistance)+" metres";
            } catch (Exception e) {
                return "You failed to upload " + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload  because the file was empty.";
        }
    }
}
