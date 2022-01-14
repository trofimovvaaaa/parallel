package Task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SkipList<T extends Comparable<? super T>> {

    private int height;
    private double p;
    private Node<T> head;

    static class Node<T> {

        public T data;
        public AtomicReference<Node<T>> right;
        public Node<T> down;

        public Node(T data, AtomicReference<Node<T>> right, Node<T> down) {
            this.data = data;
            this.right = right;
            this.down = down;
        }
    }

    public SkipList(int height, double p) {
        this.height = height;
        this.p = p;

        Node<T> elem = new Node<>(null, new AtomicReference<>(null), null);
        head = elem;

        for (int i = 0; i < height - 1; i++) {
            Node<T> newHeadElem = new Node<>(null, new AtomicReference<>(null), null);
            elem.down = newHeadElem;
            elem = newHeadElem;
        }
    }

    public boolean pop(T data) {
        if (isNull(data)) {
            throw new IllegalArgumentException("Argument should not be null");
        }

        Node<T> currElem = head;
        int currLevel = height;
        boolean towerUnmarked = true;

        while (currLevel > 0) {
            Node<T> rightElem = currElem.right.get();
            if (nonNull(rightElem) && rightElem.data.compareTo(data) == 0) {
                Node<T> afterrightElemem = rightElem.right.get();
                if (towerUnmarked) {
                    Node<T> towerElem = rightElem;
                    while (nonNull(towerElem)) {
                        towerElem.right.compareAndSet(towerElem.right.get(), null);
                        towerElem = towerElem.down;
                    }
                    towerUnmarked = false;
                }

                currElem.right.compareAndSet(rightElem, afterrightElemem);
            }

            if (nonNull(rightElem) && rightElem.data.compareTo(data) < 0) {
                currElem = rightElem;
            } else {
                currElem = currElem.down;
                currLevel--;
            }
        }

        return !towerUnmarked;
    }

    public boolean push(T data) {
        if (isNull(data)) {
            throw new IllegalArgumentException("Argument should not be null");
        }

        List<Node<T>> prev = new ArrayList<>();
        List<Node<T>> prevRight = new ArrayList<>();
        Node<T> currElem = head;
        int levelTower = generHeight();
        int currLevel = height;

        while (currLevel > 0) {
            Node<T> rightElem = currElem.right.get();

            if (currLevel <= levelTower) {
                if (isNull(rightElem) || rightElem.data.compareTo(data) >= 0) {
                    prev.add(currElem);
                    prevRight.add(rightElem);
                }
            }

            if (nonNull(rightElem) && rightElem.data.compareTo(data) < 0) {
                currElem = rightElem;
            } else {
                currElem = currElem.down;
                currLevel--;
            }
        }

        Node<T> downEl = null;
        for (int i = prev.size() - 1; i >= 0; i--) {
            Node<T> newEl = new Node<>(data, new AtomicReference<>(prevRight.get(i)), null);

            if (nonNull(downEl)) {
                newEl.down = downEl;
            }

            if (!prev.get(i).right.compareAndSet(prevRight.get(i), newEl) && i == prev.size() - 1) {
                return false;
            }

            downEl = newEl;
        }

        return true;
    }

    public boolean contain(T data) {
        Node<T> currElem = head;

        while (nonNull(currElem)) {
            Node<T> rightElem = currElem.right.get();
            if (nonNull(currElem.data) && currElem.data.compareTo(data) == 0) {
                return true;
            }
            else if (nonNull(rightElem) && rightElem.data.compareTo(data) <= 0) {
                currElem = rightElem;
            } else {
                currElem = currElem.down;
            }
        }

        return false;
    }

    public void printNotSafe() {
        Node<T> curr = head;

        while (nonNull(curr.down)) {
            curr = curr.down;
        }

        curr = curr.right.get();

        while (nonNull(curr)) {
            System.out.println(curr.data);
            curr = curr.right.get();
        }
    }

    private int generHeight() {
        int lvl = 1;

        while (lvl < height && Math.random() < p) {
            lvl++;
        }

        return lvl;
    }
}