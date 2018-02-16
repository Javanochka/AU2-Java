package ru.spbau.nikiforovskaya.functional;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for function of two arguments.
 * @param <A> first argument type
 * @param <B> second argument type
 * @param <R> return value type
 */
abstract public class Function2<A, B, R> {

    /**
     * Apply function to the given arguments
     * @param arg1 first argument
     * @param arg2 second argument
     * @return return value of the function, applied to two arguments.
     */
    abstract public R apply(A arg1, B arg2);

    /**
     * Composition with one-argument function. Such as (fg)(x,y) = g(f(x,y))
     * @param before a function to apply before (g)
     * @param <V> a type of return value of before function and therefore the return value of composition.
     * @return a function of two arguments, which is a composition of two.
     */
    public <V> Function2<A, B, V> compose(@NotNull Function1<? super R, ? extends V> before) {
        return new Function2<A, B, V>() {
            @Override
            public V apply(A arg1, B arg2) {
                return before.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    /**
     * Fixes the first argument of the function, returns a one-argument function.
     * @param firstArg a value which to pass as a first argument
     * @return a new one argument function.
     */
    public Function1<B, R> bind1(A firstArg) {
        return new Function1<B, R>() {
            @Override
            public R apply(B secondArg) {
                return Function2.this.apply(firstArg, secondArg);
            }
        };
    }

    /**
     * Fixes the second argument of the function, returns a one-argument function.
     * @param secondArg a value which to pass as a second argument
     * @return a new one argument function.
     */
    public Function1<A, R> bind2(B secondArg) {
        return new Function1<A, R>() {
            @Override
            public R apply(A firstArg) {
                return Function2.this.apply(firstArg, secondArg);
            }
        };
    }

    /**
     * Transfers a function into one-argument function as bind2 did.
     * @param secondArg a value which to pass as a second argument
     * @return a new one argument function.
     */
    public Function1<A, R> curry(B secondArg) {
        return bind2(secondArg);
    }
}
