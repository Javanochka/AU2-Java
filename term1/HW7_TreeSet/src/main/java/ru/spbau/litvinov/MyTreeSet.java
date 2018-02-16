package ru.spbau.litvinov;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public interface MyTreeSet<E> extends Set<E> {

    /**
     * Returns descending iterator as
     * {@link  TreeSet#descendingIterator()} does.
     * @return iterator in descending order
     */
    Iterator<E> descendingIterator();

    /**
     * Returns descending set as
     * {@link TreeSet#descendingSet()} does.
     * @return set in descending order
     */
    MyTreeSet<E> descendingSet();


    /**
     * Returns the first element in the set as
     * {@link TreeSet#first()} does
     * @return first (the least) element in the set
     */
    E first();

    /**
     * Returns the last element in the set as
     * {@link TreeSet#last()} does
     * @return last (the biggest) element in the set
     */
    E last();


    /**
     * Returns the biggest element which is smaller than the given one as
     * {@link TreeSet#lower(Object)} does.
     * @param e an element with which to compare
     * @return the biggest element which is smaller than the given one.
     */
    E lower(E e);

    /**
     * Returns the biggest element which is smaller or equals to the given one as
     * {@link TreeSet#floor(Object)} does.
     * @param e an element with which to compare
     * @return the biggest element which is smaller or equals to the given one.
     */
    E floor(E e);


    /**
     * Returns the smallest element which is bigger or equals to the given one as
     * {@link TreeSet#ceiling(Object)} does.
     * @param e an element with which to compare
     * @return the smallest element which is bigger or equals to the given one.
     */
    E ceiling(E e);

    /**
     * Returns the smallest element which is bigger than the given one as
     * {@link TreeSet#higher(Object)} does.
     * @param e an element with which to compare
     * @return the smallest element which is bigger than the given one.
     */
    E higher(E e);
}