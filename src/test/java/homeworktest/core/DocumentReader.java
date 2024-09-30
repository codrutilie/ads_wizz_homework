package homeworktest.core;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentReader {
    /**
     * Converts a text document to a list of JSON objects.
     *
     * @return a list containing JSON objects parsed from each line of the text file.
     */
    public static List<JSONObject> convertTxtDocToJsonList() {
        List<JSONObject> jsonList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(
                new FileReader("src/test/resources/downloads.txt"))) {
            JSONObject json;
            String line = reader.readLine();
            while (line != null) {
                json = new JSONObject(line);
                jsonList.add(json);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonList;
    }
}
