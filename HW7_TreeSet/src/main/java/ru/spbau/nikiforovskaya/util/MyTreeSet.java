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

    private Comparator<? super E> comparator;
    private Node root;
    private int size = 0;
    private boolean reverse = false;

    /**
     * Constructs MyTreeSet of elements with comparable type.
     * If some elements cannot be comparable to each other,
     * methods will throw ClassCastException.
     */
    public MyTreeSet() {
        comparator = (Comparator<E>) (o1, o2) -> castToComparable(o1).compareTo(o2);
    }

    /**
     * Constructs MyTreeSet with done comparator.
     * @param comparator to pass to the MyTreeSet.
     */
    public MyTreeSet(@NotNull Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    private MyTreeSet(@NotNull MyTreeSet<E> other, boolean reverse) {
        comparator = other.comparator;
        root = other.root;
        size = other.size;
        this.reverse = other.reverse ^ reverse;
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(@NotNull E value) {
        if (contains(value)) {
            return false;
        }
        Node i = root;
        Node parent = null;
        for (; i != null;) {
            parent = i;
            i = comparator.compare(i.value, value) > 0 ? i.left : i.right;
        }
        if (parent == null) {
            root = new Node(value, null);
            size++;
            return true;
        }

        if (comparator.compare(parent.value, value) > 0) {
            parent.left = new Node(value, parent);
        } else {
            parent.right = new Node(value, parent);
        }
        size++;
        return true;
    }

    private @Nullable Node find(@NotNull E value) {
        Node i = root;
        while (i != null) {
            int compareResult = comparator.compare(i.value, value);
            if (compareResult == 0) {
                return i;
            }
            i = compareResult > 0 ? i.left : i.right;
        }
        return null;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(@NotNull Object o) {
        E value;
        try {
            value = (E)o;
        } catch (ClassCastException e) {
            return false;
        }

        return find(value) != null;
    }

    private void removeNodeWithOnlyRightSon(@NotNull Node toRemove) {
        if (toRemove.parent == null) {
            root = toRemove.right;
        } else {
            if (toRemove.parent.left == toRemove) {
                toRemove.parent.left = toRemove.right;
            } else {
                toRemove.parent.right = toRemove.right;
            }
        }
        if (toRemove.right != null) {
            toRemove.right.parent = toRemove.parent;
        }
    }

    private void removeNodeWithOnlyLeftSon(@NotNull Node toRemove) {
        if (toRemove.parent == null) {
            root = toRemove.left;
        } else {
            if (toRemove.parent.right == toRemove) {
                toRemove.parent.right = toRemove.left;
            } else {
                toRemove.parent.left = toRemove.left;
            }
        }
        if (toRemove.right != null) {
            toRemove.right.parent = toRemove.parent;
        }
    }

    private void removeNode(@NotNull Node toRemove) {
        if (toRemove.left == null) {
            removeNodeWithOnlyRightSon(toRemove);
            return;
        }

        Node toReplace = toRemove.left;

        while (toReplace.right != null) {
            toReplace = toReplace.right;
        }

        removeNode(toReplace);
        removeNodeWithOnlyLeftSon(toReplace);
        toReplace.left = toRemove.left;
        toReplace.right = toRemove.right;
        if (toReplace.left != null) {
            toReplace.left.parent = toReplace;
        }
        if (toReplace.right != null) {
            toReplace.right.parent = toReplace;
        }
        if (toRemove.parent == null) {
            root = toReplace;
            return;
        }
        if (toRemove.parent.left == toRemove) {
            toRemove.parent.left = toReplace;
        } else {
            toRemove.parent.right = toReplace;
        }
        toReplace.parent = toRemove.parent;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(@NotNull Object o) {
        Node toRemove = find((E)o);

        if (toRemove == null) {
            return false;
        }
        removeNode(toRemove);
        size--;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> iterator() {
        return new MyTreeSetIterator(false);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return size;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull Iterator<E> descendingIterator() {
        return new MyTreeSetIterator(true);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull ru.spbau.litvinov.MyTreeSet<E> descendingSet() {
        return new MyTreeSet<>(this, true);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E first() {
        if (root == null) {
            return null;
        }
        Node i = root;
        while (getLeft(i, reverse) != null) {
            i = getLeft(i, reverse);
        }
        return i.value;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E last() {
        if (root == null) {
            return null;
        }
        Node i = root;
        while (getRight(i, reverse) != null) {
            i = getRight(i, reverse);
        }
        return i.value;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E lower(@NotNull E e) {
        Node i = root;
        E answer = null;
        while (i != null) {
            if (compareWithReverse(e, i.value, reverse) <= 0) {
                i = getLeft(i, reverse);
            } else {
                if (answer == null || compareWithReverse(answer, i.value, reverse) < 0) {
                    answer = i.value;
                }
                i = getRight(i, reverse);
            }
        }

        return answer;
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E floor(@NotNull E e) {
        if (contains(e)) {
            return e;
        }
        return lower(e);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E ceiling(@NotNull E e) {
        if (contains(e)) {
            return e;
        }
        return higher(e);
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable E higher(@NotNull E e) {
        Node i = root;
        E answer = null;
        while (i != null) {
            if (compareWithReverse(e, i.value, reverse) < 0) {
                if (answer == null || compareWithReverse(answer, i.value, reverse) > 0) {
                    answer = i.value;
                }
                i = getLeft(i, reverse);
            } else {
                i = getRight(i, reverse);
            }
        }
        return answer;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    private @NotNull Comparable<E> castToComparable(@NotNull E e) {
        return (Comparable<E>)e;
    }

    private class Node {
        private E value;
        private Node left;
        private Node right;
        private Node parent;

        private Node(E value, Node parent) {
            this.value = value;
            this.parent = parent;
        }
    }

    private class MyTreeSetIterator implements Iterator<E> {

        Node current = null;
        Node end = null;
        boolean reverse = MyTreeSet.this.reverse;

        private MyTreeSetIterator(boolean reverse) {
            this.reverse ^= reverse;
            if (MyTreeSet.this.root != null) {
                end = rightMost(MyTreeSet.this.root);
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return current != end;
        }

        /** {@inheritDoc} */
        @Override
        public @NotNull E next() {
            if (current == null) {
                current = leftMost(MyTreeSet.this.root);
                return current.value;
            }
            Node rightCurrent = getRight(current, reverse);
            if (rightCurrent == null) {
                current = getFirstBiggerAncient(current);
            } else {
                current = leftMost(rightCurrent);
            }
            return current.value;
        }

        private @NotNull Node getFirstBiggerAncient(@NotNull Node i) {
            while (getLeft(i.parent, reverse) != i) {
                i = i.parent;
            }
            return i.parent;
        }

        private @NotNull Node rightMost(@NotNull Node i) {
            while (getRight(i, reverse) != null) {
                i = getRight(i, reverse);
            }
            return i;
        }

        private @NotNull Node leftMost(@NotNull Node i) {
            while (getLeft(i, reverse) != null) {
                i = getLeft(i, reverse);
            }
            return i;
        }
    }

    private @Nullable Node getLeft(@NotNull Node i, boolean reverse) {
        return reverse ? i.right : i.left;
    }

    private @Nullable Node getRight(@NotNull Node i, boolean reverse) {
        return reverse ? i.left : i.right;
    }

    private int compareWithReverse(@NotNull E a, @NotNull E b, boolean reverse) {
        return reverse ? comparator.compare(b, a) : comparator.compare(a, b);
    }
}
