package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.SpiralMatrix;

/**
 * An example, showing how <code>SpiralMatrix</code> class works.
 */
public class SpiralMatrixExample {

    public static void main(String[] args) {

        int[][] data = {{3,2,1},
                {6,5,4},
                {9,8,7}};
        System.out.println("Current matrix: ");
        SpiralMatrix sm = new SpiralMatrix(data);
        sm.printMatrix();
        sm.sortByColumns();
        System.out.println("Sorted matrix: ");
        sm.printMatrix();
        System.out.println("Spiral: ");
        sm.printSpiral();
    }
}
