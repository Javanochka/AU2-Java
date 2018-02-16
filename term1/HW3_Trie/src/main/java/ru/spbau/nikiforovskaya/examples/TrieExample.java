package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.Trie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** An example showing the way Trie works*/
public class TrieExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Trie t = new Trie();
        System.out.println("t.add(\"aba\"): " + t.add("aba"));
        System.out.println("t.add(\"abacaba\"): " + t.add("abacaba"));
        System.out.println("t.add(\"abac\"): " + t.add("abac"));
        System.out.println("t.size(): " + t.size());
        System.out.println("t.remove(\"abac\"): " + t.remove("abac"));
        System.out.println("t.size(): " + t.size());
        System.out.println("t.remove(\"grrr\"): " + t.remove("grrr"));
        System.out.println("t.howManyStartsWithPrefix(\"ab\"): " +
                t.howManyStartsWithPrefix("ab"));
        System.out.println("t.howManyStartsWithPrefix(\"aba\"): " +
                t.howManyStartsWithPrefix("aba"));
        System.out.println("t.howManyStartsWithPrefix(\"abac\"): " +
                t.howManyStartsWithPrefix("abac"));
        System.out.println("t.contains(\"ab\"): " + t.contains("ab"));
        System.out.println("t.contains(\"aba\"): " + t.contains("aba"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        t.serialize(os);
        System.out.println("serialization results: " + os.toString());
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Trie t1 = new Trie();
        t1.deserialize(is);
        System.out.println("t1.size(): " + t1.size());
        System.out.println("t1.contains(\"ab\"): " + t1.contains("ab"));
        System.out.println("t1.contains(\"aba\"): " + t1.contains("aba"));
    }
}
