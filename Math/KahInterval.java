package com.fork27.calculator.Math;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*класс реализующий мультиинтервалы*/
public class KahInterval {

    private ArrayList<Interval> intervals = new ArrayList<>();//интервалы содержащиеся в мультиинтервале
    public static final char UNION_CHAR = '∪';

    public KahInterval() {
    }

    public KahInterval(String s) {
        this.intervals.add(new Interval(s));
    }

    private KahInterval(ArrayList<Interval> arr) {
        intervals = arr;
    }

    public KahInterval(Interval i) {
        intervals.add(i);
    }

   /* public KahInterval(String s1, String s2) {
        this.intervals.add(new Interval(s1, s2));
    }*/

    public KahInterval(BigDecimal b1, BigDecimal b2) {
        if (b1.compareTo(b2) < 1) {
            intervals.add(new Interval(b1, b2));
        } else {
            intervals.add(new Interval(Interval.NEGATIVE_INFINITY, b2));
            intervals.add(new Interval(b1, Interval.POSITIVE_INFINITY));
        }
    }

   /* public KahInterval(int b1, int b2) {
        if (b1 <= b2) {
            intervals.add(new Interval(b1, b2));
        } else {
            intervals.add(new Interval(BigMath.NEGATIVE_INFINITY, new BigDecimal(b2)));
            intervals.add(new Interval(new BigDecimal(b1), BigMath.POSITIVE_INFINITY));
        }
    }*/

    private KahInterval concateIntervals() {//обьединяет пересекающиеся интервалы мультиинтервала, если такие имеются
        for (int i = 0; i < intervals.size() - 1; i++) {
            for (int j = i + 1; j < intervals.size(); j++) {
                if (intervals.get(i).isIntersect(intervals.get(j))) {
                    Interval temp = new Interval(BigMath.min(intervals.get(i).getLeft(), intervals.get(j).getLeft()),
                            BigMath.max(intervals.get(i).getRight(), intervals.get(j).getRight()));
                    intervals.remove(j);
                    intervals.set(i, temp);

                }
            }
        }
        return this;
    }

    public KahInterval add(KahInterval ki) {//сложение
        KahInterval result = new KahInterval();
        for (int i = 0; i < ki.intervals.size(); i++) {
            result.addIntervals(this.add(ki.intervals.get(i)).intervals);
        }
        return result;
    }


    private KahInterval add(Interval inter) {
        KahInterval result = new KahInterval();
        for (int i = 0; i < intervals.size(); i++) {
            result.intervals.add(this.intervals.get(i).add(inter));
        }
        return result;
    }


    public KahInterval sub(KahInterval ki) {
        return this.add(ki.negate());
    }

    public KahInterval sub(Interval i) {
        return this.add(i.negate());
    }

    private KahInterval negate() {
        KahInterval result = new KahInterval();
        for (int i = 0; i < intervals.size(); i++) {
            result.intervals.set(i, intervals.get(i).negate());
        }
        return result;
    }

    private KahInterval mult(Interval inter) {
        KahInterval result = new KahInterval();
        for (int i = 0; i < intervals.size(); i++) {
            result.addInterval(inter.mult(this.intervals.get(i)));
        }
        return result;
    }

    public KahInterval mult(KahInterval ki) {
        KahInterval kahinter = new KahInterval();
        for (int i = 0; i < ki.intervals.size(); i++) {
            kahinter.addIntervals((this.mult(ki.intervals.get(i))).intervals);
        }
        return kahinter;
    }


    private void addInterval(Interval i) {
        this.intervals.add(i);
    }

    private void addIntervals(Collection<Interval> iCollection) {
        this.intervals.addAll(iCollection);
    }

    private KahInterval divide(Interval inter) {
        KahInterval kahinter = new KahInterval();
        for (int i = 0; i < this.intervals.size(); i++) {
            kahinter.addIntervals(this.intervals.get(i).div(inter));
        }
        return kahinter;

    }

    public KahInterval divide(KahInterval ki) {
        KahInterval kahinter = new KahInterval();
        for (int i = 0; i < ki.intervals.size(); i++) {
            kahinter.addIntervals(
                    this.divide(ki.intervals.get(i)).intervals);
        }
        return kahinter;
    }


    public String toStringEllipsis() { // 3.1415... работает только дл одного интервала
        if(this.intervals.size()==0){
            return "";
        }
        if (intervals.size() == 1) {
            return intervals.get(0).toStringEllipsis();
        } else {
            return null;
        }
    }

    public String toStringError() {// <число> +- <погрешность> работает только для 1 интервала
        if(this.intervals.size()==0){
            return "";
        }
        if (intervals.size() == 1) {
            return intervals.get(0).toStringError();
        } else {
            return null;
        }
    }

    public String toString() {// [a;b] ∪ [c;d]
        if(this.intervals.size()==0){
            return "";
        }
        String res = intervals.get(0).toString();
        for (int i = 1; i < intervals.size(); i++) {
            res = res + UNION_CHAR + intervals.get(i).toString();
        }
        return res;
    }
    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }


}
