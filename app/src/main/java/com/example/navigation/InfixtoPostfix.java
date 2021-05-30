package com.example.navigation;

import java.util.Stack;

public class InfixtoPostfix {
    // A utility function to return
    // precedence of a given operator
    // Higher returned value means
    // higher precedence
    public int Precedence(char ch)
    {
        switch (ch) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
        }
        return -1;
    }

    // The main method that converts
    // given infix expression
    // to postfix expression.
    public String infixToPostfix(String exp)
    {
        // initializing empty String for result
        StringBuilder result = new StringBuilder(new String(""));

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
                while (!stack.isEmpty() && Precedence(c) <= Precedence(stack.peek()))
                    result.append(stack.pop());

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
