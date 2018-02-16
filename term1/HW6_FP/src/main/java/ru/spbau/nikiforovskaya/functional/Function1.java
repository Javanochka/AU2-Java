package ru.spbau.nikiforovskaya.functional;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for function with one argument.
 * Can compose functions.
 * @param <A> argument type
 * @param <R> return value type
 */
abstract public class Function1<A, R> {

    /**
     * Apply function to the given argument
     * @param arg argument which to give to the function
     * @return return value of the function
     */
    abstract public R apply(A arg);

    /**
     * Composes current function and another. So that (fg) (x) = g(f(x))
     * @param before a function which to apply before (g in the previous sentence)
     * @param <V> return value of g, and therefore return value of the result function
     * @return a new function which is composition
     */
    public <V> Function1<A, V> compose(@NotNull Function1<? super R, ? extends V> before) {
        return new Function1<A, V>() {
            @Override
            public V apply(A arg) {
                return before.apply(Function1.this.apply(arg));
            }
        };
    }
}
