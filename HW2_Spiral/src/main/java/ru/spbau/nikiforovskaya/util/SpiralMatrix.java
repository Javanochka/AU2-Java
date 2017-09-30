package ru.spbau.nikiforovskaya.util;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A class, which can print matrix spirally and sort its columns by their first elements
 */
public class SpiralMatrix {

    private int[][] data;
    private int size;

    final static private int[][] DIRECTION = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

    /**
     * Constructs a <code>SpiralMatrix</code> basing on a given matrix
     * @param data A matrix to process.
     *             Requirements:
     *             <code>data.length % 2 = 1 </code>,
     *             <code>data.length = data[0].length</code>
     */
    public SpiralMatrix(int[][] data) {
        size = data.length;
        this.data = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.data[j][i] = data[i][j];
            }
        }
    }

    /**
     * Sorts matrix by first elements of the columns.
     */
    public void sortByColumns() {
        Arrays.sort(data, Comparator.comparingInt(o -> o[0]));
    }

    /**
     * Prints matrix spirally anticlockwise, starting from the middle cell.
     */
    public void printSpiral() {
        int x = size / 2;
        int y = size / 2;
        int currentDir = 0;
        int steps = 1;
        boolean change = false;
        for (int i = 0; i < size * size;) {
            for (int j = 0; j < steps; j++) {
                System.out.print(data[y][x] + " ");
                y += DIRECTION[currentDir][0];
                x += DIRECTION[currentDir][1];
            }
            i += steps;
            currentDir++;
            currentDir %= DIRECTION.length;
            if (change) {
                steps++;
            }
            change ^= true;
        }
    }

    /**
     * Prints matrix in the following format:
     * every row ends with a space and a new-line symbol,
     * numbers in every row are separated by single spaces.
     */
    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(data[j][i] + " ");
            }
            System.out.println();
        }
    }
}
