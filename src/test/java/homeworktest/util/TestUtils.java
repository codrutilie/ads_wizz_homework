package homeworktest.util;

import homeworktest.core.DocumentReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.*;

public class TestUtils {
    List<JSONObject> jsonList;
    MapUtils mapUtils;
    TimestampUtils timestampUtils;

    public TestUtils() {
        jsonList = DocumentReader.convertTxtDocToJsonList();
        mapUtils = new MapUtils();
        timestampUtils = new TimestampUtils();
    }

    /**
     * Counts and sorts occurrences of showIds filtered by city "San Francisco".
     *
     * @return a HashMap, sorted in descending order by value,
     * where the key is the showId and the value is the number of occurrences
     */
    public HashMap<String, Integer> sortedShowIdCounter() {
        HashMap<String, Integer> showIdCounter = new HashMap<>();
        HashMap<String, Integer> sortedShowIdCounter;
        Set<String> showIdSet = getListOfValuesByKeyFromDownloadIdentifierJson("showId");
        List<JSONObject> filteredJsonList = filterListByKeyValue(jsonList, "city", "San Francisco");

        for (String showId : showIdSet) {
            List<JSONObject> filterJsonListByShowIdValue = filterJsonListByShowIdValue(filteredJsonList, showId);
            showIdCounter.put(showId, filterJsonListByShowIdValue.size());
        }

        sortedShowIdCounter = mapUtils.sortByValue(showIdCounter);
        return sortedShowIdCounter;
    }

    public HashMap<String, Integer> sortedDeviceTypeCounter() {
        HashMap<String, Integer> deviceTypeCounter = new HashMap<>();
        HashMap<String, Integer> sortedDeviceTypeCounter;
        List<String> values = getListOfValuesByKeyFromMainJson("deviceType");

        for (String value : values) {
            List<JSONObject> valueFilteredList = filterListByKeyValue(jsonList, "deviceType", value);
            deviceTypeCounter.put(value, valueFilteredList.size());
        }

        sortedDeviceTypeCounter = mapUtils.sortByValue(deviceTypeCounter);
        return sortedDeviceTypeCounter;
    }

    /**
     * Counts and sorts "preroll" occurrences for each showId.
     *
     * @return a HashMap, sorted in descending order by value,
     * where the key is the showId and the value is the number prerolls occurrences
     */
    public HashMap<String, Integer> sortedPrerollCounter() {
        HashMap<String, Integer> prerollCounter = new HashMap<>();
        HashMap<String, Integer> sortedPrerollCounter;
        Set<String> showIdSet = getListOfValuesByKeyFromDownloadIdentifierJson("showId");

        for (String showId : showIdSet) {
            List<JSONObject> fiteredJsonList = filterJsonListByShowIdValue(jsonList, showId);
            int counter = countPrerollFromList(fiteredJsonList);
            prerollCounter.put(showId, counter);
        }

        sortedPrerollCounter = mapUtils.sortByValue(prerollCounter);
        return sortedPrerollCounter;
    }

    /**
     * Retrieves a map of podcasts held weekly.
     *
     * @return a HashMap where the key is the "showId" and the value is a string of format "EEE HH:mm"
     */
    public HashMap<String, String> getWeeklyPodcasts() {
        HashMap<String, String> timestampHashMap = new HashMap<>();
        Set<String> showIdSet = getListOfValuesByKeyFromDownloadIdentifierJson("showId");

        for (String showId : showIdSet) {
            List<Timestamp> timestamps = new ArrayList<>();
            List<JSONObject> filteredShowIdList = filterJsonListByShowIdValue(jsonList, showId);
            List<JSONObject> opportunities = getOpportunities(filteredShowIdList);

            for (JSONObject opportunity : opportunities) {
                Timestamp originalEventTime = new Timestamp(opportunity.getLong("originalEventTime"));
                if (!timestamps.contains(originalEventTime)) {
                    timestamps.add(originalEventTime);
                }
            }

            if (timestampUtils.checkSevenDayDifference(timestamps)) {
                String formatedTimestamp = timestampUtils.parseTimestampToString(
                        timestamps.get(0));
                timestampHashMap.put(showId, formatedTimestamp);
            }
        }

        return timestampHashMap;
    }

    /**
     * Counts the number of "preroll" ad breaks from a list of JSON objects.
     *
     * @param jsonList the list of JSON objects to process
     * @return the count of "preroll" ad breaks
     */
    private Integer countPrerollFromList(List<JSONObject> jsonList) {
        int counter = 0;
        List<JSONObject> opportunities = getOpportunities(jsonList);
        for (JSONObject opportunity : opportunities) {
            JSONObject jsonOpportunity = opportunity;
            JSONObject positionUrlSegments = jsonOpportunity.getJSONObject("positionUrlSegments");
            JSONArray adBreakIndexes = positionUrlSegments.getJSONArray("aw_0_ais.adBreakIndex");
            for (int i = 0; i < adBreakIndexes.length(); i++) {
                String adBreakIndex = adBreakIndexes.get(i).toString();
                if (adBreakIndex.contains("preroll")) {
                    counter++;
                }

            }
        }
        return counter;
    }

    /**
     * Filters a list of JSON objects based on a specified key-value pair.
     *
     * @param jsonList the list of JSON objects to filter
     * @param key      the key to look for in each JSON object
     * @param value    the value to match against the key's value in each JSON object
     * @return a list of JSON objects that contain the specified key-value pair
     */
    private List<JSONObject> filterListByKeyValue(List<JSONObject> jsonList, String key, String value) {
        List<JSONObject> filteredJsonList = new ArrayList<>();

        for (JSONObject json : jsonList) {
            String jsonValue = json.getString(key);
            if (jsonValue.equalsIgnoreCase(value)) {
                filteredJsonList.add(json);
            }
        }
        return filteredJsonList;
    }

    /**
     * Filters a list of JSON objects based on a specified "showId" value.
     *
     * @param jsonObjectList the list of JSON objects to filter
     * @param value          the "showId" value to match against
     * @return a list of JSON objects that contain the specified "showId" value
     */
    private List<JSONObject> filterJsonListByShowIdValue(List<JSONObject> jsonObjectList, String value) {
        List<JSONObject> filteredJsonList = new ArrayList<>();

        for (JSONObject jsonObject : jsonObjectList) {
            JSONObject downloadIdentifierJson = jsonObject.getJSONObject("downloadIdentifier");
            String showIdValue = downloadIdentifierJson.getString("showId");
            if (showIdValue.equalsIgnoreCase(value)) {
                filteredJsonList.add(jsonObject);
            }
        }
        return filteredJsonList;
    }

    /**
     * Retrieves a list of values associated with a specified key from a list of JSON objects.
     *
     * @param key the key to look for in each JSON object
     * @return a list of values corresponding to the specified key from the JSON objects
     */
    private List<String> getListOfValuesByKeyFromMainJson(String key) {
        List<String> values = new ArrayList<>();
        for (JSONObject jsonObject : jsonList) {
            String value = jsonObject.getString(key);
            values.add(value);
        }
        return values;
    }

    /**
     * Retrieves a set of unique values associated with a specific key from a list of JSON objects
     *
     * @param key the
     * @return a set of unique values from a list of JSON objects
     */
    private Set<String> getListOfValuesByKeyFromDownloadIdentifierJson(String key) {
        Set<String> values = new HashSet<>();
        for (JSONObject jsonObject : getDownloadIdentifierJsonList()) {
            String value = jsonObject.getString(key);
            values.add(value);
        }
        return values;
    }

    /**
     * Extracts a list of "downloadIdentifier" JSON objects from a list of JSON objects.
     *
     * @return a list of JSON objects containing the "downloadIdentifier" key
     */
    private List<JSONObject> getDownloadIdentifierJsonList() {
        List<JSONObject> downloadIdentifierJsonList = new ArrayList<>();
        for (JSONObject jsonObject : jsonList) {
            JSONObject downloadIdentifierJson = jsonObject.getJSONObject("downloadIdentifier");
            downloadIdentifierJsonList.add(downloadIdentifierJson);
        }
        return downloadIdentifierJsonList;
    }

    /**
     * Method to extract a list of "opportunities" from a list of JSON objects
     *
     * @param jsonList the list to be processed
     * @return a list of JSON objects that contain the specified "opportunities"
     */
    private List<JSONObject> getOpportunities(List<JSONObject> jsonList) {
        List<JSONObject> opportunities = new ArrayList<>();
        for (JSONObject jsonObject : jsonList) {
            JSONArray opportunitiesArray = jsonObject.getJSONArray("opportunities");
            for (Object opportunity : opportunitiesArray) {
                JSONObject jsonOpportunity = (JSONObject) opportunity;
                opportunities.add(jsonOpportunity);
            }
        }
        return opportunities;
    }
}