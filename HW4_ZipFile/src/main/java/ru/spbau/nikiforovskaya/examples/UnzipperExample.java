package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.Unzipper;

import java.io.IOException;

/**
 * An example, showing how Unzipper class works.
 * Pass source path, regex and destination path to its arguments.
 */
public class UnzipperExample {
    public static void main(String[] args) {
        Unzipper un = new Unzipper();
        try {
            un.extractFilesByRegex(args[0], args[1], args[2]);
        } catch (IOException e) {
            System.out.println("Something got wrong with the paths you passed to the arguments.");
            System.out.println("Error info:");
            e.printStackTrace();
            System.out.println("Please, rerun te program");
        }
    }
}
