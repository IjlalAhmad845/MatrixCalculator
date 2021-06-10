package com.example.navigation;

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

        if(!expression.equals("") && !EE.Convert(expression).equals("Not valid")&& !EE.Convert(expression).equals("Decimal Mistake")) {
            expression=EE.Convert(expression);
            instance.messageTextviewList.get(outputCardIndex).setText(expression);

            for(int i=0;i<expression.length();i++){
                char c=expression.charAt(i);

                if(((int)c>=97 && (int)c<=123) || (int)c >= 65 && (int)c <= 91){
                    stringStack.push(c);
                }
                else {
                    char c1 = stringStack.pop();
                    char c2 = stringStack.pop();

                    //if both chars are lowercase or from lowercase chars list
                    if ((Character.isLowerCase(c1) || ccList.contains(c1)) && (Character.isLowerCase(c2) || ccList.contains(c2)))
                        switch (c) {
                            case '+':
                                EE.charMap.put(ccList.get(i), EE.charMap.get(c2) + EE.charMap.get(c1));
                                stringStack.push(ccList.get(i));
                                break;
                            case '-':
                                EE.charMap.put(ccList.get(i), EE.charMap.get(c2) - EE.charMap.get(c1));
                                stringStack.push(ccList.get(i));
                                break;
                            case '·':
                                EE.charMap.put(ccList.get(i), EE.charMap.get(c2) * EE.charMap.get(c1));
                                stringStack.push(ccList.get(i));
                                break;
                        }
                        //if 1st char is uppercase or from uppercase list and 2nd char is lowercase or from lowercase list
                    else if ((Character.isUpperCase(c1) || mmList.contains(c1)) && (Character.isLowerCase(c2) || ccList.contains(c2)))
                        switch (c) {
                            case '+':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Added in Matrices ");
                                break;
                            case '-':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Subtracted From Matrices ");
                                break;
                            case '·':
                                EE.matrixMap.put(mmList.get(i), ScalarMultiply(EE.matrixMap.get(c1), EE.charMap.get(c2)));//but char will be extracted from charmap
                                stringStack.push(mmList.get(i));
                                break;
                        }

                        //if 2nd char is uppercase or from uppercase list and 1st char is lowercase or from lowercase list
                    else if((Character.isLowerCase(c1) || ccList.contains(c1)) && (Character.isUpperCase(c2) || mmList.contains(c2)))
                        switch (c) {
                            case '+':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Added in Matrices ");
                                break;
                            case '-':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Subtracted From Matrices ");
                                break;
                            case '·':
                                EE.matrixMap.put(mmList.get(i), ScalarMultiply(EE.matrixMap.get(c2), EE.charMap.get(c1)));//but char will be extracted from charmap
                                stringStack.push(mmList.get(i));
                                break;
                        }

                        //if both chars are uppercase or from uppercase chars list
                    else if((Character.isUpperCase(c1) || mmList.contains(c1)) && (Character.isUpperCase(c2) || mmList.contains(c2)))
                        switch (c) {
                            case '+':
                                EE.matrixMap.put(mmList.get(i), addMatrix(EE.matrixMap.get(c2), EE.matrixMap.get(c1)));//but char will be extracted from charmap
                                stringStack.push(mmList.get(i));
                                break;
                            case '-':
                                EE.matrixMap.put(mmList.get(i), subtractMatrix(EE.matrixMap.get(c2), EE.matrixMap.get(c1)));//but char will be extracted from charmap
                                stringStack.push(mmList.get(i));
                                break;
                            case '·':                                                               //matrices will be fetched in a reverse order for multiplication
                                EE.matrixMap.put(mmList.get(i), multiplyMatrix(EE.matrixMap.get(c1), EE.matrixMap.get(c2)));//but char will be extracted from charmap
                                stringStack.push(mmList.get(i));
                                break;
                        }
                    }
            }
            //loop ended AND the final result is present in STACK

            //condition for all the chars present in the expression
            if(stringStack.size()>0 && (Character.isLowerCase(stringStack.elementAt(stringStack.size()-1)) || ccList.contains(stringStack.elementAt(stringStack.size()-1)))){

                //hiding all the elements of matrix for fresh printing
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
                instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setVisibility(View.VISIBLE);

                //typecasting to remove '.' in non decimal values
                double result=EE.charMap.get(stringStack.pop());

                result=Math.round(result*100.0)/100.0;

                if(result==(int)result)
                instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setText((int)result+"");
                else
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(0).get(0).setText(result+"");
            }

            //condition if any matrix is present in expression
            else if(stringStack.size()>0 && (Character.isUpperCase(stringStack.elementAt(stringStack.size()-1)) || mmList.contains(stringStack.elementAt(stringStack.size()-1)))){

                //extracted matrix from hashmap by last stack element
                ArrayList<ArrayList<Double>> A=EE.matrixMap.get(stringStack.pop());

                //hiding all the elements of matrix for fresh printing
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
                double result;

                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++) {
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.VISIBLE);

                        //typecasting to remove '.' in non decimal values
                        result=A.get(i).get(j);
                        result=Math.round(result*100.0)/100.0;

                        if(result==(int)result)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setText((int)result+"");
                        else instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setText(result+"");
                    }
                }
            }

            }
        //for hiding output and msg box when expression is empty
        else if(expression.equals("")){
            instance.messageTextviewList.get(outputCardIndex).setText("");

            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++) {
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
            }

        }

        //for hiding output when expression is not valid
        else{
            instance.messageTextviewList.get(outputCardIndex).setText(EE.Convert(expression));

            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++) {
                    instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
            }
        }

    }

    /**============================================ METHOD FOR SCALAR MULTIPLY OF MATRIX =============================================================**/
    public ArrayList<ArrayList<Double>> ScalarMultiply(ArrayList<ArrayList<Double>> A, double scalar){

        for(int i=0;i<A.size();i++){
            for(int j=0;j<A.size();j++){
                A.get(i).set(j,A.get(i).get(j)*scalar);
            }
        }

        return A;
    }

    private ArrayList<ArrayList<Double>> addMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B) {
        for(int i=0;i<A.size();i++){
            for(int j=0;j<A.size();j++){
                A.get(i).set(j,A.get(i).get(j)+B.get(i).get(j));
            }
        }

        return A;
    }

    private ArrayList<ArrayList<Double>> subtractMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B) {
        for(int i=0;i<A.size();i++){
            for(int j=0;j<A.size();j++){
                A.get(i).set(j,A.get(i).get(j)-B.get(i).get(j));
            }
        }

        return A;
    }

    private ArrayList<ArrayList<Double>> multiplyMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B) {
        ArrayList<ArrayList<Double>> C=new ArrayList<>();

        for(int i=0;i<A.size();i++){
            C.add(new ArrayList<>());

            for(int j=0;j<A.size();j++){

                C.get(i).add(0.0);
                for(int k=0;k<A.size();k++)
                    C.get(i).set(j,A.get(i).get(k)*B.get(k).get(j)+C.get(i).get(j));
            }
        }

        return C;
    }
}
