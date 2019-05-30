package com.fork27.calculator;

import com.fork27.calculator.Math.KahInterval;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Node {
    //узлы могут представлять либо мат оператор Operator2 либо константу-мультиинтервал KahInterval
    static public final Operator2 ADD = new Operator2("+", 1, 2);
    static public final Operator2 SUB = new Operator2("-", 1, 2);
    static public final Operator2 MULT = new Operator2("*", 2, 2);
    static public final Operator2 DIV = new Operator2("/", 2, 2);

    public boolean isTerminal = true;
    private boolean needBrackets = false;
    private KahInterval result = null;
    private Operator2 operator = null;
    private Node[] children;
    private int level = 0;

    //private Node parent;

    public Node(KahInterval ki) {
        this.result = ki;
        this.isTerminal = true;
        this.level = 0;
    }

    public Node(Node[] nodes, Operator2 op) {

        this.children = nodes;
        this.operator = op;
        this.isTerminal = false;
        this.level = Math.max(nodes[0].getLevel(), nodes[1].getLevel()) + 1;
    }

    public Node(Operator2 op) {
        this.operator = op;
        this.isTerminal = false;
        this.level = 0;
        this.children = new Node[op.argNumber];
    }

    public Node() {
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public KahInterval execute() throws UnsupportedOperationException {
        if (isTerminal) {
            return result;
        } else {
            if (result != null) {
                return result;
            }
            KahInterval[] ki = new KahInterval[children.length];
            try {
                for (int i = 0; i < children.length; i++) {
                    ki[i] = children[i].execute();
                }
                result = executeOperator(operator, ki);
                return result;
            } catch (UnsupportedOperationException u) {
                throw u;
            }
        }
    }


    public KahInterval executeOperator(Operator2 op, KahInterval[] ki) throws UnsupportedOperationException {
        if (ki.length != op.argNumber) {
            throw new UnsupportedOperationException("Неверное количество аргументов");
        } else {
            switch (op.argNumber) {
                case 2:
                    if (operator == ADD) {
                        result = ki[0].add(ki[1]);
                    } else if (operator == SUB) {
                        result = ki[0].sub(ki[1]);
                    } else if (operator == MULT) {
                        result = ki[0].mult(ki[1]);
                    } else if (operator == DIV) {
                        result = ki[0].divide(ki[1]);
                    }
                    return result;

                default:
                    return null;
            }

        }
    }

    public String toString() {
        if (isTerminal) {
            return result.toStringEllipsis();
        } else {
            String args[] = new String[children.length];
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    args[i] = children[i].toString();
                } else {
                    args[i] = "";
                }
            }
            String res="";
            switch (this.operator.argNumber) {
                case 2:
                    res = args[0] + operator.toString() + args[1];
            }
            if (needBrackets) {
                res = "("+res+")";
            }
                return res;
        }
    }

    /* public String toString(int maxLength){
         if(isTerminal){
             return result.toString();
         }
         if(toString().length()>maxLength){
             if (children[1] != null && children[0] != null){
                 String s1;
                 String s2;
                 if(children[0].isTerminal){
                      s1=children[0].toString();
                 }
             }
         }else{
             return toString();
         }
     }*/
    public String toStringConv(int level) {
        if (this.level <= level) {
            if (result == null) {
                setResult();
            }
            return result.toStringEllipsis();
        } else {
            return toString();
        }
    }

    public void setBrackets() {
        if (children != null) {
            if (children[0] != null && !children[0].isTerminal) {
                if (children[0].operator.priority < this.operator.priority) {
                    children[0].setNeedBrackets(true);
                } else {
                    children[0].setNeedBrackets(false);
                }
                children[0].setBrackets();
            }
            if (children[1] != null && !children[1].isTerminal) {
                if (children[1].operator.priority < this.operator.priority) {
                    children[1].setNeedBrackets(true);
                } else {
                    setNeedBrackets(false);
                }
                children[1].setBrackets();
            }
        } else {
            setNeedBrackets(false);
        }
    }

    public KahInterval getResult() {
        return result;
    }

    public void setResult() {
        this.result = execute();

    }


    public Node[] getChildren() {
        return children;
    }

    public void setChildren(Node[] children) {
        this.isTerminal = false;
        this.children = children;
        this.level = Math.max(children[0].getLevel(), children[1].getLevel()) + 1;

    }

    public void setChild(Node ch, int index) {
        this.children[index] = ch;
        this.isTerminal = false;
        this.level = Math.max(this.getLevel(), children[index].getLevel() + 1);
    }

    public boolean isNeedBrackets() {
        return needBrackets;
    }

    public void setNeedBrackets(boolean needBrackets) {
        this.needBrackets = needBrackets;
    }


    public Operator2 getOperator() {
        return operator;
    }

    public void setOperator(Operator2 operator) {
        this.operator = operator;
    }

}
