package com.example.navigation;

import android.view.View;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.TimeoutException;

public class Calculations {

    MainActivity instance;
    ExpressionEvaluator EE=new ExpressionEvaluator();

    int outputCardIndex;
    String expression;
    ArrayList<Character> ccList=new ArrayList<>();
    ArrayList<Character> mmList=new ArrayList<>();
    ArrayList<ArrayList<Double>> Temp =new ArrayList<>();
    int rows=0,cols=0;
    boolean error=false;

    Stack<Character> stringStack=new Stack<>();

    Calculations() {
        this.instance = MainActivity.getInstance();
    }

    public void setCc() {
        ccList.clear();
        for(int i=0;i<55;i++){
            ccList.add((char)(i+130));
        }
    }

    public void setMm() {
        mmList.clear();
        for(int i=0;i<55;i++) {
            mmList.add((char) (i + 185));
        }
    }

    public void setOutputCardIndex(int outputCardIndex) {
        this.outputCardIndex = outputCardIndex;
    }

    public void masterControlFunction(){error=false;
        setCc();setMm();
        stringStack.clear();
        expression=instance.editTextList.get(outputCardIndex).getText().toString();

        //All the errors at one place are checked and displayed after anyone is encountered
        if(!expression.equals("") &&  !EE.Convert(expression).equals("Decimal Mistake") && !EE.Convert(expression).equals("Invalid")  &&
                !EE.Convert(expression).equals("Empty Brackets") && (EE.Convert(expression).length()<6 || !EE.Convert(expression).substring(0,6).equals("Matrix")) &&
                !EE.Convert(expression).equals("Expression too long to evaluate") && !EE.Convert(expression).equals("Brackets Mistake") &&
                !EE.Convert(expression).equals("Operator Mistake")) {

            expression=EE.Convert(expression);
            instance.messageTextviewList.get(outputCardIndex).setText(expression);

            for(int i=0;i<expression.length();i++){
                char c=expression.charAt(i);

                if(((int)c>=97 && (int)c<=123) || (int)c >= 65 && (int)c <= 91){
                    stringStack.push(c);
                }

                else if(c=='#' || c=='~'){
                    char c1 = 0;
                    //Error handling of empty function brackets
                    try{
                         c1 = stringStack.pop();
                    }catch(Exception e){
                        error = true;
                        instance.messageTextviewList.get(outputCardIndex).setText("Invalid");
                    }

                    if(!error && (Character.isUpperCase(c1) || mmList.contains(c1)))
                    switch (c){
                        case '#':
                            System.out.println(EE.matrixRowsMap.get(c1));
                            if(Objects.equals(EE.matrixRowsMap.get(c1), EE.matrixColsMap.get(c1)))
                            EE.charMap.put(ccList.get(i),detN(EE.matrixMap.get(c1),EE.matrixRowsMap.get(c1)));
                            else {
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Square Matrices are applicable for determinant");
                                error=true;
                            }

                            stringStack.push(ccList.get(i));
                            break;

                        case '~':
                            Temp=transpose(EE.matrixMap.get(c1),EE.matrixRowsMap.get(c1),EE.matrixColsMap.get(c1));
                            EE.matrixMap.put(mmList.get(i),Temp);

                            if (Temp.size() > 0) {
                                EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                EE.matrixColsMap.put(mmList.get(i), Temp.size());
                            }

                            stringStack.push(mmList.get(i));
                            break;
                    }
                    else {
                        instance.messageTextviewList.get(outputCardIndex).setText("Function not applicable on scalars");
                        error=true;
                    }
                    if(error)break;
                }
                else {
                    char c1 = stringStack.pop();
                    char c2 = stringStack.pop();

                    //if both chars are lowercase or from lowercase chars list
                    if ((Character.isLowerCase(c1) || ccList.contains(c1)) && (Character.isLowerCase(c2) || ccList.contains(c2))) {
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
                        if(error)break;
                    }
                        //if 1st char is uppercase or from uppercase list and 2nd char is lowercase or from lowercase list
                    else if ((Character.isUpperCase(c1) || mmList.contains(c1)) && (Character.isLowerCase(c2) || ccList.contains(c2))) {
                        switch (c) {
                            case '+':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Added in Matrices ");
                                error=true;
                                break;
                            case '-':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Subtracted From Matrices ");
                                error=true;
                                break;
                            case '·':
                                Temp = ScalarMultiply(EE.matrixMap.get(c1), EE.charMap.get(c2), EE.matrixRowsMap.get(c1), EE.matrixColsMap.get(c1));
                                EE.matrixMap.put(mmList.get(i), Temp);//but char will be extracted from charmap

                                //every time a new matrix is formed its dimensions must be stored
                                if (Temp.size() > 0) {
                                    EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                    EE.matrixColsMap.put(mmList.get(i), Temp.size());
                                }

                                stringStack.push(mmList.get(i));
                                break;
                        }
                        if(error)break;
                    }

                        //if 2nd char is uppercase or from uppercase list and 1st char is lowercase or from lowercase list
                    else if((Character.isLowerCase(c1) || ccList.contains(c1)) && (Character.isUpperCase(c2) || mmList.contains(c2))) {
                        switch (c) {
                            case '+':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Added in Matrices ");
                                error=true;
                                break;
                            case '-':
                                instance.messageTextviewList.get(outputCardIndex).setText("Only Matrices will be Subtracted From Matrices ");
                                error=true;
                                break;
                            case '·':
                                Temp = ScalarMultiply(EE.matrixMap.get(c2), EE.charMap.get(c1), EE.matrixRowsMap.get(c2), EE.matrixColsMap.get(c2));
                                EE.matrixMap.put(mmList.get(i), Temp);//but char will be extracted from charmap

                                //every time a new matrix is formed its dimensions must be stored
                                if (Temp.size() > 0) {
                                    EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                    EE.matrixColsMap.put(mmList.get(i), Temp.size());
                                }

                                stringStack.push(mmList.get(i));
                                break;
                        }
                    }

                        //if both chars are uppercase or from uppercase chars list
                    else if((Character.isUpperCase(c1) || mmList.contains(c1)) && (Character.isUpperCase(c2) || mmList.contains(c2))) {
                        switch (c) {
                            case '+':                                                       //but char will be extracted from charmap
                                Temp = addMatrix(EE.matrixMap.get(c2), EE.matrixMap.get(c1), EE.matrixRowsMap.get(c2), EE.matrixColsMap.get(c2), EE.matrixRowsMap.get(c1), EE.matrixColsMap.get(c1));
                                EE.matrixMap.put(mmList.get(i), Temp);

                                //every time a new matrix is formed its dimensions must be stored
                                if (Temp.size() > 0) {
                                    EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                    EE.matrixColsMap.put(mmList.get(i), Temp.size());
                                }

                                stringStack.push(mmList.get(i));
                                break;
                            case '-':                                                            //but char will be extracted from charmap
                                Temp = subtractMatrix(EE.matrixMap.get(c2), EE.matrixMap.get(c1), EE.matrixRowsMap.get(c2), EE.matrixColsMap.get(c2), EE.matrixRowsMap.get(c1), EE.matrixColsMap.get(c1));
                                EE.matrixMap.put(mmList.get(i), Temp);

                                //every time a new matrix is formed its dimensions must be stored
                                if (Temp.size() > 0) {
                                    EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                    EE.matrixColsMap.put(mmList.get(i), Temp.size());
                                }

                                stringStack.push(mmList.get(i));
                                break;
                            case '·':
                                //for handling exception in case when there is already error in Multiplication
                                if (EE.matrixMap.get(c1).size() > 0 && EE.matrixMap.get(c2).size() > 0)
                                    //matrices will be fetched in a reverse order for multiplication
                                    Temp = multiplyMatrix(EE.matrixMap.get(c1), EE.matrixMap.get(c2), EE.matrixRowsMap.get(c2), EE.matrixColsMap.get(c2), EE.matrixRowsMap.get(c1), EE.matrixColsMap.get(c1));//but char will be extracted from charmap
                                EE.matrixMap.put(mmList.get(i), Temp);

                                //every time a new matrix is formed its dimensions must be stored
                                if (Temp.size() > 0) {
                                    EE.matrixRowsMap.put(mmList.get(i), Temp.get(0).size());
                                    EE.matrixColsMap.put(mmList.get(i), Temp.size());
                                }
                                System.out.println(EE.matrixRowsMap.size());

                                stringStack.push(mmList.get(i));
                                break;
                        }
                        if(error)break;
                    }
                    }
            }
            //loop ended AND the final result is present in STACK

            //condition for all the chars present in the expression
            if(!error && stringStack.size()>0 && (Character.isLowerCase(stringStack.elementAt(stringStack.size()-1)) || ccList.contains(stringStack.elementAt(stringStack.size()-1)))){

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
            else if(!error && stringStack.size()>0 && (Character.isUpperCase(stringStack.elementAt(stringStack.size()-1)) || mmList.contains(stringStack.elementAt(stringStack.size()-1)))){
                cols=rows=0;

                //extracted matrix from hashmap by last stack element
                Temp=EE.matrixMap.get(stringStack.pop());

                //if only single matrix is present in the expression field then it will show its original dimensions at first time
                if(expression.length()==1){
                    rows=instance.matrixRows.get(instance.matrixNamesStringList.indexOf(expression));
                    cols=instance.matrixCols.get(instance.matrixNamesStringList.indexOf(expression));
                }
                else if(Temp.size()>0){
                    cols=Temp.size();
                    rows=Temp.get(0).size();
                }

                //hiding all the elements of matrix for fresh printing
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
                }
                double result;
                for(int i=0;i<cols;i++){
                    for(int j=0;j<rows;j++) {
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.VISIBLE);

                        //typecasting to remove '.' in non decimal values
                        result=Temp.get(i).get(j);
                        result=Math.round(result*100.0)/100.0;

                        if(result==(int)result)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setText((int)result+"");
                        else instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setText(result+"");
                    }
                }
            }
            else if(error){
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++)
                        instance.matrixOutputTextviewList.get(outputCardIndex).get(i).get(j).setVisibility(View.GONE);
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
    public ArrayList<ArrayList<Double>> ScalarMultiply(ArrayList<ArrayList<Double>> A, double scalar,int rows,int cols){
        ArrayList<ArrayList<Double>> B=new ArrayList<>();

        for(int i=0;i<cols;i++){
            B.add(new ArrayList<>());
            for(int j=0;j<rows;j++){
                B.get(i).add(A.get(i).get(j)*scalar);
            }
        }

        return B;
    }

    /**============================================ METHOD FOR ADDING TWO MATRICES =============================================================**/
    private ArrayList<ArrayList<Double>> addMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B,int rows1,int cols1,int rows2,int cols2) {
        ArrayList<ArrayList<Double>> C=new ArrayList<>();

        if(rows1==rows2 && cols1==cols2)
        for(int i=0;i<cols1;i++){
            C.add(new ArrayList<>());
            for(int j=0;j<rows1;j++){
                C.get(i).add(A.get(i).get(j)+B.get(i).get(j));
            }
        }
        else {
            instance.messageTextviewList.get(outputCardIndex).setText("Matrix Order must be same for Addition");
            error=true;
        }

        return C;
    }

    /**============================================ METHOD FOR SUBTRACTING TWO MATRICES =============================================================**/
    private ArrayList<ArrayList<Double>> subtractMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B,int rows1,int cols1,int rows2,int cols2) {
        ArrayList<ArrayList<Double>> C=new ArrayList<>();

        if(rows1==rows2 && cols1==cols2)
        for(int i=0;i<cols1;i++){
            C.add(new ArrayList<>());
            for(int j=0;j<rows1;j++){
                C.get(i).add(A.get(i).get(j)-B.get(i).get(j));
            }
        }
        else {
            instance.messageTextviewList.get(outputCardIndex).setText("Matrix Order must be same for Subtraction");
            error=true;
        }

        return C;
    }

    /**============================================ METHOD FOR MULTIPLYING TWO MATRICES =============================================================**/
    private ArrayList<ArrayList<Double>> multiplyMatrix(ArrayList<ArrayList<Double>> A, ArrayList<ArrayList<Double>> B,int rows1,int cols1,int rows2,int cols2) {
        ArrayList<ArrayList<Double>> C=new ArrayList<>();

        if(cols1==rows2)
        for(int i=0;i<cols2;i++){
            C.add(new ArrayList<>());

            for(int j=0;j<rows1;j++){

                C.get(i).add(0.0);
                for(int k=0;k<cols1;k++)
                    C.get(i).set(j,A.get(i).get(k)*B.get(k).get(j)+C.get(i).get(j));
            }
        }
        else {
            instance.messageTextviewList.get(outputCardIndex).setText("Matrices with incompatible Dimensions");
            error=true;
        }

        return C;
    }

    /**========================================== METHODS FOR CALCULATING DETERMINANT OF MATRIX =========================================================**/
    public static Double det2(ArrayList<ArrayList<Double>> A){
        return A.get(0).get(0)*A.get(1).get(1)-A.get(1).get(0)*A.get(0).get(1);
    }

    public static Double detN(ArrayList<ArrayList<Double>> A,int n){
        double result=0.0;
        if(n>=3){
            int i1=0,j1=0;

            ArrayList<ArrayList<Double>> B=new ArrayList<>();

            for(int i=0;i<n-1;i++){
                B.add(new ArrayList<>());
                for(int j=0;j<n-1;j++)
                    B.get(i).add(0.0);
            }

            for(int itr=0;itr<n;itr++){
                for(int i=0;i<n;i++){
                    for(int j=0;j<n;j++)
                        if(i!=0 && j!=itr){
                            B.get(i1).set(j1,A.get(i).get(j));
                            if(j1==n-2){
                                if(i1==n-2)i1=0;
                                else i1++;
                                j1=0;
                            }
                            else j1++;
                        }
                }
                result+=A.get(0).get(itr)*Math.pow(-1,itr)*detN(B,B.size());
            }
            B.clear();
        }
        else if(n==2)result=det2(A);
        else result=A.get(0).get(0);

        return result;
    }

    /**==================================================== METHOD FOR TRANSPOSE OF MATRIX =====================================================**/
    public ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> A,int rows, int cols){

        ArrayList<ArrayList<Double>> B=new ArrayList<>();

        for(int i=0;i<rows;i++){
            B.add(new ArrayList<>());
            for(int j=0;j<cols;j++)
                B.get(i).add(A.get(j).get(i));
        }
        return B;
    }
}
