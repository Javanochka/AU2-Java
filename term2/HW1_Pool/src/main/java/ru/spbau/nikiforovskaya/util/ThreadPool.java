package ru.spbau.nikiforovskaya.util;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A thread pool of tasks with fixed number of threads.
 * @param <T> a type of data which will be processed.
 */
public class ThreadPool<T> {

    private Thread[] threads;
    private final LinkedList<ThreadPoolTask> taskQueue;

    /**
     * Creates a pool of tasks with fixed number of threads
     * @param numberOfThreads number of threads which will be available for tasks
     */
    public ThreadPool(int numberOfThreads) {
        threads = new Thread[numberOfThreads];
        taskQueue = new LinkedList<>();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new ThreadPoolWorker());
            threads[i].setDaemon(true);
            threads[i].start();
        }
    }

    /**
     * Add a new task for pool by its supplier
     * @param task a supplier function to run
     * @return task wrapper, which you can ask if task is ready, etc.
     */
    public LightFuture<T> addTask(Supplier<T> task) {
        ThreadPoolTask taskWrapper = new ThreadPoolTask(task);
        synchronized (taskQueue) {
            taskQueue.addLast(taskWrapper);
        }
        return taskWrapper;
    }

    /** Says to interrupt all the threads */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    private class ThreadPoolTask implements LightFuture<T> {

        private boolean ready = false;
        private Supplier<T> task;
        private T result = null;
        private Exception exceptionOccurred = null;

        private ThreadPoolTask(Supplier<T> task) {
            this.task = task;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isReady() {
            return ready;
        }

        /** {@inheritDoc} */
        @Override
        public T get() throws LightExecutionException {
            while (!ready) {
                Thread.yield();
            }

            if (exceptionOccurred != null) {
                throw new LightExecutionException(exceptionOccurred);
            }
            return result;
        }

        /** {@inheritDoc} */
        @Override
        public LightFuture<T> thenApply(Function<T, T> function) {
            return ThreadPool.this.addTask(() -> {
                try {
                    return function.apply(ThreadPoolTask.this.get());
                } catch (LightExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private class ThreadPoolWorker implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                ThreadPoolTask currentTask;
                synchronized (taskQueue) {
                    if (taskQueue.isEmpty()) {
                        continue;
                    }
                    currentTask = taskQueue.pollFirst();
                }

                if (currentTask == null) {
                    continue;
                }
                try {
                    currentTask.result = currentTask.task.get();
                } catch (Exception e) {
                    currentTask.exceptionOccurred = e;
                }
                currentTask.ready = true;
            }
        }
    }
}
