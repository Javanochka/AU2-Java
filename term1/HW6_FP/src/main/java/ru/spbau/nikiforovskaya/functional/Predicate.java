package ru.spbau.nikiforovskaya.functional;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for predicate for one argument. Accepts different
 * @param <A> type of predicate argument
 */
abstract public class Predicate<A> extends Function1<A, Boolean> {

    /**
     * Constant function, which to every argument return {@code true}.
     * @param <T> argument type
     * @return function, which to every argument return {@code true}.
     */
    public static <T> Predicate<T> ALWAYS_TRUE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return true;
            }
        };
    }

    /**
     * Constant function, which to every argument return {@code false}.
     * @param <T> argument type
     * @return function, which to every argument return {@code false}.
     */
    public static <T> Predicate<T> ALWAYS_FALSE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(Object arg) {
                return false;
            }
        };
    }

    /**
     * Returns logical or of two predicates.
     * @param other other predicate to make or with.
     * @return new predicate, which behaves as a logical or of two predicates.
     */
    public Predicate<A> or(@NotNull Predicate<A> other) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) || other.apply(arg);
            }
        };
    }

    /**
     * Returns logical and of two predicates.
     * @param other other predicate to make and with.
     * @return new predicate, which behaves as a logical and of two predicates.
     */
    public Predicate<A> and(@NotNull  Predicate<A> other) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) && other.apply(arg);
            }
        };
    }

    /**
     * Returns logical not of the predicate.
     * @return predicate, which behaves as logical not of the predicate.
     */
    public Predicate<A> not() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }
}
