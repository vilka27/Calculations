package com.fork27.calculator.Math;

import java.math.BigDecimal;
import java.util.ArrayList;

/*класс реализующий интервалы*/
public class Interval {
    public static final BigDecimal POSITIVE_INFINITY = BigMath.POSITIVE_INFINITY;//+бесконечность
    public static final BigDecimal NEGATIVE_INFINITY = BigMath.NEGATIVE_INFINITY;//-бесконечность
    public static int accuracy = 20;

    public static final char UNCERTANITY_CHAR = '▒';//'?';//'\u2BD1'; символ обозначающий неизвестную цифру стоящую в данном разряде числа
    public static final char INFINITY_CHAR = '∞';
    //public static final char ELLIPSIS_CHAR = '…';
    public static final char PLUS_MINUS = '±';
    public static final char BRACKET_L = '[';
    public static final char BRACKET_R = ']';
    public static final char BRACKET_L_O = '(';
    public static final char BRACKET_R_O = ')';
    public static final String POINT = "\\.";

    private BigDecimal l = BigDecimal.ZERO;//левый конец интервала
    private BigDecimal r = BigDecimal.ZERO;//правый конец интервала
    private boolean isLeftOpen;
    private boolean isRightOpen;


    //конструкторы
    public Interval() {
    }


    public Interval(BigDecimal left, BigDecimal right, boolean iLO, boolean iRO) {//предполагается что левое число меньше или равно правому
        this.l = left;
        this.r = right;
        this.isLeftOpen = iLO;
        this.isRightOpen = iRO;
    }

    /*public Interval(int int1, int int2) {
        this.l = new BigDecimal(int1);
        this.r = new BigDecimal(int2);
    }

    public Interval(String s1, String s2) {
        this.l = new BigDecimal(s1);
        this.r = new BigDecimal(s2);
    }*/

    public Interval(String s) { //распознает интервалы в виде 3.1415... или 3.14155 ± 0.00005 или  [3.14;3.15]

        if (s.charAt(s.length() - 1) == UNCERTANITY_CHAR/*ELLIPSIS_CHAR*/) {
            s = s.substring(0, s.length() - 1);
            BigDecimal a = new BigDecimal(s);
            BigDecimal b;
            if (a.signum() >= 0) {
                b = a.add(a.ulp());
                this.isRightOpen = true;
                this.isLeftOpen = false;
            } else {
                b = a.subtract(a.ulp());
                this.isRightOpen = true;
                this.isLeftOpen = false;
            }
            this.l = a;
            this.r = b;
        } else if (s.contains("" + PLUS_MINUS)) {
            String[] str = s.split(PLUS_MINUS + "");
            BigDecimal mid = new BigDecimal(str[0]);
            BigDecimal err = BigDecimal.ZERO;
            if (str.length > 1) {
                err = new BigDecimal(str[1]);
            }
            this.l = (mid).subtract(err);
            this.r = mid.add(err);
            this.isRightOpen = false;
            this.isLeftOpen = false;


        } else if (s.charAt(0) == BRACKET_L || s.charAt(0) == BRACKET_L_O) {
            if (s.charAt(0) == BRACKET_L) {
                this.isLeftOpen = false;
            } else {
                this.isLeftOpen = true;
            }
            if (s.charAt(s.length() - 1) == BRACKET_R) {
                this.isRightOpen = false;
            } else {
                this.isRightOpen = true;
            }
            s = s.substring(1, s.length() - 1);
            String[] str = s.split(";");
            this.l = new BigDecimal(str[0]);
            this.r = new BigDecimal(str[1]);
        } else {
            BigDecimal a = new BigDecimal(s);
            this.l = a;
            this.r = a;
            this.isRightOpen = false;
            this.isLeftOpen = false;
        }
    }

    public void removeZeroes() { //убирает лишние нули после запятой
        if (this.l != NEGATIVE_INFINITY) {
            this.l = this.l.stripTrailingZeros();
        }
        if (this.r != POSITIVE_INFINITY) {
            this.r = this.r.stripTrailingZeros();
        }
    }

    public String toStringEllipsis() {//строковое представление интервала в  виде строки с многоточием 3.1415...

        if (BigMath.equals(this.l, this.r)) {
            if (isLeftOpen && isRightOpen) {
                return "emptySet";
            }
            return BigMath.toPlainString(this.l);
        } else {
            if (!this.isFinite()) {
                if (l == NEGATIVE_INFINITY && r == POSITIVE_INFINITY) {
                    return PLUS_MINUS + "" + INFINITY_CHAR;
                }else if (l == NEGATIVE_INFINITY) {
                    return "-" + INFINITY_CHAR;
                }else{
                    return "" + INFINITY_CHAR;
                }
            } else {//конечные
                if (!isLeftOpen && !isRightOpen) {
                    String res = "";
                    if (this.l.signum() != this.r.signum()) {
                        res = res + PLUS_MINUS;
                    }
                    //this.removeZeroes();
                    String[] leftS = this.l.toPlainString().split(POINT);
                    String[] rightS = this.r.toPlainString().split(POINT);
                    if (leftS[0].length() != rightS[0].length()) {
                        char[] chars = new char[Math.max(leftS.length, rightS.length)];
                        for (int i = 0; i < chars.length; i++) {
                            chars[i] = UNCERTANITY_CHAR;//знак неопределенности
                        }
                        res = res + new String(chars);
                        return res;
                    } else {

                        int i;
                        boolean flag = false;
                        for (i = 0; i < leftS[0].length(); i++) {
                            if (leftS[0].charAt(i) == rightS[0].charAt(i)) {
                                res = res + leftS[0].charAt(i);
                            } else {
                                ////////////////////////////////////////////////////////////////////////////////
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            for (int j = i; j < leftS[0].length(); j++) {
                                res = res + UNCERTANITY_CHAR;
                            }
                            return res;
                        } else {
                            res = res + ".";

                            String n;
                            String m;
                            if (leftS[1].length() > rightS[1].length()) {
                                n = rightS[1];
                                m = leftS[1];
                            } else {
                                n = leftS[1];
                                m = rightS[1];
                            }
                            for (int k = n.length(); k < m.length(); k++) {
                                n = n + "0";
                            }

                            for (i = 0; i < m.length(); i++) {
                                if (n.charAt(i) == m.charAt(i)) {
                                    res = res + m.charAt(i);
                                } else {
                                    break;
                                }
                            }


                        }
                        res = res + UNCERTANITY_CHAR;//ELLIPSIS_CHAR;
                        return res;
                    }

                } else{
                    String res = "";
                    if (this.l.signum() != this.r.signum()) {
                        res = res + PLUS_MINUS;
                    }
                    //this.removeZeroes();
                    BigDecimal left=this.l;
                    BigDecimal right=this.r;


                    if(isRightOpen){
                        right=BigMath.sub(r, r.ulp());
                    }
                    if(isLeftOpen){
                        left=BigMath.add(l,l.ulp());
                        /////////////////////////////////////////////
                    }
                    String[] leftS = this.l.toPlainString().split(POINT);
                    String[] rightS = right.toPlainString().split(POINT);

                    if (leftS[0].length() != rightS[0].length()) {
                        char[] chars = new char[Math.max(leftS.length, rightS.length)];
                        for (int i = 0; i < chars.length; i++) {
                            chars[i] = UNCERTANITY_CHAR;//знак неопределенности
                        }
                        res = res + new String(chars);
                        return res;
                    } else {

                        int i;
                        boolean flag = false;
                        for (i = 0; i < leftS[0].length(); i++) {
                            if (leftS[0].charAt(i) == rightS[0].charAt(i)) {
                                res = res + leftS[0].charAt(i);
                            } else {
                                ////////////////////////////////////////////////////////////////////////////////
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            for (int j = i; j < leftS[0].length(); j++) {
                                res = res + UNCERTANITY_CHAR;
                            }
                            return res;
                        } else {
                            res = res + ".";

                            String n;
                            String m;
                            if (leftS[1].length() > rightS[1].length()) {
                                n = rightS[1];
                                m = leftS[1];
                            } else {
                                n = leftS[1];
                                m = rightS[1];
                            }
                            for (int k = n.length(); k < m.length(); k++) {
                                n = n + "0";
                            }

                            for (i = 0; i < m.length(); i++) {
                                if (n.charAt(i) == m.charAt(i)) {
                                    res = res + m.charAt(i);
                                } else {
                                    break;
                                }
                            }


                        }
                        res = res + UNCERTANITY_CHAR;//ELLIPSIS_CHAR;
                        return res;
                    }

                }
            }
        }
    }

    public String toStringError() {//строковое представление интервала в виде <число> +- <погрешность>
        if (BigMath.equals(this.l, this.r)) {
            return BigMath.toPlainString(this.l);
        } else {
            if (this.contains(POSITIVE_INFINITY)) {//бесконечные значения
                if (this.contains(NEGATIVE_INFINITY)) {
                    return "0" + PLUS_MINUS + INFINITY_CHAR;
                }
                return BigMath.toPlainString(this.l) + "+" + INFINITY_CHAR;
            } else if (this.contains(NEGATIVE_INFINITY)) {
                return BigMath.toPlainString(this.r) + "-" + INFINITY_CHAR;

            } else {//конечные
                BigDecimal mid = BigMath.add(this.l, this.r).divide(new BigDecimal(2));
                BigDecimal err = BigMath.sub(this.r, this.l).divide(new BigDecimal(2));
                return (mid.toPlainString() + "" + PLUS_MINUS + err.toPlainString());

            }
        }
    }

    public String toString() {//обычная запись интервала [a;b]
        String l;
        if (this.l == BigMath.NEGATIVE_INFINITY) {
            l = "-" + INFINITY_CHAR;
        } else if (this.l == BigMath.POSITIVE_INFINITY) {
            l = INFINITY_CHAR + "";
        } else {

            l = this.l.stripTrailingZeros().toPlainString();
        }
        String r;
        if (this.r == BigMath.NEGATIVE_INFINITY) {
            r = "-" + INFINITY_CHAR;
        } else if (this.r == BigMath.POSITIVE_INFINITY) {
            r = INFINITY_CHAR + "";
        } else {
            r = this.r.stripTrailingZeros().toPlainString();
        }
        return (BRACKET_L + l + ";" + r + BRACKET_R);
    }

    public boolean isIntersect(Interval i) { //пересекаются ли интервалы
        if (this.contains(i.l) || this.contains(i.r)) {
            return true;
        } else {
            return false;
        }
    }

    public Interval add(Interval i2) {//сложение
        Interval result = new Interval();
        result.l = BigMath.add(this.l, i2.l);
        result.r = BigMath.add(this.r, i2.r);
        return result;
    }

    public Interval sub(Interval i) {//вычиатние
        return this.add(i.negate());
    }

    public Interval mult(Interval i) {//умножение
        BigDecimal ll = BigMath.multiply(this.l, i.l);
        BigDecimal lr = BigMath.multiply(this.l, i.r);
        BigDecimal rl = BigMath.multiply(this.r, i.l);
        BigDecimal rr = BigMath.multiply(this.r, i.r);

        Interval result = new Interval();
        result.l = BigMath.min(BigMath.min(ll, lr), BigMath.min(rl, rr));
        result.r = BigMath.max(BigMath.max(ll, lr), BigMath.max(rl, rr));
        return result;
        //return new Interval (((ll.min(lr)).min(rl)).min(rr),ll.max(lr).max(rl).max(rr));
    }

    public ArrayList<Interval> div(Interval i) { //деление
        ArrayList arr = new ArrayList<Interval>();
        if (!i.contains(BigDecimal.ZERO)) {
            arr.add(this.mult(i.invert()));
            return arr;
        } else {
            if (this.l.signum() != this.r.signum() && BigMath.compare(this.l, BigDecimal.ZERO) != 0 && BigMath.compare(this.r, BigDecimal.ZERO) != 0) {
                arr.add(new Interval(BigMath.NEGATIVE_INFINITY, BigMath.POSITIVE_INFINITY, false, false));
                return arr;
            }
            BigDecimal bd;
            if (i.r.equals(BigDecimal.ZERO)) {
                if (i.l.equals(BigDecimal.ZERO)) {
                    return null;
                }

                if (BigMath.compare(this.l, BigDecimal.ZERO) > 0) {

                    bd = BigMath.divide(this.l, i.l, accuracy, BigDecimal.ROUND_CEILING);

                    arr.add(new Interval(BigMath.NEGATIVE_INFINITY, bd));
                    return arr;
                } else {

                    bd = BigMath.divide(this.r, i.l, accuracy, BigDecimal.ROUND_FLOOR);

                    arr.add(new Interval(bd, POSITIVE_INFINITY));
                    return arr;
                }

            }
            if (i.l.equals(BigDecimal.ZERO)) {
                if (this.l.signum() > 0) {

                    bd = BigMath.divide(this.l, i.r, accuracy, BigDecimal.ROUND_FLOOR);

                    arr.add(new Interval(bd, POSITIVE_INFINITY));
                    return arr;
                } else {

                    bd = BigMath.divide(this.r, i.r, accuracy, BigDecimal.ROUND_CEILING);

                    arr.add(new Interval(NEGATIVE_INFINITY, bd));
                    return arr;
                }
            }

            if (this.r.signum() < 0) {

                bd = BigMath.divide(this.r, i.r, accuracy, BigDecimal.ROUND_CEILING);

                arr.add(new Interval(NEGATIVE_INFINITY, bd));

                bd = BigMath.divide(this.r, i.l, accuracy, BigDecimal.ROUND_FLOOR);

                arr.add(new Interval(bd, POSITIVE_INFINITY));
                return arr;
            } else {

                bd = BigMath.divide(this.l, i.l, accuracy, BigDecimal.ROUND_CEILING);

                arr.add(new Interval(NEGATIVE_INFINITY, bd));

                bd = BigMath.divide(this.l, i.r, accuracy, BigDecimal.ROUND_FLOOR);

                arr.add(new Interval(bd, POSITIVE_INFINITY));
                return arr;
            }
        }


    }

    public boolean contains(BigDecimal num) { //содержит ли интервал число num
        if (BigMath.compare(this.l, num) < 0 && BigMath.compare(this.r, num) > 0) {

            return true;
        } else if ((BigMath.equals(this.l, num) && !isLeftOpen) || (BigMath.equals(this.r, num) && !isRightOpen)) {
            return true;
        }
        return false;
    }


    private Interval invert() {//возвращает результат деления единицы на интервал не содержащий 0
        Interval result = new Interval();
        if (!this.contains(BigDecimal.ZERO)) {
            try {
                result.l = BigMath.invert(this.r);
            } catch (Exception e) {
                result.l = BigMath.invert(this.r, accuracy, BigDecimal.ROUND_FLOOR);
            }
            try {
                result.r = BigMath.invert(this.l);
            } catch (Exception e) {
                result.r = BigMath.invert(this.l, accuracy, BigDecimal.ROUND_CEILING);
            }
            return result;
        }
        return null;
    }

    private ArrayList<Interval> invert2() {//результат деления единицы на интервал содержащий ноль
        ArrayList<Interval> arr = new ArrayList<>();

        arr.add(new Interval(BigMath.divide(BigDecimal.ONE, this.r, accuracy, BigDecimal.ROUND_FLOOR), BigMath.POSITIVE_INFINITY));
        arr.add(new Interval(NEGATIVE_INFINITY, BigMath.divide(BigDecimal.ONE, this.l, accuracy, BigDecimal.ROUND_CEILING)));
        return arr;
    }


    public Interval negate() { //результат умножения интервала на -1
        Interval result = new Interval();
        result.l = BigMath.negateBD(this.r);
        result.r = BigMath.negateBD(this.l);
        return result;
    }


    public Interval pow(int n) {//возведение в целую степень
        if (!this.contains(BigDecimal.ZERO)) {
            Interval result;
            if (n > 0) {
                result = this.clone();
                for (int i = 1; i < n; i++) {
                    result.mult(this);
                }
            } else if (n < 0) {
                if (!this.contains(BigDecimal.ZERO)) {
                    result = this.clone();
                    for (int i = -1; i > n; i--) {
                        result.mult(this);
                    }
                    result = result.invert();
                } else {
                    return null;
                }
            } else {
                result = new Interval(BigDecimal.ONE, BigDecimal.ONE);
            }
            return result;
        } else {
            return null;
        }
    }


    public Interval clone() {
        return new Interval(this.l, this.r);
    }

    public boolean isFinite() {
        if (l == NEGATIVE_INFINITY || r == POSITIVE_INFINITY) {
            return true;
        } else {
            return false;
        }

    }

    public BigDecimal getLeft() {
        return l;
    }

    public BigDecimal getRight() {
        return r;
    }

    static class EndPoint{


        BigDecimal value;
        boolean isOpen=false;
         public EndPoint(BigDecimal b, boolean isOp){
            value=b;
            isOpen=isOp;
         }
        public EndPoint(BigDecimal b){
            value=b;
        }


    }

    public static EndPoint add(EndPoint e1, EndPoint e2){

        return new EndPoint(BigMath.add(e1.value,e2.value),e1.isOpen||e2.isOpen);
    }
    public static EndPoint sub(EndPoint e1,EndPoint e2){
        return new EndPoint(BigMath.sub(e1.value,e2.value),e1.isOpen||e2.isOpen);
    }


}
