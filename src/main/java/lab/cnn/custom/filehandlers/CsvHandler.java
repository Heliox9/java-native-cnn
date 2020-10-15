package lab.cnn.custom.filehandlers;

import lab.controlBase.commandlineInterface.LoggingBase;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * handler for mapping images in a folder via csv
 */
public class CsvHandler implements Serializable {

    /**
     * reads file and returns content
     *
     * @param file the File to read (has to exist and be .csv)
     * @return csv content as map
     */
    public static Map<String, Integer> handle(File file) {
        Map<String, Integer> mapping = new HashMap<>();

        // check file for existence
        if (file.exists() && file.getAbsolutePath().endsWith(".csv")) {
            String row;

            try {
                // build reader on file
                BufferedReader csvReader = new BufferedReader(new FileReader(file));

                // read line by line
                while ((row = csvReader.readLine()) != null) {
                    // seperate lines by ,
                    String[] data = row.split(",");
                    // add data into map
                    mapping.put(data[0], Integer.parseInt(data[1]));
                }
                // close open reader
                csvReader.close();
            } catch (IOException e) {
                LoggingBase.log.error("Unexpected file read error", e);
            }
        } else {
            LoggingBase.log.error("Can't read file at " + file.getAbsolutePath());
        }

        return mapping;
    }


}
