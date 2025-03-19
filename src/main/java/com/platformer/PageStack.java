package com.platformer;

/**
 * Used for tracking navigation with many pages. The {@link PageStack} uses
 * a stack data structure implemented with a linked list. Each page has an
 * {@code int} associated with it, which will be the number stored in the stack.
 *
 * @author Samuel Zhang
 */
public class PageStack {

    /**
     * Head {@link Node} of the stack
     */
    private Node head;

    /**
     * Adds a page to the stack.
     *
     * @param page {@code int} representation of the page
     */
    public void add(int page) {
        Node newPage = new Node(page);
        newPage.next = head;
        head = newPage;
    }

    /**
     * Pops the top page from the stack.
     */
    public void pop() {
        head = head.next;
    }

    /**
     * Returns the top of the stack.
     *
     * @return {@code int} representation of the top page
     */
    public int top() {
        return head.page;
    }

    /**
     * Clears the entire stack.
     */
    public void clear() {
        head = null;
    }

    /**
     * Inner helper class for the linked list implementation of the {@link PageStack}.
     *
     * @author Samuel Zhang
     */
    private static class Node {

        /**
         * {@code int} representation of page stored in the {@link Node}.
         */
        final int page;

        /**
         * The next {@link Node} in the linked list.
         */
        Node next;

        /**
         * Creates a new {@link Node} and saves the stored {@code int} representation of the page
         *
         * @param page {@code int} representation
         */
        Node(int page) {
            this.page = page;
        }

    }

}