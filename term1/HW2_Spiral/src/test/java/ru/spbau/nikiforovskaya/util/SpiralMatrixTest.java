package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SpiralMatrixTest {

    SpiralMatrix sm;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(null);
    }

    @Test
    void testSortByColumns1x1() {
        sm = new SpiralMatrix(new int[][] {{1}});
        sm.sortByColumns();
        sm.printMatrix();
        assertEquals("1", output.toString().trim());
    }

    @Test
    void testSortByColumns3x3DifferentReverse() {
        sm = new SpiralMatrix(new int[][] {{9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}});
        sm.sortByColumns();
        sm.printMatrix();
        assertEquals("7 8 9 \n4 5 6 \n1 2 3", output.toString().trim());
    }

    @Test
    void testSortByColumns3x3SameFirst() {
        sm = new SpiralMatrix(new int[][] {{1, 1, 1},
                {2, 3, 4},
                {5, 6, 7}});
        sm.sortByColumns();
        sm.printMatrix();
        assertEquals("1 1 1 \n2 3 4 \n5 6 7", output.toString().trim());
    }

    @Test
    void testSortByColumns5x5DifferentRandom() {
        sm = new SpiralMatrix(new int[][] {{9, 4, 7, 5, 6},
                {6, 5, 4, 7, 1},
                {4, 6, 3, 2, 1},
                {7, 4, 3, 7, 1},
                {2, 5, 1 ,6 ,2}});
        sm.sortByColumns();
        sm.printMatrix();
        assertEquals("4 5 6 7 9 \n" +
                "5 7 1 4 6 \n" +
                "6 2 1 3 4 \n" +
                "4 7 1 3 7 \n" +
                "5 6 2 1 2", output.toString().trim());
    }

    @Test
    void testPrintSpiral1x1() {
        sm = new SpiralMatrix(new int[][] {{1}});
        sm.printSpiral();
        assertEquals("1", output.toString().trim());
    }

    @Test
    void testPrintSpiralByColumns3x3Different() {
        sm = new SpiralMatrix(new int[][] {{9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}});
        sm.printSpiral();
        assertEquals("5 4 7 8 9 6 3 2 1", output.toString().trim());
    }

    @Test
    void testPrintSpiralByColumns3x3SameFirst() {
        sm = new SpiralMatrix(new int[][] {{1, 1, 1},
                {2, 3, 4},
                {5, 6, 7}});
        sm.printSpiral();
        assertEquals("3 4 1 1 1 2 5 6 7", output.toString().trim());
    }

    @Test
    void testPrintSpiralByColumns5x5DifferentRandom() {
        sm = new SpiralMatrix(new int[][] {{9, 4, 7, 5, 6},
                {6, 5, 4, 7, 1},
                {4, 6, 3, 2, 1},
                {7, 4, 3, 7, 1},
                {2, 5, 1 ,6 ,2}});
        sm.printSpiral();
        assertEquals("3 2 7 4 5 6 4 3 7 1 1 1 6" +
                " 5 7 4 9 6 4 7 2 5 1 6 2", output.toString().trim());
    }

    @Test
    void testPrintMatrix3x3Different() {
        sm = new SpiralMatrix(new int[][] {{9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}});
        sm.printMatrix();
        assertEquals("9 8 7 \n" +
                "6 5 4 \n" +
                "3 2 1", output.toString().trim());
    }

}