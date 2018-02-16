package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorTest {

    private Stack<Double> mockedStackValues;
    private Stack<Character> mockedStackOperators;

    @BeforeEach
    void setUp() {
        mockedStackValues = mockStack();
        mockedStackOperators = mockStack();
    }

    @Test
    void testCalculateReversePolishNotationOnlyOperator() {
        String toProcess = "23+";

        when(mockedStackValues.pop())
                .thenReturn(3.0, 2.0, 5.0);
        Calculator calculator = new Calculator(mockedStackOperators, mockedStackValues);
        double result = calculator.calculateReversePolishNotation(toProcess);
        assertEquals(5.0, result, 1e-9);
        InOrder inOrder = inOrder(mockedStackValues);

        verify(mockedStackValues, times(3)).push(anyDouble());
        inOrder.verify(mockedStackValues).clear();
        inOrder.verify(mockedStackValues).push(2.0);
        inOrder.verify(mockedStackValues).push(3.0);
        inOrder.verify(mockedStackValues).push(5.0);
        verifyNoMoreInteractions(mockedStackOperators);
    }

    @Test
    void testCalculateReversePolishNotationDifficult() {
        String toProcess = "23+4*6-2/";

        when(mockedStackValues.pop())
                .thenReturn(3.0, 2.0, 4.0, 5.0, 6.0, 20.0, 2.0, 14.0, 7.0);
        Calculator calculator = new Calculator(mockedStackOperators, mockedStackValues);
        double result = calculator.calculateReversePolishNotation(toProcess);
        assertEquals(7.0, result, 1e-9);

        InOrder inOrder = inOrder(mockedStackValues);

        verify(mockedStackValues, times(9)).push(anyDouble());
        inOrder.verify(mockedStackValues).clear();
        inOrder.verify(mockedStackValues).push(2.0);
        inOrder.verify(mockedStackValues).push(3.0);
        inOrder.verify(mockedStackValues).push(5.0);
        inOrder.verify(mockedStackValues).push(4.0);
        inOrder.verify(mockedStackValues).push(20.0);
        inOrder.verify(mockedStackValues).push(6.0);
        inOrder.verify(mockedStackValues).push(14.0);
        inOrder.verify(mockedStackValues).push(2.0);
        inOrder.verify(mockedStackValues).push(7.0);
        verifyNoMoreInteractions(mockedStackOperators);
    }

    @Test
    void testGetReversePolishNotationOnlyOperator() {
        String toProcess = "2+3";

        when(mockedStackOperators.isEmpty())
                .thenReturn(true, true);
        when(mockedStackOperators.pop())
                .thenReturn('+', '(');
        Calculator calculator = new Calculator(mockedStackOperators, mockedStackValues);
        String result = calculator.getReversePolishNotation(toProcess);
        assertEquals("23+", result);
        verifyNoMoreInteractions(mockedStackValues);
        InOrder inOrder = inOrder(mockedStackOperators);
        inOrder.verify(mockedStackOperators).clear();
        inOrder.verify(mockedStackOperators).push('(');
        inOrder.verify(mockedStackOperators).push('+');
        verify(mockedStackOperators, times(2)).push(anyChar());
    }

    @Test
    void testGetReversePolishNotationDifficult() {
        String toProcess = "((2+3)*4-6)/2";
        when(mockedStackOperators.isEmpty())
                .thenReturn(true, true, false, false, true);
        when(mockedStackOperators.pop())
                .thenReturn('+', '(', '*', '-', '(', '/', '(');
        when(mockedStackOperators.top())
                .thenReturn('*', '(');

        Calculator calculator = new Calculator(mockedStackOperators, mockedStackValues);
        String result = calculator.getReversePolishNotation(toProcess);
        assertEquals("23+4*6-2/", result);
        verifyNoMoreInteractions(mockedStackValues);
        InOrder inOrder = inOrder(mockedStackOperators);
        inOrder.verify(mockedStackOperators).clear();
        inOrder.verify(mockedStackOperators, times(3)).push('(');
        inOrder.verify(mockedStackOperators).push('+');
        inOrder.verify(mockedStackOperators).push('*');
        inOrder.verify(mockedStackOperators).push('-');
        inOrder.verify(mockedStackOperators).push('/');
        verify(mockedStackOperators, times(7)).push(anyChar());
    }

    @Test
    void testCalculateGeneralExpressionSimple() {
        String toProcess = "2+3";
        when(mockedStackOperators.isEmpty())
                .thenReturn(true, true);
        when(mockedStackOperators.pop())
                .thenReturn('+', '(');
        when(mockedStackValues.pop())
                .thenReturn(3.0,2.0, 5.0);
        Calculator calculator = new Calculator(mockedStackOperators, mockedStackValues);
        double result = calculator.calculateGeneralExpression(toProcess);
        assertEquals(5.0, result, 1e-9);
    }

    @SuppressWarnings("unchecked")
    private <T> Stack<T> mockStack() {
        return mock(Stack.class);
    }

}