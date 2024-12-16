package io.github.hellomaker.launcher.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    public static double twoPointDouble(double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float twoPointFloat(float number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
