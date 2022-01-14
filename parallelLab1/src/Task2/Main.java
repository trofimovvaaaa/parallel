package Task2;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        SkipList<String> skipList = new SkipList<>(16, 0.5);
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new ThreadTest(skipList));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("Contains: " + skipList.contain("Thread-1"));
        skipList.printNotSafe();

    }
}
