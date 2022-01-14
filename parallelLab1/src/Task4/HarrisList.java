package Task4;


import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

public class HarrisList<T extends Comparable<? super T>> {

    private Node<T> head = new Node<>(null, new AtomicReference<>(null));

    static class Node<T> {

        public T data;
        public AtomicReference<Node<T>> next;

        public Node(T data, AtomicReference<Node<T>> next) {
            this.data = data;
            this.next = next;
        }
    }

    public boolean pop(T data) {
        if (isNull(data)) {
            throw new IllegalArgumentException("Argument should not be null");
        }

        Node<T> prevElem = head;
        while (nonNull(prevElem.next.get())) {
            Node<T> currElem = prevElem.next.get();
            Node<T> nextElem = currElem.next.get();

            if (currElem.data.compareTo(data) == 0) {
                if (currElem.next.compareAndSet(nextElem, null) && prevElem.next.compareAndSet(currElem, nextElem)) {
                    return true;
                }
            } else {
                prevElem = currElem;
            }
        }

        return false;
    }

    public void push(T data) {
        if (isNull(data)) {
            throw new IllegalArgumentException("Argument should not be null");
        }

        Node<T> newElem = new Node<>(data, new AtomicReference<>(null));
        Node<T> currentElem = head;

        while (true) {
            Node<T> nextElem = currentElem.next.get();

            if (nonNull(nextElem)) {
                if (nextElem.data.compareTo(data) >= 0) {
                    newElem.next = new AtomicReference<>(nextElem);
                    if (currentElem.next.compareAndSet(nextElem, newElem)) {
                        return;
                    }
                } else {
                    currentElem = nextElem;
                }
            } else if (currentElem.next.compareAndSet(null, newElem)) {
                return;
            }
        }
    }

    public boolean contains(T data) {
        Node<T> currentElem = head.next.get();

        while (nonNull(currentElem)) {
            if (currentElem.data.compareTo(data) == 0) {
                return true;
            }

            currentElem = currentElem.next.get();
        }

        return false;
    }

    public void printNotSafe() {
        Node<T> current = head.next.get();
        while (nonNull(current)) {
            System.out.println(current.data);
            current = current.next.get();
        }
    }
}
