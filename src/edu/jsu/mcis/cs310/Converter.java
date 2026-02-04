package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.*;
import java.util.*;


public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        // Read info for use
        try {
        CSVReader reader = new CSVReader(new java.io.StringReader(csvString));
        java.util.List<String[]> rows = reader.readAll();
        reader.close();

        // Put headings in array
        JsonArray colHeadings = new JsonArray();
        for (String heading : rows.get(0)) {
            colHeadings.add(heading);
        }

        // Set up other arrays for info
        JsonArray prodNums = new JsonArray();
        JsonArray data = new JsonArray();

       // Populate arrays with info
        for (int i = 1; i < rows.size(); i++) {

            String[] row = rows.get(i);

            
            prodNums.add(row[0]);

            
            JsonArray instance = new JsonArray();

            instance.add(row[1]);                 
            instance.add(Integer.parseInt(row[2])); 
            instance.add(Integer.parseInt(row[3])); 
            instance.add(row[4]);                  
            instance.add(row[5]);                  
            instance.add(row[6]);                  
            data.add(instance);
        }

        /* Build JSON object */
        JsonObject object = new JsonObject();
        object.put("ProdNums", prodNums);
        object.put("ColHeadings", colHeadings);
        object.put("Data", data);

        result = Jsoner.serialize(object);
        
      
        
        
        
        
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
        JsonObject jsonObject = Jsoner.deserialize(jsonString, new JsonObject());
        
        JsonArray colHeadings = (JsonArray)jsonObject.get("ColHeadings");
        JsonArray prodNums = (JsonArray) jsonObject.get("ProdNums");
        JsonArray data = (JsonArray) jsonObject.get("Data");
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
        
        String[] header = new String[colHeadings.size()];
        for (int i = 0; i < colHeadings.size(); i++) {
            header[i] = colHeadings.get(i).toString();
        }
        csvWriter.writeNext(header);

        for (int i = 0; i < data.size(); i++) {

            JsonArray instance = (JsonArray) data.get(i);
            String[] row = new String[colHeadings.size()];

            row[0] = prodNums.get(i).toString();
            row[1] = instance.get(0).toString();
            row[2] = instance.get(1).toString();

            int episode = ((Number) instance.get(2)).intValue();
            row[3] = String.format("%02d", episode);

            row[4] = instance.get(3).toString();
            row[5] = instance.get(4).toString();
            row[6] = instance.get(5).toString();

            csvWriter.writeNext(row);
        }

        csvWriter.close();
        result = writer.toString();
        
        
        
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
