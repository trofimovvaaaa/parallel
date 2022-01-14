package Task3;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class Queue {
    private class Node {
        Integer value;
        AtomicReference<Node> next;
        public Node(Integer value, AtomicReference<Node> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node dummy;
    private AtomicReference<Node> headRef;
    private AtomicReference<Node> tailRef;
    public Queue() {
        dummy = new Node(null, new AtomicReference<>(null));
        headRef = new AtomicReference<>(dummy);
        tailRef = new AtomicReference<>(dummy);
    }

    private boolean CAS(
            AtomicReference<Node> pointer,
            Node oldNode,
            Node newNode
    ) {
        if (pointer.get() != oldNode)
            return false;
        pointer.set(newNode);
        return true;
    }

    public void push(Integer value) {
        // додавання елемента
        Node newTail = new Node(value, new AtomicReference<>(null));
        while (true) {
            Node tail = tailRef.get();
            if (CAS(tail.next, null, newTail)) {
                CAS(tailRef, tail, newTail);
                return;
            } else {
                CAS(tailRef, tail, tail.next.get());
            }
        }
    }

    public Integer pop() throws NoSuchElementException {
        // видалення елемента
        while (true) {
            Node head = headRef.get();
            Node tail = tailRef.get();
            Node nextHead = head.next.get();
            if (head == tail) {
                if (nextHead == null) {
                    throw new NoSuchElementException();
                } else {
                    CAS(tailRef, tail, nextHead);
                }
            } else {
                Integer result = nextHead.value;
                if (CAS(headRef, head, nextHead))
                    return result;
            }
        }
    }

    public boolean isEmpty() {
        return headRef.get() == tailRef.get();
    }
    public int count() {
        int count = -1; // except dummy node
        Node iterator = headRef.get();
        while (iterator != null) {
            count++;
            iterator = iterator.next.get();
        }
        return count;
    }
}
