package com.fork27.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fork27.calculator.Math.Interval;
import com.fork27.calculator.Math.KahInterval;

public class MainActivity extends AppCompatActivity {
    public int intervalForm = 0;//0- строка с многоточием 1- +- 2- интервал
    public KahInterval currentInt;
    public Tree expressionTree;
    public Node currentNode;
    public KahInterval prevResult = new KahInterval();
    public static char UNCERTANITY_CHAR = Interval.UNCERTANITY_CHAR;
    public static char INFINITY_CHAR = Interval.INFINITY_CHAR;
    //public static char ELLIPSIS_CHAR = Interval.ELLIPSIS_CHAR;
    public static char PLUS_MINUS = Interval.PLUS_MINUS;

    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expressionTree = new Tree();


        Button num1 = findViewById(R.id.num1);
        Button num2 = findViewById(R.id.num2);
        Button num3 = findViewById(R.id.num3);
        Button num4 = findViewById(R.id.num4);
        Button num5 = findViewById(R.id.num5);
        Button num6 = findViewById(R.id.num6);
        Button num7 = findViewById(R.id.num7);
        Button num8 = findViewById(R.id.num8);
        Button num9 = findViewById(R.id.num9);
        Button num0 = findViewById(R.id.num0);

        Button point = findViewById(R.id.point);
        Button eq = findViewById(R.id.equal);
        Button add = findViewById(R.id.add);
        Button sub = findViewById(R.id.sub);
        Button mult = findViewById(R.id.mult);
        Button div = findViewById(R.id.div);
        TextView current = findViewById(R.id.current);
        Button back = findViewById(R.id.back);
        Button ellipsis = findViewById(R.id.ellipsis);
        Button infinity = findViewById(R.id.infinity);
        Button plusMinus = findViewById(R.id.plusMinus);
        TextView expression = findViewById(R.id.expression);
        expression.setMovementMethod(new ScrollingMovementMethod());
        current.setMovementMethod(new ScrollingMovementMethod());
        mDetector = new GestureDetector(this, new MyGestureListener());

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };
        current.setOnTouchListener(touchListener);

        View.OnClickListener onClickWrite = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView current = findViewById(R.id.current);
                    TextView expression = findViewById(R.id.expression);
                    if (currentNode != null && !currentNode.isTerminal) {
                        try {
                            expressionTree.addNode(currentNode);
                        } catch (Exception e) {
                            Log.e("", "hey", e);
                        }
                        setExpression(expression, expressionTree);
                        currentNode = null;
                        current.setText("");
                    }
                    char symb;
                    switch (v.getId()) {
                        case R.id.num1:
                            symb = '1';
                            break;
                        case R.id.num2:
                            symb = ('2');
                            break;
                        case R.id.num3:
                            symb = ('3');
                            break;
                        case R.id.num4:
                            symb = ('4');
                            break;
                        case R.id.num5:
                            symb = ('5');
                            break;
                        case R.id.num6:
                            symb = ('6');
                            break;
                        case R.id.num7:
                            symb = ('7');
                            break;
                        case R.id.num8:
                            symb = ('8');
                            break;
                        case R.id.num9:
                            symb = ('9');
                            break;
                        case R.id.num0:
                            symb = ('0');
                            break;

                        case R.id.point:
                            symb = ('.');
                            break;
                        case R.id.ellipsis:
                            symb = UNCERTANITY_CHAR;//ELLIPSIS_CHAR;
                            break;
                        case R.id.plusMinus:
                            symb = PLUS_MINUS;
                            break;
                        case R.id.infinity:
                            symb = INFINITY_CHAR;
                            break;
                        default:
                            symb = ' ';

                    }
                    current.setText(current.getText().toString() + symb);
                    currentInt = new KahInterval(current.getText().toString());
                    currentNode = new Node(currentInt);
                } catch (Exception e) {
                    Log.e("EXCEPTION", "something went wrong1");
                }
            }
        };
        View.OnClickListener onClickAct = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView current = findViewById(R.id.current);
                    TextView expression = findViewById(R.id.expression);
                    if (currentNode != null) {
                        currentNode = new Node(new KahInterval(current.getText().toString()));
                        expressionTree.addNode(currentNode);
                    }
                    setExpression(expression, expressionTree);
                    switch (v.getId()) {
                        case R.id.add:
                            currentNode = new Node(Node.ADD);
                            break;
                        case R.id.sub:
                            currentNode = new Node(Node.SUB);
                            break;
                        case R.id.mult:
                            currentNode = new Node(Node.MULT);
                            break;
                        case R.id.div:
                            currentNode = new Node(Node.DIV);
                            break;
                    }
                    current.setText(currentNode.getOperator().string);
                } catch (Exception e) {
                    Log.e("EXCEPTION", "something went wrong2");
                }
            }
        };
        View.OnClickListener onClickBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView current = findViewById(R.id.current);
                    if (currentNode != null && !currentNode.isTerminal) {
                        currentNode = null;
                        current.setText("");
                    } else {


                        try {
                            current.setText(current.getText().subSequence(0, current.getText().length() - 1));

                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    Log.e("EXCEPTION", "something went wrong3");
                }
            }
        };
        View.OnClickListener onClickCalc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView expression = findViewById(R.id.expression);
                    //currentNode= new Node(new KahInterval(current.getText().toString()));
                    expressionTree.addNode(currentNode);
                    currentNode = null;
                    currentInt = expressionTree.execute();
                    setCurrentIntervalText(currentInt);
                    setExpression(expression, expressionTree);
                    //currentNode=new Node(currentInt);
                } catch (Exception e) {
                    Log.e("EXCEPTION", "something went wrong4");
                }
            }
        };
        eq.setOnClickListener(onClickCalc);
        num1.setOnClickListener(onClickWrite);
        num2.setOnClickListener(onClickWrite);
        num3.setOnClickListener(onClickWrite);
        num4.setOnClickListener(onClickWrite);
        num5.setOnClickListener(onClickWrite);
        num6.setOnClickListener(onClickWrite);
        num7.setOnClickListener(onClickWrite);
        num8.setOnClickListener(onClickWrite);
        num9.setOnClickListener(onClickWrite);
        num0.setOnClickListener(onClickWrite);
        div.setOnClickListener(onClickAct);
        mult.setOnClickListener(onClickAct);
        add.setOnClickListener(onClickAct);
        sub.setOnClickListener(onClickAct);
        point.setOnClickListener(onClickWrite);
        back.setOnClickListener(onClickBack);
        ellipsis.setOnClickListener(onClickWrite);
        infinity.setOnClickListener(onClickWrite);
        plusMinus.setOnClickListener(onClickWrite);

    }

    public void setCurrentIntervalText(KahInterval ki) {//0- строка с многоточием 1- +- 2- интервал
        TextView current = findViewById(R.id.current);
        if (intervalForm < 0) {
            intervalForm = 0;
        }
        if (intervalForm > 2) {
            intervalForm = 2;
        }
        switch (intervalForm) {
            case 0:
                current.setText(ki.toStringEllipsis());
                break;
            case 1:
                current.setText(ki.toStringError());
                break;
            case 2:
                current.setText(ki.toString());
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //реагирует на свайп влево и вправо меняет формат вывода интервалов
            if (Math.abs(velocityX) > 5) {
                if (velocityX > 0) {
                    intervalForm--;
                    setCurrentIntervalText(currentInt);
                } else {
                    intervalForm++;
                    setCurrentIntervalText(currentInt);
                }
                return true;
            }
            return false;
        }
    }

    public boolean setExpression(TextView textView, Tree expr) {
        //выводит математическое выражение в textView сворачивая некоторые подвыражения если строка не помещается полностью
        try {

            String s = "";
            for (int i = 0; i <= expr.getRoot().getLevel(); i++) {
                s = expr.toString(i);
                if (!isTooLarge(textView, s)) {
                    textView.setText(s);
                    return true;

                }
            }
            textView.setText(s);
            return false;
        } catch (Exception e) {
            Log.e("", "hhhhhh", e);
        }
        return false;
    }

    public boolean isTooLarge(TextView text, String newText) {//проверяет поместится ли строка полоностью в textView
        float textWidth = text.getPaint().measureText(newText);
        return (textWidth >= text.getMeasuredWidth());
    }

   /* public boolean isOperator(String s) {
        if (s.equals(Node.ADD.string) || s.equals(Node.SUB.string) ||
                s.equals(Node.MULT.string) || s.equals(Node.DIV.string)) {
            return true;
        }
        return false;

    }
    /*public void setIntervalText(Interval i) {
        TextView ellipsis = findViewById(R.id.ellipsisNumber);
        TextView inter = findViewById(R.id.Interval);
        TextView error = findViewById(R.id.errorNumber);
        ellipsis.setText(i.toStringEllipsis());
        inter.setText(i.toString());
        error.setText(i.toStringError());
    }


    public void setKahIntervalText(KahInterval ki) {
        TextView ellipsis = findViewById(R.id.ellipsisNumber);
        TextView inter = findViewById(R.id.Interval);
        TextView error = findViewById(R.id.errorNumber);
        ellipsis.setText(ki.toStringEllipsis());
        inter.setText(ki.toString());
        error.setText(ki.toStringError());
    }
*/


}
