package utcb.fii.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Measurement(double x, double r) {
    private static final Logger log = LogManager.getLogger(Measurement.class);

    public Measurement {
        if (x <= 0 || r <= 0) {
            log.error("Invalid values for log: X={}, R={}", x, r);
            throw new IllegalArgumentException("Values must be > 0");
        }
    }

    public double getDecimalLogX() {
        return roundTo5Decimals(Math.log10(x));
    }

    public double getDecimalLogR() {
        return roundTo5Decimals(Math.log10(r));
    }

    /**
     * Rounds the given double value to 5 decimal places.
     * If the value is not a valid number (NaN) or infinite, the original value is returned.
     *
     * @param value the double value to be rounded
     * @return the rounded value to 5 decimal places, or the original value if it is NaN or infinite
     */
    private double roundTo5Decimals(double value) {
        return (Double.isNaN(value) || Double.isInfinite(value)) ? value : BigDecimal.valueOf(value).setScale(5, RoundingMode.HALF_UP).doubleValue();
    }
}
