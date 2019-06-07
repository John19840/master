package com.tsystems.javaschool.tasks.calculator;

import java.util.*;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // TODO: Implement the logic here
        if(statement == null) return null;
        PrefixParser parser = new PrefixParser();
        if(!parser.isCorrect(statement)) return null;
        String result = calculate(parser.parse(statement));
        return result;
    }
    //class for parsing expression
    class PrefixParser {
        private String operators = "+-*/";
        private String delimiters = "()" + operators;
        //check expressions for errors
        public boolean isCorrect(String expression){
            //chech double operators
            for (int i = 0; i < operators.length(); i++) {
                String token = String.valueOf(operators.charAt(i));
                token += token;
                if(expression.contains(token))return false;
            }
            //check operators at start\end of string
            for (int i = 0; i < operators.length(); i++) {
                if(expression.startsWith(String.valueOf(operators.charAt(i)))) return false;
                if(expression.endsWith(String.valueOf(operators.charAt(i)))) return false;
            }
            //check correct brackets count and position
            int count = 0;
            for (int i = 0; i < expression.length(); i++) {
                if(expression.charAt(i) == '(') count++;
                else if(expression.charAt(i) == ')' && count == 0) return false;
                else if(expression.charAt(i) == ')') count--;
            }
            if(count != 0) return false;


            return true;
        }

        private boolean isOperator(String token) {
            for (int i = 0; i < delimiters.length(); i++) {
                if (token.charAt(0) == delimiters.charAt(i)) return true;
            }
            return false;
        }

        private int priority(String token) {
            if (token.equals("(")) return 1;
            if (token.equals("+") || token.equals("-")) return 2;
            if (token.equals("*") || token.equals("/")) return 3;
            return 4;
        }

        public List<String> parse(String infix) {
            if(infix.isEmpty() || infix.equals("")) return null;

            List<String> postfix = new ArrayList<String>();
            Deque<String> operations = new ArrayDeque<String>();
            StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);

            String curr;

            while (tokenizer.hasMoreTokens()) {

                curr = tokenizer.nextToken();
                //check numbers
                if(curr.matches("^\\d+\\.\\d+$") || curr.matches("^\\d+$")){
                    postfix.add(curr);
                }
                else {
                    //check brackets
                    if(curr.equals("(")) operations.push(curr);
                    else if(curr.equals(")")){
                        while(!operations.peek().equals("(")) {
                            postfix.add(operations.pop());
                        }
                        operations.pop();
                    }
                    //check operators ant its priorities
                    else if(isOperator(curr)){
                        if(operations.isEmpty()) operations.push(curr);
                        else if(priority(curr) < priority(operations.peek()) || priority(curr) == priority(operations.peek())) {
                            postfix.add(operations.pop());
                            operations.push(curr);
                        }
                        else operations.push(curr);
                    }
                    else return null;
                }


            }
            while(!operations.isEmpty()) {
                postfix.add(operations.pop());
            }
            return postfix;
        }
    }

    public String calculate(List<String> postfix){
        if(postfix == null) return null;
        Deque<Double> deque = new ArrayDeque<Double>();
        for (String t : postfix) {
            if(t.matches("^\\d+.\\d+$") || t.matches("^\\d+$")) deque.push(Double.valueOf(t));
            else if(t.equals("+")) deque.push(deque.pop() + deque.pop());
            else if(t.equals("-")) {
                Double b = deque.pop(), a = deque.pop();
                deque.push(a - b);
            }
            else if (t.equals("*")) deque.push(deque.pop() * deque.pop());
            else if (t.equals("/")) {
                Double b = deque.pop(), a = deque.pop();
                if(b == 0) return null;
                deque.push(a / b);
            }
        }
        Double result = deque.pop();
        //if result is int ->
        if(result % 1 == 0) {
            int r = result.intValue();
            return String.valueOf(r);
        }
        else {
            //if double result -> round
            result = (double) Math.round(result * 10000d) / 10000d;
            return result.toString();
        }
    }
}
