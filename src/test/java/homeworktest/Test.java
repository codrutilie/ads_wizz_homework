package homeworktest;

import homeworktest.util.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Test {
    private TestUtils testUtils;

    public Test() {
        testUtils = new TestUtils();
    }

    @org.junit.Test
    public void ex3() {
        HashMap<String, Integer> sortedShowIdCounter = testUtils.sortedShowIdCounter();

        Object key = sortedShowIdCounter.keySet().toArray()[0];
        int value = sortedShowIdCounter.get(key);
        System.out.println("Most popular show is: " + key);
        System.out.println("Number of downloads: " + value);

        assertEquals("Who Trolled Amber", key);
        assertEquals(value,24);
    }

    @org.junit.Test
    public void ex4() {
        HashMap<String, Integer> sortedDeviceTypeCounter = testUtils.sortedDeviceTypeCounter();

        Object key = sortedDeviceTypeCounter.keySet().toArray()[0];
        int value = sortedDeviceTypeCounter.get(key);
        System.out.println("Most popular device is: " + key);
        System.out.println("Number of downloads: " + value);

        assertEquals("mobiles & tablets", key);
        assertEquals(60, value);
    }

    @org.junit.Test
    public void ex5() {
        List<String> expectedShowId = new ArrayList<>(List.of("Stuff You Should Know", "Who Trolled Amber",
                "Crime Junkie", "The Joe Rogan Experience"));
        List<Integer> expectedPrerollNumber = new ArrayList<>(List.of(40, 40, 30, 10));
        HashMap<String, Integer> sortedPrerollCounter = testUtils.sortedPrerollCounter();

        for (int i = 0; i < expectedShowId.size(); i++) {
            Object key = sortedPrerollCounter.keySet().toArray()[i];
            Integer value = sortedPrerollCounter.get(key);

            System.out.println("Show Id: " + key + ", Preroll Opportunity Number: " + value);

            assertEquals(key, expectedShowId.get(i));
            assertEquals(value, expectedPrerollNumber.get(i));
        }
    }

    @org.junit.Test
    public void ex6() {
        List<String> expectedWeeklyPodcasts = new ArrayList<>(List.of("Crime Junkie", "Who Trolled Amber"));
        List<String> expectedNumberOfWeeklyPodcasts = new ArrayList<>(List.of("Wed 22:00", "Mon 20:00"));
        HashMap<String, String> weeklyPodcasts = testUtils.getWeeklyPodcasts();

        System.out.println("Weekly shows are: ");

        for (int i = 0; i < expectedWeeklyPodcasts.size(); i++) {
            Object key = weeklyPodcasts.keySet().toArray()[i];
            String value = weeklyPodcasts.get(key);

            System.out.println(key + " - " + value);

            assertEquals(key, expectedWeeklyPodcasts.get(i));
            assertEquals(value, expectedNumberOfWeeklyPodcasts.get(i));
        }
    }
}
