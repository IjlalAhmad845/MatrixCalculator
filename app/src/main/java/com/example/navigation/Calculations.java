package com.example.navigation;

import android.util.Log;
import java.util.Stack;

public class Calculations {

    MainActivity instance;
    ExpressionEvaluator EE=new ExpressionEvaluator();

    int outputCardIndex;
    String expression;
    char[] cc=new char[60];

    Stack<Character> stringStack=new Stack<>();

    Calculations() {
        this.instance = MainActivity.getInstance();
    }

    public void setCc() {
        for(int i=0;i<60;i++)
            cc[i]=(char)(i+130);
    }

    public void setOutputCardIndex(int outputCardIndex) {
        this.outputCardIndex = outputCardIndex;
    }

    public void masterControlFunction(){
        setCc();
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

                    switch (c){
                        case '+':
                            EE.map.put(cc[i],EE.map.get(c2)+EE.map.get(c1));
                            stringStack.push(cc[i]);
                            break;
                        case '-':EE.map.put(cc[i],EE.map.get(c2)-EE.map.get(c1));
                            stringStack.push(cc[i]);
                            break;
                        case '·':EE.map.put(cc[i],EE.map.get(c2)*EE.map.get(c1));
                            stringStack.push(cc[i]);
                            break;
                    }
                }
            }
            Log.d("map", EE.map.get(stringStack.pop())+"");
            }
        else
                instance.messageTextviewList.get(outputCardIndex).setText(EE.Convert(expression));

    }
}
