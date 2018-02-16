package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.Set;

/** An example showing how Set works. */
public class SetExample {
    public static void main(String[] args) {
        Set<Integer> s = new Set<>();
        System.out.println("s.size(): " + s.size());
        s.add(2);
        System.out.println("s.add(2)");
        System.out.println("s.size(): " + s.size());
        System.out.println("s.contains(2): " + s.contains(2));
        System.out.println("s.contains(3): " + s.contains(3));
        s.add(5);
        System.out.println("s.add(5)");
        System.out.println("s.size(): " + s.size());
        System.out.println("s.contains(2): " + s.contains(2));
        System.out.println("s.contains(3): " + s.contains(3));
        System.out.println("s.contains(5): " + s.contains(5));
        System.out.println("s.toArrayList().toString(): "
                + s.toArrayList().toString());
    }
}
