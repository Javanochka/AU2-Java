package ru.spbau.nikiforovskaya.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Binary search tree realization.
 * @param <E> a type of elements to store in the tree.
 */
public class BinarySearchTree<E> {

    private Comparator<? super E> comparator;
    private Node root;
    private int size = 0;

    /**
     * Construct new tree with the given comparator on the elements.
     * @param comparator which to use when comparing elements
     */
    public BinarySearchTree(@NotNull Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Adds new element to the tree.
     * @param value to add into the tree.
     * @return {@code true}, if there was not such element before,
     * {@code false} otherwise.
     */
    public boolean add(@NotNull E value) {
        if (contains(value)) {
            return false;
        }
        Node i = root;
        Node parent = null;
        while (i != null) {
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

    /**
     * Check if the given element is stored in the tree.
     * @param o element to check
     * @return {@code true}, if there is such element, {@code false} otherwise.
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Removes element from the tree.
     * @param o element to remove.
     * @return {@code true} if there was such element, {@code false} otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean remove(@NotNull Object o) {
        Node toRemove = find((E)o);

        if (toRemove == null) {
            return false;
        }
        removeNode(toRemove);
        size--;
        return true;
    }

    /**
     * Returns iterator over the tree.
     * @return iterator over the tree.
     */
    public @NotNull Iterator<E> iterator() {
        return new BinarySearchTreeIterator(false);
    }

    /**
     * Returns number of elements in the tree.
     * @return number of elements in the tree.
     */
    public int size() {
        return size;
    }

    /**
     * Returns descending iterator.
     * @return iterator in descending order
     */
    public @NotNull Iterator<E> descendingIterator() {
        return new BinarySearchTreeIterator(true);
    }

    /**
     * Returns the first element in the tree.
     * @return first (the least) element in the tree.
     */
    public @Nullable E first() {
        if (root == null) {
            return null;
        }
        Node i = root;
        while (i.left != null) {
            i = i.left;
        }
        return i.value;
    }

    /**
     * Returns the last element in the tree.
     * @return last (the biggest) element in the tree.
     */
    public @Nullable E last() {
        if (root == null) {
            return null;
        }
        Node i = root;
        while (i.right != null) {
            i = i.right;
        }
        return i.value;
    }

    /**
     * Returns the biggest element which is smaller than the given one.
     * @param e an element with which to compare
     * @return the biggest element which is smaller than the given one.
     */
    public @Nullable E lower(@NotNull E e) {
        Node i = root;
        E answer = null;
        while (i != null) {
            if (comparator.compare(e, i.value) <= 0) {
                i = i.left;
            } else {
                if (answer == null || comparator.compare(answer, i.value) < 0) {
                    answer = i.value;
                }
                i = i.right;
            }
        }
        return answer;
    }

    /**
     * Returns the biggest element which is smaller or equals to the given one.
     * @param e an element with which to compare
     * @return the biggest element which is smaller or equals to the given one.
     */
    public @Nullable E floor(@NotNull E e) {
        if (contains(e)) {
            return e;
        }
        return lower(e);
    }

    /**
     * Returns the smallest element which is bigger or equals to the given one.
     * @param e an element with which to compare
     * @return the smallest element which is bigger or equals to the given one.
     */
    public @Nullable E ceiling(@NotNull E e) {
        if (contains(e)) {
            return e;
        }
        return higher(e);
    }

    /**
     * Returns the smallest element which is bigger than the given one.
     * @param e an element with which to compare
     * @return the smallest element which is bigger than the given one.
     */
    public @Nullable E higher(@NotNull E e) {
        Node i = root;
        E answer = null;
        while (i != null) {
            if (comparator.compare(e, i.value) < 0) {
                if (answer == null || comparator.compare(answer, i.value) > 0) {
                    answer = i.value;
                }
                i = i.left;
            } else {
                i = i.right;
            }
        }
        return answer;
    }

    /**
     * Returns array of the elements in the tree.
     * @return array of the elements in the tree.
     */
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        E[] result = (E[])new Object[size];
        int i = 0;
        for (@NotNull Iterator<E> it = iterator(); it.hasNext(); ) {
            E e = it.next();
            result[i++] = e;
        }
        return result;
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

    private class BinarySearchTreeIterator implements Iterator<E> {

        private Node current = null;
        private Node end = null;
        private boolean reverse;

        private BinarySearchTreeIterator(boolean reverse) {
            this.reverse = reverse;
            if (BinarySearchTree.this.root != null) {
                end = rightMost(BinarySearchTree.this.root);
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
                current = leftMost(BinarySearchTree.this.root);
                return current.value;
            }
            Node rightCurrent = getRight(current);
            if (rightCurrent == null) {
                current = getFirstBiggerAncient(current);
            } else {
                current = leftMost(rightCurrent);
            }
            return current.value;
        }

        private @NotNull Node getFirstBiggerAncient(@NotNull Node i) {
            while (getLeft(i.parent) != i) {
                i = i.parent;
            }
            return i.parent;
        }

        private @NotNull Node rightMost(@NotNull Node i) {
            while (getRight(i) != null) {
                i = getRight(i);
            }
            return i;
        }

        private @NotNull Node leftMost(@NotNull Node i) {
            while (getLeft(i) != null) {
                i = getLeft(i);
            }
            return i;
        }

        private @Nullable Node getLeft(@NotNull Node i) {
            return reverse ? i.right : i.left;
        }

        private @Nullable Node getRight(@NotNull Node i) {
            return reverse ? i.left : i.right;
        }

    }
}
