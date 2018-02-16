package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.HashMap;

import java.util.Map;

/**
 * An Example class shows basic possibilities of HashMap
 */
public class HashMapExample {
    public static void main(String[] args) {
        HashMap<String, String> ht = new HashMap<>();

        System.out.println("Welcome. Let's see the possibilities of HashTable.");
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"anything\"): " + ht.containsKey("anything"));
        System.out.println("ht.get(\"anything\"): " + ht.get("anything"));
        System.out.println("ht.remove(\"anything\"): " + ht.remove("anything"));
        System.out.println("ht.put(\"Cat\", \"Asya\"): " + ht.put("Cat", "Asya"));
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"Cat\"): " + ht.containsKey("Cat"));
        System.out.println("ht.get(\"Cat\"): " + ht.get("Cat"));
        System.out.println("ht.put(\"Dog\", \"Asya\"): " + ht.put("Dog", "Asya"));
        System.out.println("ht.contains(\"Dog\"): " + ht.containsKey("Dog"));
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.put(\"Cat\", \"Masya\"): " + ht.put("Cat", "Masya"));
        System.out.println("ht.entrySet(): ");
        for (Map.Entry<String, String> entry : ht.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        ht.clear();
        System.out.println("ht.clear()");
        System.out.println("ht.size(): " + ht.size());
        System.out.println("ht.contains(\"Cat\"): " + ht.containsKey("Cat"));
        System.out.println("ht.contains(\"Dog\"): " + ht.containsKey("Dog"));

    }
}