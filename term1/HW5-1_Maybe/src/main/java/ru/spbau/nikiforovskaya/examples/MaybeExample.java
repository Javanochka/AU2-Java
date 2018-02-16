package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.Maybe;
import ru.spbau.nikiforovskaya.util.MaybeException;

import java.io.*;

/**
 * An example, showing how maybe works.
 * Reads int values from file test.in,
 * writes their squares into the other file.
   If there was written not int in file, writes {@code null} as a result.
 */
public class MaybeExample {
    public static void main(String[] args) throws MaybeException {
        try (BufferedReader in = new BufferedReader(
                new FileReader("src/main/resources/test.in"));
        PrintWriter out = new PrintWriter("src/main/resources/test.out")) {
            String s = null;
            while ((s = in.readLine()) != null) {
                Maybe<Integer> n;
                try {
                    n = Maybe.just(Integer.parseInt(s));
                } catch (NumberFormatException ne) {
                    n = Maybe.nothing();
                }
                Maybe<Integer> result = n.map(integer -> integer * integer);
                out.println(result.isPresent() ? result.get() : null);

            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find the file \"test.in\".");
            System.out.println("Please, create the file and then run the program again.");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            System.out.println("Some problem occurred:");
            e.printStackTrace();
        }
    }
}
