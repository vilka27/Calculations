package com.fork27.calculator;

public class Operator2{
    static public final Operator2 ADD= new Operator2("+",1,2);
    static public final Operator2 SUB= new Operator2("-",1,2);
    static public final Operator2 MULT = new Operator2("*",2,2);
    static public final Operator2 DIV = new Operator2("/",2,2);
    static public final Operator2 MINUS = new Operator2("-",99,1);

    public String string;
    public int priority;
    public int argNumber;


    public Operator2(String s,int i,int args){
        this.string=s;
        this.priority=i;
        this.argNumber=args;
    }
    public String toString(){
        return this.string;
    }


}