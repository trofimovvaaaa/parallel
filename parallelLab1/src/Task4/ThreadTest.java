package Task4;

public class ThreadTest implements Runnable {

    private HarrisList<String> harrisList;

    public ThreadTest(HarrisList<String> harrisList) {
        this.harrisList = harrisList;
    }

    @Override
    public void run() {
        String currThreadName = Thread.currentThread().getName();
        harrisList.push(currThreadName);

        if (currThreadName.equals("Thread-4")) {
            System.out.println("Remove: " + harrisList.pop(currThreadName));
        }
    }
}
