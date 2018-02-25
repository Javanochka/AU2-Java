package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LazyFactoryTest {

    private Random r;

    @BeforeEach
    void setUp() {
        r = new Random();
    }

    @Test
    void testCreateSimpleLazyCheckReturnsString() {
        Lazy<String> lazy = LazyFactory.createSimpleLazy(() -> "abacaba");
        assertEquals("abacaba", lazy.get());
    }

    @Test
    void testCreateSimpleLazyCheckReturnsNull() {
        Lazy<String> lazy = LazyFactory.createSimpleLazy(() -> null);
        assertNull(lazy.get());
    }

    @Test
    void testCreateSimpleLazyCheckNumberOfCalls() {
        int[] num = new int[]{0};
        Lazy<String> lazy = LazyFactory.createSimpleLazy(() -> {
            num[0]++;
            return "aba";
        });

        for (int i = 0; i < 100; i++) {
            assertEquals("aba", lazy.get());
        }

        assertEquals(1, num[0]);
    }

    @Test
    void testCreateSimpleLazyCheckNumberOfCallsRandom() {
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(r::nextInt);

        int ans = lazy.get();
        for (int i = 0; i < 100; i++) {
            assertEquals(ans, (int)lazy.get());
        }
    }

    @Test
    void testCreateMultithreadLazyCheckReturnsStringOneThread() {
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> "abacaba");
        assertEquals("abacaba", lazy.get());
    }

    @Test
    void testCreateMultithreadLazyCheckReturnsStringManyThreads() throws InterruptedException {
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> "aba");
        int n = 10;
        Thread[] threads = new Thread[n];
        boolean[] result = new boolean[n];
        for (int i = 0; i < n; i++) {
            final int fi = i;
            threads[i] = new Thread(() -> result[fi] = lazy.get().equals("aba"));
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }
        for (int i = 0; i < n; i++) {
            assertTrue(result[i]);
        }
    }

    @Test
    void testCreateMultithreadLazyCheckReturnsNullOneThread() {
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> null);
        assertNull(lazy.get());
    }

    @Test
    void testCreateMultithreadLazyCheckReturnsNullManyThreads() throws InterruptedException {
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> null);
        int n = 10;
        Thread[] threads = new Thread[n];
        boolean[] result = new boolean[n];
        for (int i = 0; i < n; i++) {
            final int fi = i;
            threads[i] = new Thread(() -> result[fi] = lazy.get() == null);
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }
        for (int i = 0; i < n; i++) {
            assertTrue(result[i]);
        }
    }

    @Test
    void testCreateMultithreadLazyCheckNumberOfCallsOneThread() {
        int[] num = new int[]{0};
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> {
            num[0]++;
            return "aba";
        });

        for (int i = 0; i < 100; i++) {
            assertEquals("aba", lazy.get());
        }
        assertEquals(1, num[0]);
    }

    @Test
    void testCreateMultithreadLazyCheckNumberOfCallsManyThreads() throws InterruptedException {
        int[] num = new int[]{0};
        Lazy<String> lazy = LazyFactory.createMultithreadLazy(() -> {
            num[0]++;
            return "aba";
        });
        int n = 10;
        Thread[] threads = new Thread[n];
        boolean[] result = new boolean[n];
        for (int i = 0; i < n; i++) {
            final int fi = i;
            threads[i] = new Thread(() -> result[fi] = lazy.get().equals("aba"));
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }
        for (int i = 0; i < n; i++) {
            assertTrue(result[i]);
        }
        assertEquals(1, num[0]);
    }

    @Test
    void testCreateMultithreadLazyCheckNumberOfCallsRandomOneThread() {
        Lazy<Integer> lazy = LazyFactory.createMultithreadLazy(r::nextInt);

        int ans = lazy.get();
        for (int i = 0; i < 100; i++) {
            assertEquals(ans, (int)lazy.get());
        }
    }

    @Test
    void testCreateMultithreadLazyCheckNumberOfCallsRandomManyThreads() throws InterruptedException {
        Lazy<Integer> lazy = LazyFactory.createMultithreadLazy(r::nextInt);
        int n = 10;
        Thread[] threads = new Thread[n];
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            final int fi = i;
            threads[i] = new Thread(() -> result[fi] = lazy.get());
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }
        int ans = result[0];
        for (int i = 0; i < n; i++) {
            assertEquals(ans, result[i]);
        }
    }
}