package org.example;

import java.util.Iterator;
import java.util.function.Consumer;

public class CustomLinkedList<E> implements Iterable<E> {

    private Node<E> first;

    private Node<E> last;

    private int size;

    public CustomLinkedList() {}

    public void add(E value) {
        Node<E> newNode = new Node<>(value);
        if (first == null)
            first = newNode;
        else
            last.next = newNode;

        last = newNode;
        size++;
    }

    public E get(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: %s, Size: %s".formatted(index, size));

        Node<E> cur = first;

        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }

        return cur.value;
    }

    public E remove(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: %s, Size: %s".formatted(index, size));

        Node<E> cur = first;
        Node<E> prev = null;

        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.next;
        }
        if (prev == null) // index == 0
            first = first.next;
        else
            prev.next = cur.next;
        size--;

        return cur.value;
    }

    public boolean remove(E value) {
        Node<E> cur = first;
        Node<E> prev = null;
        if (value == null) {
            for (; cur != null; prev = cur, cur = cur.next) {
                if (cur.value == null) {
                    if (prev == null)
                        first = first.next;
                    else
                        prev.next = cur.next;
                    size--;

                    return true;
                }
            }
        } else {
            for (; cur != null; prev = cur, cur = cur.next) {
                if (cur.value.equals(value)) {
                    if (prev == null)
                        first = first.next;
                    else
                        prev.next = cur.next;
                    size--;

                    return true;
                }
            }
        }

        return false;
    }

    public boolean contains(E value) {
        Node<E> cur = first;
        if (value == null) {
            while (cur != null) {
                if (cur.value == null)
                    return true;

                cur = cur.next;
            }
        } else {
            while (cur != null) {
                if (cur.value.equals(value))
                    return true;

                cur = cur.next;
            }
        }

        return false;
    }

    public int size() {
        return size;
    }

    public void addAll(Iterable<? extends E> elements) {
        for (E e : elements) {
            add(e);
        }
    }

    public void reverse() {
        Node<E> cur = first;
        Node<E> prev = null;
        while (cur != null) {
            Node<E> nxt = cur.next;
            cur.next = prev;
            prev = cur;
            cur = nxt;
        }
        first = prev;
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<E> {
        private Node<E> cur = first;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public E next() {
            E value = cur.value;
            cur = cur.next;
            return value;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }
    }


    @Override
    public String toString() {
        if (first == null)
            return "[]";

        StringBuilder sb = new StringBuilder();
        Node<E> cur = first;

        sb.append('[');
        while (cur != null) {
            sb.append(cur).append(", ");
            cur = cur.next;
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }


    private static class Node<E> {
        E value;
        Node<E> next;
        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

}