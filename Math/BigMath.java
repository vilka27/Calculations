package com.fork27.calculator.Math;

import java.math.BigDecimal;
import java.math.RoundingMode;
//класс реализующий арифметические операции для конечных BigDecimal чисел и специальных значений POSITIVE_INFINITY и NEGATIVE_INFINITY
public class BigMath {

    //костыль позволяющий ввести бесконечные числа
    public static final BigDecimal POSITIVE_INFINITY = new BigDecimal(99999);
    public static final BigDecimal NEGATIVE_INFINITY = new BigDecimal(-99999);
    //

    public static final char INFINITY_CHAR = Interval.INFINITY_CHAR;

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        if (b1 == NEGATIVE_INFINITY || b2 == NEGATIVE_INFINITY) {
            if (b1 != POSITIVE_INFINITY && b2 != POSITIVE_INFINITY) {
                return NEGATIVE_INFINITY;
            } else return null;
        } else if (b1 == POSITIVE_INFINITY || b2 == POSITIVE_INFINITY) {

            return POSITIVE_INFINITY;

        } else return b1.add(b2);
    }

    public static BigDecimal sub(BigDecimal b1, BigDecimal b2) {
        return add(b1, negateBD(b2));
    }

    public static boolean isFinite(BigDecimal b) {
        if (b == POSITIVE_INFINITY || b == NEGATIVE_INFINITY) {
            return false;
        } else {
            return true;
        }
    }

    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        if (!isFinite(b1) || !isFinite(b2)) {
            if (b1.signum() * b2.signum() < 0) {
                return NEGATIVE_INFINITY;
            } else if (b1.signum() * b2.signum() > 0) {
                return POSITIVE_INFINITY;
            } else return BigDecimal.ZERO;
        } else {
            return b1.multiply(b2);
        }
    }

    public static BigDecimal max(BigDecimal b1, BigDecimal b2) {
        if (compare(b1, b2) > 0) {
            return b1;
        } else {
            return b2;
        }
    }

    public static BigDecimal min(BigDecimal b1, BigDecimal b2) {
        if (compare(b1, b2) < 0) {
            return b1;
        } else {
            return b2;
        }
    }

    public static int compare(BigDecimal b1, BigDecimal b2) {
        //-1 b1<b2
        // 0 b1=b2
        // +1 b1>b2
        if (b1 == b2) {
            return 0;
        } else if (b1 == NEGATIVE_INFINITY) {
            return -1;
        } else if (b2 == NEGATIVE_INFINITY) {
            return 1;
        } else if (b1 == POSITIVE_INFINITY) {
            return 1;
        } else if (b2 == POSITIVE_INFINITY) {
            return -1;
        } else {
            return b1.compareTo(b2);
        }
    }

    public static boolean equals(BigDecimal b1, BigDecimal b2) {
        if (!isFinite(b1) || !isFinite(b2)) {
            return b1 == b2;
        } else {
            return b1.equals(b2);
        }
    }

    public static String toPlainString(BigDecimal b) {
        if (isFinite(b)) {

            return b.stripTrailingZeros().toPlainString();
        } else {
            if (b == POSITIVE_INFINITY) {
                return INFINITY_CHAR + "";
            } else {
                return "-" + INFINITY_CHAR;
            }
        }
    }

    public static BigDecimal negateBD(BigDecimal b) {
        if (b == POSITIVE_INFINITY) {
            return NEGATIVE_INFINITY;
        }
        if (b == NEGATIVE_INFINITY) {
            return POSITIVE_INFINITY;
        } else return b.negate();
    }

    public static BigDecimal invert(BigDecimal b) {
        if (b == POSITIVE_INFINITY || b == NEGATIVE_INFINITY) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.ONE.divide(b);
        }
    }

    public static BigDecimal invert(BigDecimal b, int scale, int roundingMode) {
        if (b == POSITIVE_INFINITY || b == NEGATIVE_INFINITY) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.ONE.divide(b, scale, roundingMode);

        }
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale, int roundingMode) {
        BigDecimal b3;
        try {
            b3 = multiply(b1, invert(b2));
        } catch (Exception e) {
            b3 = multiply(b1, invert(b2, scale, roundingMode));
        }
        return b3;
    }
/*
    public static BigDecimal exponent_d(BigDecimal x) {

        BigDecimal sum = new BigDecimal(0);
        for (int n = 0; n < limit; n++) {
            sum = sum.add(x.pow(n).divide(factorial(n), accuracy, RoundingMode.FLOOR));
        }
        return sum;
    }

    public static BigDecimal exponent_u(BigDecimal x) {//вообще говоря с помощью ряда м приближаемся к значению снизу,
        //// а значет верхняя оценка в данном виде не точна
        //необходимо добавить остаточный член ряда

        BigDecimal sum = new BigDecimal(0);
        int n;
        for (n = 0; n < limit; n++) {
            sum = sum.add(x.pow(n).divide(factorial(n), accuracy, RoundingMode.CEILING));
        }
        sum = sum.add(x.pow(n).divide(factorial(n), accuracy, RoundingMode.CEILING));
        sum = sum.add(x.pow(n).divide(factorial(n), accuracy, RoundingMode.CEILING));
        return sum;
    }

    public static BigDecimal factorial(int n) {
        BigDecimal res = BigDecimal.valueOf(1);
        for (int i = 2; i <= n; i++) {
            res = res.multiply(BigDecimal.valueOf(i));
        }
        return res;
    }

    public static BigDecimal sinus_u(BigDecimal x) {
        BigDecimal sum = BigDecimal.ZERO;
        int n;
        for (n = 0; n < limit; n++) {
            sum = sum.add(x.pow(2 * n + 1).divide(factorial(2 * n + 1), accuracy, RoundingMode.CEILING));
            n++;
            sum = sum.subtract(x.pow(2 * n + 1).divide(factorial(2 * n + 1), accuracy, RoundingMode.FLOOR));
        }
        sum = sum.add(x.pow(2 * n + 1).divide(factorial(2 * n + 1), accuracy, RoundingMode.CEILING));
        return sum;
    }


    public static BigDecimal sinus_d(BigDecimal x) {
        BigDecimal sum = BigDecimal.ZERO;
        int n;
        for (n = 0; n < limit; n++) {
            sum = sum.add(x.pow(2 * n + 1).divide(factorial(2 * n + 1), accuracy, RoundingMode.FLOOR));
            n++;
            sum = sum.subtract(x.pow(2 * n + 1).divide(factorial(2 * n + 1), accuracy, RoundingMode.CEILING));
        }
        return sum;
    }
    */
}
