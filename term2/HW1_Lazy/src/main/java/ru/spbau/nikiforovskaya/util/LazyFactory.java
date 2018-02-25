package ru.spbau.nikiforovskaya.util;

import java.util.function.Supplier;

/** Class with static methods for creating basic lazy realizations. */
public class LazyFactory {

    /**
     * Creates a simple lazy implementation, which is good for one-thread programs.
     * (without synchronization)
     * @param supplier a function to produce staff
     * @param <T> what to store in lazy
     * @return lazy object for one-thread cases.
     */
    public static <T> Lazy<T> createSimpleLazy(Supplier<T> supplier) {
        return new Lazy<T>() {

            private T result = null;
            private Supplier<T> produce = supplier;

            @Override
            public T get() {
                if (produce != null) {
                    result = produce.get();
                    produce = null;
                }
                return result;
            }
        };
    }

    /**
     * Creates a lazy implementation, which is good for multi-thread programs.
     * (with synchronization)
     * @param supplier a function to produce staff
     * @param <T> what to store in lazy
     * @return lazy object for multi-thread programs
     */
    public static <T> Lazy<T> createMultithreadLazy(Supplier<T> supplier) {
        return new Lazy<T>() {

            private T result = null;
            private Supplier<T> produce = supplier;

            @Override
            public T get() {
                if (produce == null) {
                    return result;
                }
                synchronized (this) {
                    if (produce != null) {
                        result = produce.get();
                        produce = null;
                    }
                }
                return result;
            }
        };
    }
}
