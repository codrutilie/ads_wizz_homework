package homeworktest.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

public class TimestampUtils {
    /**
     * Checks if the difference between consecutive timestamps is exactly seven days.
     *
     * @param timestamps List of timestamps to check.
     * @return true if the difference between each consecutive timestamp is seven days, false otherwise.
     */
    public boolean checkSevenDayDifference(List<Timestamp> timestamps) {
        for (int i = 1; i < timestamps.size(); i++) {
            Instant previous = timestamps.get(i - 1).toInstant();
            Instant current = timestamps.get(i).toInstant();
            if (ChronoUnit.DAYS.between(previous, current) != 7) {
                return false;
            }
        }
        return true;
    }

    /**
     * Formats a Timestamp to a string in the "EEE HH:mm" format.
     *
     * @param timestamp the Timestamp to be formatted
     * @return the formatted string
     */
    public String parseTimestampToString(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatedDate = sdf.format(timestamp);

        return formatedDate;
    }
}
