package ru.spbau.nikiforovskaya.util;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.spbau.nikiforovskaya.util.SecondPartTasks.*;

class SecondPartTasksTest {
    @Test
    void testFindQuotes() {
        String[] result =
                findQuotes(Arrays.asList("src/test/resources/cat",
                        "src/test/resources/dog", "src/test/resources/file1"),
                "Asya").toArray(new String[]{});
        Arrays.sort(result);
        String[] right = {
                "my name is Asyablahblah3ojfnkd",
                "kef o4m rkmbd Asya",
                "And my name is Asya",
                "www.Asya.com",
                "iewjflkmdf but fox couldn't agree with Asya",
                "nwjhdckdns Asyaaaaa????",
                "Asya"
        };
        Arrays.sort(right);
        assertArrayEquals(right, result);
    }

    @Test
    void testPiDividedBy4() {
        for (int i = 0; i < 10; i++) {
            assertEquals(Math.PI / 4, piDividedBy4(), 1e-3);
        }
    }

    @Test
    void testFindPrinter() {
        Map<String, List<String>> m = new HashMap<>();
        m.put("A", Arrays.asList("Once upon a time there was",
                "She said meow meow meow", "seojfoesijhfosel  wcge",
                "I cannot write stories -_-"));
        m.put("B", Arrays.asList("Once", "Twice", "Is that true?"));
        m.put("C", Arrays.asList("HEY-HEY", "BIG STORY", "blah blah blah blah",
                "meow meow meow meow", "owjfkjdfnfkjv esdfncn iuhfk",
                "kw3jfuehin ,;efdne u28q"));
        assertEquals("C", findPrinter(m));
        m = new HashMap<>();
        assertEquals("", findPrinter(m));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testCalculateGlobalOrder() {
        Map<String, Integer> m1 = new HashMap<>();
        m1.put("Apple", 3);
        m1.put("Orange", 1);
        m1.put("Pear", 5);
        Map<String, Integer> m2 = new HashMap<>();
        m2.put("Apple", 4);
        m2.put("Orange", 100);
        m2.put("Kiwi", 3);
        Map<String, Integer> m3 = new HashMap<>();
        m3.put("Apple", 200);
        m3.put("Orange", 1);
        m3.put("Kiwi", 1);
        m3.put("Grape", 32);
        Map<String, Integer> result = calculateGlobalOrder(Arrays.asList(m1, m2, m3));
        Map<String, Integer> wanted = new HashMap<>();
        wanted.put("Apple", 207);
        wanted.put("Orange", 102);
        wanted.put("Kiwi", 4);
        wanted.put("Pear", 5);
        wanted.put("Grape", 32);
        Object[] wantedArr = wanted.entrySet().toArray();
        Comparator<Object> comparator = (o1, o2) -> {
            Map.Entry<String, Integer> t1 = (Map.Entry<String, Integer>) o1;
            Map.Entry<String, Integer> t2 = (Map.Entry<String, Integer>) o2;
            if (t1.getKey().equals(t2.getKey())) {
                return Integer.compare(t1.getValue(), t2.getValue());
            }
            return t1.getKey().compareTo(t2.getKey());
        };
        Arrays.sort(wantedArr, comparator);
        Object[] resultArr = result.entrySet().toArray();
        Arrays.sort(resultArr, comparator);
        assertArrayEquals(wantedArr, resultArr);
    }
}