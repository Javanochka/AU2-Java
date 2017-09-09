package ru.spbau.nikiforovskaya.test;

import ru.spbau.nikiforovskaya.util.HashTable;

/**
 * An Example class shows basic possibilities of HashTable
 */
public class Example {
    public static void main(String[] args) {
        HashTable ht = new HashTable();

        System.out.println("Welcome. Let's see the possibilities of HashTable.");
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"anything\"): " + ht.contains("anything"));
        System.out.println("ht.get(\"anything\"): " + ht.get("anything"));
        System.out.println("ht.remove(\"anything\"): " + ht.remove("anything"));
        System.out.println("ht.put(\"Cat\", \"Asya\"): " + ht.put("Cat", "Asya"));
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"Cat\"): " + ht.contains("Cat"));
        System.out.println("ht.get(\"Cat\"): " + ht.get("Cat"));
        System.out.println("ht.put(\"Dog\", \"Asya\"): " + ht.put("Dog", "Asya"));
        System.out.println("ht.contains(\"Dog\"): " + ht.contains("Dog"));
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.put(\"Cat\", \"Masya\"): " + ht.put("Cat", "Masya"));
        ht.clear();
        System.out.println("ht.clear()");
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"Cat\"): " + ht.contains("Cat"));
        System.out.println("ht.contains(\"Dog\"): " + ht.contains("Dog"));

    }
}
