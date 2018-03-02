package ru.spbau.nikiforovskaya.examples;

import ru.spbau.nikiforovskaya.util.LightFuture;
import ru.spbau.nikiforovskaya.util.ThreadPool;

/** An example showing how ThreadPool works. */
public class ThreadPoolExample {

    public static void main(String[] args) throws LightFuture.LightExecutionException {
        ThreadPool<Integer> pool = new ThreadPool<>(5);
        LightFuture<Integer> task = pool.addTask(() -> 2 * 2);
        System.out.println(task.get() + " = 2 * 2");
        LightFuture<Integer> task1 = pool.addTask(() -> 2 * 3);
        LightFuture<Integer> task2 = task1.thenApply((i) -> i + 1);
        LightFuture<Integer> task3 = task1.thenApply((i) -> i + 2);
        System.out.println(task1.get() + " = 2 * 3");
        System.out.println(task2.get() + " = 6 + 1");
        System.out.println(task3.get() + " = 6 + 2");
    }
}
