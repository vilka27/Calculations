package com.fork27.calculator;

import android.util.Log;

import com.fork27.calculator.Math.KahInterval;

import java.util.concurrent.ExecutionException;

public class Tree {

    private Node root;

    public Tree() {
    }

    public Tree(Node r) {
        this.root = r;
    }

    public KahInterval execute() {
        if (root != null) {
            try{
            return root.execute();
            }catch (UnsupportedOperationException u){
                return new KahInterval();
            }
        }
        return null;
    }

    public String toString() {
        if (root != null) {
            root.setBrackets();
            return root.toString();
        }
        return "";
    }
    public boolean isEmpty(){
        return(root==null);
    }

    public String toString(int level) {
        if (root != null) {
            try {
                root.setBrackets();
            }catch(Exception e){
                Log.e("","hey",e);
            }
            String res = null;
            return root.toStringConv(level);
        }

return "";

    }

    public void addNode(Node node) {
        if (root == null) {
            root = node;
        } else {
            if(root.isTerminal){
                node.setChild(root,0);
                root=node;
                return;
            }
                if (root.getChildren()[1] == null && root.getChildren()[0] != null) {
                    root.setChild(node, 1);
                } else {
                    node.setChild(root, 0);
                    root = node;
                }


            }
        }
    public Node getRoot() {
        return root;
    }
    public void setRoot(Node root) {
        this.root = root;
    }

}
