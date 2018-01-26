package uk.co.codefreak.rhythmmachine.time;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Time {

    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal val = new BigDecimal(value);
        val = val.setScale(places, RoundingMode.HALF_UP);
        return val.doubleValue();
    }

    // Converts time from seconds into hours.
    public String toHours(int seconds) {
        String timeString;
        double intoMinutes = seconds / 60;
        double intoHours = intoMinutes / 60;

        timeString = round(intoHours,2) + " hours";
        return timeString;
    }
}
