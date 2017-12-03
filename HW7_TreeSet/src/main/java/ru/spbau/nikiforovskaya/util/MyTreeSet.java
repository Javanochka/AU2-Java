package ru.spbau.nikiforovskaya.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

/**
 * A set realization with binary search tree.
 * Supports TreeSet operations like, first, last, etc.
 * @param <E> a type of elements to store in the tree set.
 */
public class MyTreeSet<E> extends AbstractSet<E> implements ru.spbau.litvinov.MyTreeSet<E> {

    private BinarySearchTree<E> tree;
    private boolean reverse = false;

    /**
     * Constructs MyTreeSet of elements with comparable type.
     * If some elements cannot be comparable to each other,
     * methods will throw ClassCastException.
     */
    public MyTreeSet() {
        Comparator<? super E> comparator = (Comparator<E>)
                (o1, o2) -> castToComparable(o1).compareTo(o2);
        tree = new BinarySearchTree<>(comparator);
    }

    /**
     * Constructs MyTreeSet with done comparator.
     * @param comparator to pass to the MyTreeSet.
     */
    public MyTreeSet(@NotNull Comparator<? super E> comparator) {
        tree = new BinarySearchTree<>(comparator);
    }

    private MyTreeSet(@NotNull MyTreeSet<E> other, boolean reverse) {
        tree = other.tree;
        this.reverse = other.reverse ^ reverse;
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(@NotNull E value) {
        return tree.add(value);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(@NotNull Object o) {
        return tree.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(@NotNull Object o) {
        return tree.remove(o);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> iterator() {
        if (reverse) {
            return tree.descendingIterator();
        }
        return tree.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return tree.size();
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> descendingIterator() {
        if (!reverse) {
            return tree.descendingIterator();
        }
        return tree.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull ru.spbau.litvinov.MyTreeSet<E> descendingSet() {
        return new MyTreeSet<>(this, true);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E first() {
        if (reverse) {
            return tree.last();
        }
        return tree.first();
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E last() {
        if (reverse) {
            return tree.first();
        }
        return tree.last();
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E lower(@NotNull E e) {
        if (reverse) {
            return tree.higher(e);
        }
        return tree.lower(e);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E floor(@NotNull E e) {
        if (reverse) {
            return tree.ceiling(e);
        }
        return tree.floor(e);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E ceiling(@NotNull E e) {
        if (!reverse) {
            return tree.ceiling(e);
        }
        return tree.floor(e);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E higher(@NotNull E e) {
        if (!reverse) {
            return tree.higher(e);
        }
        return tree.lower(e);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    private @NotNull Comparable<E> castToComparable(@NotNull E e) {
        return (Comparable<E>) e;
    }
}
