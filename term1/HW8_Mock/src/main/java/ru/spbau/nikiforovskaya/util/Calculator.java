package ru.spbau.nikiforovskaya.util;

import org.jetbrains.annotations.NotNull;

/**
 * A class, providing functions to calculate expressions,
 * written in regular or reverse polish notation.
 * Receives only expressions without spaces,
 * each number consists of the only digit.
 */
public class Calculator {

    private Stack<Character> operators;
    private Stack<Double> values;

    /**
     * Constructs new calculator, using given stacks
     * @param operators stack where operators will be stored
     * @param values stack where values will be stored
     */
    public Calculator (@NotNull Stack<Character> operators, @NotNull Stack<Double> values) {
        this.operators = operators;
        this.values = values;
    }

    /**
     * Calculates result of expression in reverse polish notation
     * @param expression expression in reverse polish notation
     * @return result of calculating
     */
    public double calculateReversePolishNotation(@NotNull String expression) {
        values.clear();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                values.push((double)(c - '0'));
            } else {
                double b = values.pop();
                double a = values.pop();
                values.push(apply(c, a, b));
            }
        }
        return values.pop();
    }

    /**
     * Transforms expression in regular notation into reverse Polish one.
     * @param expression expression in regular notation to process.
     * @return expression in reverse Polish notation
     */
    public @NotNull String getReversePolishNotation(@NotNull String expression) {
        operators.clear();
        StringBuilder result = new StringBuilder("");
        expression = "(" + expression + ")";
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                char top = operators.pop();
                while (top != '(') {
                    result.append(top);
                    top = operators.pop();
                }
            } else {
                int thisPriority = getPriority(c);
                while (!operators.isEmpty() && getPriority(operators.top()) >= thisPriority) {
                    result.append(operators.pop());
                }
                operators.push(c);
            }

        }
        return result.toString();
    }

    /**
     * Calculates expression in regular notation.
     * @param expression in regular notation to calculate
     * @return result of calculating
     */
    public double calculateGeneralExpression(@NotNull String expression) {
        String polishNotation = getReversePolishNotation(expression);
        return calculateReversePolishNotation(polishNotation);
    }

    private int getPriority(char c) {
        switch (c) {
            case '(':
                return 0;
            case '+':
            case'-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                throw new IllegalArgumentException("operators should be only + - * /");
        }
    }

    private double apply(char c, double a, double b) {
        switch (c) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                throw new IllegalArgumentException("operators should be only + - * /");
        }
    }
}
