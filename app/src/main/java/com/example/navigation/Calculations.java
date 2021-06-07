package com.example.navigation;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

public class Calculations {

    MainActivity instance;
    ExpressionEvaluator EE=new ExpressionEvaluator();

    int outputCardIndex;
    String expression;
    ArrayList<Character> ccList=new ArrayList<>();
    ArrayList<Character> mmList=new ArrayList<>();

    Stack<Character> stringStack=new Stack<>();

    Calculations() {
        this.instance = MainActivity.getInstance();
    }

    public void setCc() {
        for(int i=0;i<40;i++){
           // System.out.println((char)(i+130));
            ccList.add((char)(i+130));
        }
    }

    public void setMm() {
        for(int i=0;i<40;i++) {
           // System.out.println((char) (i + 170));
            mmList.add((char) (i + 170));
        }
    }

    public void setOutputCardIndex(int outputCardIndex) {
        this.outputCardIndex = outputCardIndex;
    }

    public void masterControlFunction(){
        setCc();setMm();
        stringStack.clear();
        expression=instance.editTextList.get(outputCardIndex).getText().toString();

        if(!expression.equals("") && !EE.Convert(expression).equals("Not valid")) {
            expression=EE.Convert(expression);
            instance.messageTextviewList.get(outputCardIndex).setText(expression);

            for(int i=0;i<expression.length();i++){
                char c=expression.charAt(i);

                if(((int)c>=97 && (int)c<=123) || (int)c >= 65 && (int)c <= 91){
                    stringStack.push(c);
                }
                else {
                    char c1=stringStack.pop();
                    char c2=stringStack.pop();

                    if(!Character.isUpperCase(c1) && !Character.isUpperCase(c2))
                    switch (c){
                        case '+':
                            EE.map.put(ccList.get(i),EE.map.get(c2)+EE.map.get(c1));
                            stringStack.push(ccList.get(i));
                            break;
                        case '-':EE.map.put(ccList.get(i),EE.map.get(c2)-EE.map.get(c1));
                            stringStack.push(ccList.get(i));
                            break;
                        case '·':EE.map.put(ccList.get(i),EE.map.get(c2)*EE.map.get(c1));
                            stringStack.push(ccList.get(i));
                            break;
                    }
                }
            }
            if(Character.isLowerCase(stringStack.elementAt(stringStack.size()-1)) || ccList.contains(stringStack.elementAt(stringStack.size()-1))){
                Log.d("map", EE.map.get(stringStack.elementAt(stringStack.size()-1))+"");
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
                instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setVisibility(View.VISIBLE);

                //typecasting to remove '.' in non decimal values
                double result=EE.map.get(stringStack.pop());

                result=Math.round(result*100.0)/100.0;

                if(result!=(int)result)
                instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setText(result+"");
                else
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setText((int)result+"");
            }

            }
        else if(expression.equals("")){
            instance.messageTextviewList.get(outputCardIndex).setText("");
            instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setText("");
        }

        else
            instance.messageTextviewList.get(outputCardIndex).setText(EE.Convert(expression));
    }
}
