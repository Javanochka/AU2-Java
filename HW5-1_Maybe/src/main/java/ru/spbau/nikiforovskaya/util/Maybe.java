package ru.spbau.nikiforovskaya.util;

import java.util.function.Function;

/**
 * A wrapper class for one element, helping to avoid appealing to NULL elements.
 * In addition, it has map method, helping to apply some function to that element.
 * @param <T> a class of element you want to store.
 */
public class Maybe<T> {

    private T data;

    private Maybe(T object) {
        data = object;
    }

    /**
     * Creates a new Maybe object, containing given element.
     * @param t an element you want to store
     * @param <U> a class of element you want to store
     * @return a new Maybe object.
     */
    public static <U> Maybe<U> just(U t) {
        return new Maybe<U>(t);
    }

    /**
     * Creates a new Maybe object, containing nothing.
     * @param <U> a class of element
     * @return a new Maybe object, containing nothing.
     */
    public static <U> Maybe<U> nothing() {
        return new Maybe<U>(null);
    }

    /**
     * Returns an object stored inside, if it exists.
     * @return an object stored inside, if it exists
     * @throws MaybeException if a wrapper contains nothing.
     */
    public T get() throws MaybeException {
        if (data == null) {
            throw new MaybeException("No value stored in Maybe");
        }
        return data;
    }

    /**
     * Checks if there is object stored inside the wrapper.
     * @return {@code true} if object exists, {@code false} otherwise.
     */
    public boolean isPresent() {
        return data != null;
    }

    /**
     * Applies a given function to the object, stored in wrapper.
     * @param mapper a function to apply
     * @return result of function application wrapped in Maybe if object exists in wrapper,
     * or {@code nothing} if object doesn't exist.
     */
    public Maybe<T> map(Function<? super T, ? extends T> mapper) {
        if (data == null) {
            return nothing();
        }
        return new Maybe<T>(mapper.apply(data));
    }
}
