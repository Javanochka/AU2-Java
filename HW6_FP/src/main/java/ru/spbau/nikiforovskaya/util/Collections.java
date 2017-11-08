package ru.spbau.nikiforovskaya.util;

import org.jetbrains.annotations.NotNull;
import ru.spbau.nikiforovskaya.functional.Function1;
import ru.spbau.nikiforovskaya.functional.Function2;
import ru.spbau.nikiforovskaya.functional.Predicate;

import java.util.ArrayList;
import java.util.Iterator;

/**  A class for working with functions and collections. */
public class Collections {

    /**
     * Applies given function to each element in the Iterable container
     * @param f a function to apply
     * @param list Iterable container, to which elements apply the function
     * @param <A> type of elements in the list
     * @param <R> type of return value of the function
     * @return a new list with result of applying function to the elements in the same order
     */
    public static <A, R> ArrayList<R> map(@NotNull Function1<? super A, R> f,
                                          @NotNull Iterable<A> list) {
        ArrayList<R> result = new ArrayList<>();
        for (A element : list) {
            result.add(f.apply(element));
        }
        return result;
    }

    /**
     * Leaves only those elements from Iterable list, predicate of which is {@code true}.
     * @param f a predicate to filter with
     * @param list an Iterable list which to filter
     * @param <A> a type of elements in the given list
     * @return a list of elements left in the same order
     */
    public static <A> ArrayList<A> filter(@NotNull Predicate<? super A> f,
                                          @NotNull Iterable<A> list) {
        ArrayList<A> result = new ArrayList<>();
        for (A element : list) {
            if (f.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Returns the biggest prefix of the Iterable list,
     * all elements of which return true on the given predicate.
     * @param f a predicate with which to check elements
     * @param list an Iterable list to process.
     * @param <A> a type of elements in the given list
     * @return the biggest prefix of the Iterable list,
     * all elements of which return true on the given predicate.
     */
    public static <A> ArrayList<A> takeWhile(@NotNull Predicate<? super A> f,
                                             @NotNull Iterable<A> list) {
        ArrayList<A> result = new ArrayList<>();
        for (A element : list) {
            if (!f.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    /**
     * Returns the biggest prefix of the Iterable list,
     * all elements of which return false on the given predicate.
     * @param f a predicate with which to check elements
     * @param list an Iterable list to process.
     * @param <A> a type of elements in the given list
     * @return the biggest prefix of the Iterable list,
     * all elements of which return false on the given predicate.
     */
    public static <A> ArrayList<A> takeUnless(@NotNull Predicate<? super A> f,
                                              @NotNull Iterable<A> list) {
        return takeWhile(f.not(), list);
    }

    /**
     * If elements in the Iterable list are in order a_1, a_2, a_3,...
     * And the start value is x, then result is f...f(f(f(x,a_1), a_2), a_3)...)
     * @param f a function, which to apply
     * @param startValue a value with which to start
     * @param list an Iterable list
     * @param <A> a type of start value and therefore return value
     * @param <B> a type of elements in the given list
     * @return the result of the expression f...f(f(f(x,a_1), a_2), a_3)...)
     */
    public static <A, B> A foldl(@NotNull Function2<? super A, ? super B, ? extends A> f,
                                 A startValue, @NotNull Iterable<B> list) {
        A result = startValue;
        for (B element : list) {
            result = f.apply(result, element);
        }
        return result;
    }

    private static <A, B> A foldr(@NotNull Function2<? super B, ? super A, ? extends A> f,
                                  A startValue, @NotNull Iterator<B> position) {
        if (!position.hasNext()) {
            return startValue;
        }
        return f.apply(position.next(), foldr(f, startValue, position));
    }

    /**
     * If elements in the Iterable list are in order a_1, a_2, a_3,...
     * And the start value is x, then result is f(x, f(a_1, f(a_2, ...)))
     * @param f a function, which to apply
     * @param startValue a value with which to start
     * @param list an Iterable list
     * @param <A> a type of start value and therefore return value
     * @param <B> a type of elements in the given list
     * @return the result of the expression f(x, f(a_1, f(a_2, ...)))
     */
    public static <A, B> A foldr(@NotNull Function2<? super B, ? super A, ? extends A> f,
                                 A startValue, @NotNull Iterable<B> list) {
        return foldr(f, startValue, list.iterator());
    }
}
