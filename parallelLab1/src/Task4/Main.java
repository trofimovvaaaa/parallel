package Task4;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        HarrisList<String> harrisList = new HarrisList<>();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new ThreadTest(harrisList));
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("Contains: " + harrisList.contains("Thread-1"));
        harrisList.printNotSafe();
    }
}
