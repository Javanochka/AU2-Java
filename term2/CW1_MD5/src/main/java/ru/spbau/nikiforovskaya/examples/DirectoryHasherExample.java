package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.hasher.DirectoryHasher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Example, showing the difference between general realization
 * and fork join one in DirectoryHasher.
 *
 * Just pass the directory name to the arguments and see the result)
 */
public class DirectoryHasherExample {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        long time1 = System.currentTimeMillis();
        String s1 = DirectoryHasher.getDirectoryHashSimple(args[0]);
        time1 = System.currentTimeMillis() - time1;
        long time2 = System.currentTimeMillis();
        String s2 = DirectoryHasher.getDirectoryHashForkJoin(args[0]);
        time2 = System.currentTimeMillis() - time2;
        if (s1.equals(s2)) {
            System.out.println("Ok");
            System.out.println("Simple: " + time1);
            System.out.println("Fork join: " + time2);
        } else {
            System.out.println("Fail");
        }
        System.out.println("Hash simple: " + s1);
        System.out.println("Hash fork join: " + s2);
    }
}
