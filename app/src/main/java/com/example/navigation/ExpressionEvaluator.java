package com.example.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class ExpressionEvaluator {
    static HashMap<Integer,Character> map=new HashMap<>();

    /**============================================== FOR REPLACING NUMBERS TO ALPHABETS ==========================================**/
    public String Convert(String str){
        int num=0;
        str=str+".";
        boolean counter=false;
        ArrayList<Integer> numList=new ArrayList<>();
        for(int i=0;i<str.length();i++){
            if(Character.isDigit(str.charAt(i))){
                num=num*10+Integer.parseInt(String.valueOf(str.charAt(i)));
                counter=true;
            }
            else {
                if(counter)
                    numList.add(num);
                num=0;
                counter=false;
            }
        }
        Collections.sort(numList);
        Collections.reverse(numList);
        for(int i=0;i<numList.size();i++){
            map.put(numList.get(i),(char)(i+97));
            str=str.replace(String.valueOf(numList.get(i)),String.valueOf( map.get(numList.get(i))));
        }
        str=str.substring(0,str.length()-1);
        System.out.println(str);
        if(Valid_Arithmetic(str,str.length()))
            return  infixToPostfix(str);
        else return "Not valid";
    }

    /**============================================== FOR CHECKING VALIDITY OF EXPRESSION ==========================================**/
    public boolean Valid_Arithmetic(String str, int len)
    {
        if (Check_Parenthesis(str, len)) // like "())" and more
        {
            return false;
        }
        if (Check_For_Invalidity(str, len)) // like "a(" and "a-)" and ")a" and "ab"
        {
            return false;
        }
        // Operator Are Always Less Than One Then The Operands
        return !Check_Opeartor_Opearand(str, len);
    }

    public static boolean Check_Opeartor_Opearand(String str, int len)
    {
        int OperatorCount = 0, OperandCount = 0;
        for (int i = 0; i < len; i++)
        {
            if (((int)str.charAt(i) >= 65 && (int)str.charAt(i) <= 91) || ((int)str.charAt(i) >= 97 && (int)str.charAt(i) <= 123))
                OperandCount++;
            if (str.charAt(i) == '^' || str.charAt(i) == '-' || str.charAt(i) == '+' || str.charAt(i) == '·' || str.charAt(i) == '/')
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
                    boolean c = str.charAt(i - 1) == '^' || str.charAt(i - 1) == '-' || str.charAt(i - 1) == '+' || str.charAt(i - 1) == '·' || str.charAt(i - 1) == '/';
                    if (c && str.charAt(i) == ')')
                        return true;
                    else if (a && b)
                        return true;
                    else if (c && (str.charAt(i)== '^' || str.charAt(i) == '-' || str.charAt(i) == '+' || str.charAt(i) == '·' ||str.charAt(i) == '/'))
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

            case '^':
                return 3;

            case '#':
            case '$':
            case '~':
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
                while (!stack.isEmpty() &&
                        stack.peek() != '(')
                    result.append(stack.pop());

                stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Precedence(c)
                        <= Precedence(stack.peek())){

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
