package com.platformer;

// imports
import java.util.*;

/**
 * A linked list of {@link Rectangle}s. The {@link Iterable} interface
 * is implemented, making for-each loops possible over this data structure.
 */
public class RectangleList implements Iterable<Rectangle> {

    /**
     * Head {@link Node} of the linked list.
     */
    private Node head;

    /**
     * Length of the {@link RectangleList}.
     */
    private int size;

    /**
     * Adds a {@link Rectangle} to the {@link RectangleList}.
     *
     * @param element {@link Rectangle} to add
     */
    public void add(Rectangle element) {
        size++;
        Node newNode = new Node(element);
        newNode.next = head;
        head = newNode;
    }

    /**
     * Clears the {@link RectangleList}.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * @return an iterator over the {@link RectangleList} with type {@link Rectangle}
     */
    @Override
    public Iterator<Rectangle> iterator() {
        return new Itr();
    }

    /**
     * Inner helper class for the linked list.
     */
    private static final class Node {

        /**
         * The {@link Rectangle} stored in the {@link Node}.
         */
        Rectangle data;

        /**
         * The next {@link Node} in the {@link RectangleList}.
         */
        Node next;

        /**
         * Creates a new {@link Node} and saves the stored {@link Rectangle}.
         *
         * @param data {@link Rectangle} to store
         */
        Node(Rectangle data) {
            this.data = data;
        }

    }

    /**
     * Inner class for the iterator of the {@link RectangleList}.
     */
    private final class Itr implements Iterator<Rectangle> {

        /**
         * The next {@link Node} in the iterator.
         */
        private Node nextNode = head;

        /**
         * The next index of the iterator.
         */
        private int nextIndex = 0;

        /**
         * @return if the iterator has a next {@link Node}
         */
        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        /**
         * @return next {@link Node} in the iterator
         * @throws NoSuchElementException if there are no more elements in the iterator
         */
        @Override
        public Rectangle next() {
            if (!hasNext()) throw new NoSuchElementException();

            nextIndex++;
            Rectangle data = nextNode.data;
            nextNode = nextNode.next;
            return data;
        }

    }

}