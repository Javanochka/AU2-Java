package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.Calculator;
import ru.spbau.nikiforovskaya.util.Stack;

/**
 * Example showing how Calculator class works.
 * Pass an expression to the arguments and look at the result.
 */
public class CalculatorExample {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Pass to arguments an expression to calculate");
            return;
        }
        Calculator calculator = new Calculator(new Stack<>(), new Stack<>());
        System.out.print(args[0] + " = ");
        System.out.println(calculator.calculateGeneralExpression(args[0]));
    }
}
