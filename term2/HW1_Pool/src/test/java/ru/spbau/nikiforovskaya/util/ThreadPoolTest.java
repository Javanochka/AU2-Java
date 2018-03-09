package ru.spbau.nikiforovskaya.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {

    @Test
    void testOneTaskOneAskOneThread() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        assertEquals(4, (int) task.get());
    }

    @Test
    void testOneTaskManyAsksOneThread() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
    }

    @Test
    void testOneTaskManyAsksSameAnswerOneThread() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        int[] number = new int[] {4};
        LightFuture<Integer> task = pool.addTask(() -> number[0]);
        assertEquals(4, (int) task.get());
        number[0] = 39834;
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
    }

    @Test
    void testTwoTasksTwoAsksOneThread() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        LightFuture<Integer> task1 = pool.addTask(() -> 2 * 2);
        LightFuture<Integer> task2 = pool.addTask(() -> 2 * 6);
        assertEquals(12, (int) task2.get());
        assertEquals(4, (int) task1.get());
    }

    @Test
    void testTwoTasksTwoAsksOneThreadExceptional() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(1);
        LightFuture<Integer> task1 = pool.addTask(() -> null);
        LightFuture<Integer> task2 = task1.thenApply(a -> a * 2);
        assertNull(task1.get());
        assertThrows(LightFuture.LightExecutionException.class, task2::get);
    }

    @Test
    void testOneTaskOneAskManyThreads() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        assertEquals(4, (int) task.get());
    }

    @Test
    void testOneTaskManyAsksManyThreads() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
    }

    @Test
    void testOneTaskManyAsksSameAnswerManyThreads() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        int[] number = new int[] {4};
        LightFuture<Integer> task = pool.addTask(() -> number[0]);
        assertEquals(4, (int) task.get());
        number[0] = 39834;
        assertEquals(4, (int) task.get());
        assertEquals(4, (int) task.get());
    }

    @Test
    void testTwoTasksTwoAsksManyThreads() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task1 = pool.addTask(() -> 2 * 2);
        LightFuture<Integer> task2 = pool.addTask(() -> 2 * 6);
        assertEquals(12, (int) task2.get());
        assertEquals(4, (int) task1.get());
    }

    @Test
    void testTwoTasksTwoAsksManyThreadsExceptional() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task1 = pool.addTask(() -> null);
        LightFuture<Integer> task2 = task1.thenApply(a -> a * 2);
        assertNull(task1.get());
        assertThrows(LightFuture.LightExecutionException.class, task2::get);
    }

    @Test
    void testManyTasksManyAsksManyThreads() throws LightFuture.LightExecutionException, InterruptedException {
        ThreadPool<Integer> pool = new ThreadPool<>(10);
        int[] number = new int[1000];
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            pool.addTask(() -> ++number[j]);
        }

        Thread.sleep(1000);

        for (int i = 0; i < 1000; i++) {
            assertEquals(1, number[i]);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void testShutdown() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Supplier<Integer> job = () -> 0;

        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer>[] tasks = new LightFuture[5];

        for (int i = 0; i < 5; i++) {
            tasks[i] = pool.addTask(job);
        }

        Field threads = pool.getClass().getDeclaredField("threads");
        threads.setAccessible(true);

        for (Thread thread : (Thread[]) threads.get(pool)) {
            assertFalse(thread.isInterrupted());
            assertTrue(thread.isAlive());
        }

        pool.shutdown();

        Thread.sleep(1000);

        for (Thread thread : (Thread[]) threads.get(pool)) {
            assertFalse(thread.isAlive());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void testThenApplyPowers() throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(10);
        LightFuture<Integer>[] tasks = new LightFuture[10];
        tasks[0] = pool.addTask(() -> 1);
        for (int i = 1; i < 10; i++) {
            tasks[i] = tasks[i - 1].thenApply((x) -> x * 2);
        }

        for (int i = 9; i > -1; i--) {
            assertEquals(1 << i, (int) tasks[i].get());
        }
    }

}