package com.example.navigation;

import android.os.PowerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class ExpressionEvaluator {
    MainActivity instance;

    ExpressionEvaluator(){this.instance = MainActivity.getInstance();}
    HashMap<Character,Double> charMap =new HashMap<>();
    HashMap<Character,ArrayList<ArrayList<Double>>> matrixMap=new HashMap<>();
    HashMap<Character,Integer> matrixColsMap=new HashMap<>();
    HashMap<Character,Integer> matrixRowsMap=new HashMap<>();
    ArrayList<String> powerList=new ArrayList<>();

    /**============================================== FOR REPLACING NUMBERS TO ALPHABETS ==========================================**/

    public String Convert(String str){
        charMap.clear();
        matrixMap.clear();
        matrixRowsMap.clear();
        matrixColsMap.clear();
        powerList.clear();

        if(str.length()==1 && Character.isLowerCase(str.charAt(0)))
            return "Invalid";
        if(str.length()<=6 && str.contains(","))
            return "Invalid";

        str=str+";;;";
        String s="";
        ArrayList<Double> numList=new ArrayList<>();
        for(int i=0;i<str.length();i++){

            //replacing Determinant String
            if(str.contains("det")){
                str=str.replace("det","#");
                if( str.charAt(str.indexOf("#")+2)==')')
                    return "Empty Brackets";
            }

            //replacing Transpose String
            if(str.contains("tps")){
                str=str.replace("tps","~");
                if( str.charAt(str.indexOf("~")+2)==')')
                    return "Empty Brackets";
            }

            //replacing power String
            if(str.contains("pow")){

                for(int j=1;str.contains("pow");j++){
                    if(Character.isDigit(str.charAt(str.indexOf(",")+1))){
                        powerList.add(String.valueOf(str.charAt(str.indexOf(",")+1)));
                        if(Character.isDigit(str.charAt(str.indexOf(",")+2)))
                            powerList.set(powerList.size()-1,powerList.get(powerList.size()-1)+String.valueOf(str.charAt(str.indexOf(",")+2)));
                    }


                    if(powerList.size()==j) {
                        if(Integer.parseInt(powerList.get(powerList.size()-1))<=15){
                            str = str.replaceFirst("pow", "^");
                            str = str.replaceFirst("," + powerList.get(powerList.size() - 1), "");
                        }
                        else return "Power too large";
                    }
                    else return "Invalid Power";
                }

            }


            //Storing matrices in Hashmap
            if(Character.isUpperCase(str.charAt(i))){
                if(returnCurrentMatrix(String.valueOf(str.charAt(i))).size()==0)
                    return "Matrix '"+str.charAt(i)+"' doesn't Exist";
                else {
                    matrixMap.put(str.charAt(i), returnCurrentMatrix(String.valueOf(str.charAt(i))));
                    matrixRowsMap.put(str.charAt(i), returnCurrentMatRows(String.valueOf(str.charAt(i))));
                    matrixColsMap.put(str.charAt(i), returnCurrentMatCols(String.valueOf(str.charAt(i))));
                }
            }

            //replacing digits
            if(Character.isDigit(str.charAt(i)) || str.charAt(i)=='.')
                s=s+str.charAt(i);

            //Stored all the matrices into separate hashmap
            else {
                if(!s.equals("")){
                    if(!s.contains(".")) {
                        str = str.substring(0, i) + ".0" + str.substring(i);
                        s += ".0";
                        i += 2;
                    }
                    //checking if incoming numbers does not have value before and after '.'
                    else if(s.indexOf(".")==0 || s.indexOf(".")==s.length()-1) {
                            return "Decimal Mistake";
                    }
                    try {
                        numList.add(Double.parseDouble(s));
                    } catch (Exception e) {
                        return "Decimal Mistake";
                    }
                }
                s="";
            }
        }
        Collections.sort(numList);
        Collections.reverse(numList);

        for(int i=0;i<numList.size();i++){
            charMap.put((char)(i+97),numList.get(i));
            str=str.replace(String.valueOf(numList.get(i)),String.valueOf((char)(i+97)));
        }

        str=str.substring(0,str.length()-3);
        //System.out.println(matrixMap.size());

        if(charMap.size()>26)
            return "Expression too long to evaluate";

        else if(Valid_Arithmetic(str,str.length())==0)
            return  infixToPostfix(str);
        else if(Valid_Arithmetic(str,str.length())==1)
            return "Brackets Mistake";
        else if(Valid_Arithmetic(str,str.length())==2)
            return "Invalid";
        else if(Valid_Arithmetic(str,str.length())==3)
            return "Operator Mistake";
        else return "Invalid";
    }

    public ArrayList<ArrayList<Double>> returnCurrentMatrix(String MatName){
            ArrayList<ArrayList<Double>> currentMat=new ArrayList<>();

            //returning null matrix if the corresponding matrix don't exist
            if(!instance.matrixNamesStringList.contains(MatName))
                return currentMat;

            for(int i=0;i<5;i++){
                currentMat.add(new ArrayList<>());
                for(int j=0;j<5;j++){
                    currentMat.get(i).add(Double.valueOf(instance.matrixPreviewTextviewList.get(instance.matrixNamesStringList.indexOf(MatName)).get(i).get(j).getText().toString()));
                }
            }
            return currentMat;
    }

    public int returnCurrentMatRows(String MatName){
            return instance.matrixRows.get(instance.matrixNamesStringList.indexOf(MatName));
    }
    public int returnCurrentMatCols(String MatName){
        return instance.matrixCols.get(instance.matrixNamesStringList.indexOf(MatName));
    }

    /**============================================== FOR CHECKING VALIDITY OF EXPRESSION ==========================================**/
    public int Valid_Arithmetic(String str, int len)
    {
        if (Check_Parenthesis(str, len)) // like "())" and more
        {
            return 1;
        }
        if (Check_For_Invalidity(str, len)) // like "a(" and "a-)" and ")a" and "ab"
        {
            return 2;
        }
        // Operator Are Always Less Than One Then The Operands
        if(!Check_Opeartor_Opearand(str, len))
            return 0;
        else
            return 3;
    }

    public static boolean Check_Opeartor_Opearand(String str, int len)
    {
        int OperatorCount = 0, OperandCount = 0;
        for (int i = 0; i < len; i++)
        {
            if (((int)str.charAt(i) >= 65 && (int)str.charAt(i) <= 91) || ((int)str.charAt(i) >= 97 && (int)str.charAt(i) <= 123))
                OperandCount++;
            if (str.charAt(i) == '-' || str.charAt(i) == '+' || str.charAt(i) == '·' || str.charAt(i) == '/')
                OperatorCount++;
        }
        return !(OperatorCount + 1 == OperandCount);
        // return 0 for correct output
    }

    public  boolean Check_For_Invalidity(String str, int len)
    {
        for (int i = 1; i < len; i++)
        {
            boolean a = ((int)str.charAt(i - 1) >= 65 && (int)str.charAt(i - 1) <= 91) || ((int)str.charAt(i - 1) >= 97 && (int)str.charAt(i - 1) <= 123);
            if (a && str.charAt(i) == '(')
                return true;
            else {
                boolean b = ((int)str.charAt(i) >= 65 && (int)str.charAt(i) <= 91) || ((int)str.charAt(i) >= 97 && (int)str.charAt(i) <= 123);
                if (str.charAt(i-1) == ')' && b)
                    return true;
                else {
                    boolean c = str.charAt(i - 1) == '-' || str.charAt(i - 1) == '+' || str.charAt(i - 1) == '·' || str.charAt(i - 1) == '/';
                    if (c && str.charAt(i) == ')')
                        return true;
                    else if (a && b)
                        return true;
                    else if (c && (str.charAt(i) == '-' || str.charAt(i) == '+' || str.charAt(i) == '·' ||str.charAt(i) == '/'))
                        return true;
                }
            }
        }
        return false; // return 0 for true arithmetics
    }

    public boolean Check_Parenthesis(String str, int len)
    {
        StringBuilder bracstr = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            if (str.charAt(i) == '(' || str.charAt(i) == ')')
            {
                bracstr.append(str.charAt(i));

            }
        }

        Stack<Character> st = new Stack<>();
        int n = bracstr.length();
        for (int i = 0; i < n; i++)
        {
            if (st.isEmpty() || bracstr.charAt(i) == '(')
                st.push(bracstr.charAt(i));
            else if (st.peek() == '(' && bracstr.toString().charAt(i) == ')')
                st.pop();
            else
                return true;
        }
        return !st.isEmpty(); // return ture for invalid
    }



    /**============================================== CONVERTING INFIX TO POSTFIX ==========================================**/
    // A utility function to return
    // precedence of a given operator
    // Higher returned value means
    // higher precedence
    static int Precedence(char ch)
    {
        switch (ch)
        {
            case '+':
            case '-':
                return 1;

            case '·':
            case '/':
                return 2;

            case '#':   //for determinant
            case '~':   //for transpose
            case '^':   //for nth power
            case '&': //for inverse
            case '$':  //for trace
            case '@': //for adjoint
            case '%': //for minors
            case '*': //for cofactors
                return 4;
        }
        return -1;
    }

    // The main method that converts
    // given infix expression
    // to postfix expression.
    public String infixToPostfix(String exp)
    {
        // initializing empty String for result
        StringBuilder result = new StringBuilder();

        // initializing empty stack
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i<exp.length(); ++i)
        {
            char c = exp.charAt(i);

            // If the scanned character is an
            // operand, add it to output.
            if (Character.isLetterOrDigit(c))
                result.append(c);

                // If the scanned character is an '(',
                // push it to the stack.
            else if (c == '(')
                stack.push(c);

                //  If the scanned character is an ')',
                // pop and output from the stack
                // until an '(' is encountered.
            else if (c == ')')
            {
                while (!stack.isEmpty() && stack.peek() != '(')
                    result.append(stack.pop());

                stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Precedence(c) <= Precedence(stack.peek())){
                    result.append(stack.pop());
                }
                stack.push(c);
            }

        }

        // pop all the operators from the stack
        while (!stack.isEmpty()){
            if(stack.peek() == '(')
                return "Invalid Expression";
            result.append(stack.pop());
        }
        return result.toString();
    }
}
