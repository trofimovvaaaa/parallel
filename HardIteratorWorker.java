package Task1;
public class HardIteratorWorker extends Thread {

    boolean started = false;
    boolean done = false;

    int count;
    HardIterator iterator;
    public HardIteratorWorker(HardIterator iterator, int count) {
        this.iterator = iterator;
        this.count = count;
    }
    @Override
    public void run() {
        started = true;
        for (int i = 0; i < this.count; ++i) {
            this.iterator.addWithCAS();
            // using add() without CAS instead should break the test:
            //  this.iterator.add();
        }
        done = true;
    }

    public static void waitFor(HardIteratorWorker worker) {
        do {
            try {
                Thread.sleep(100l);
            } catch (Exception e) {}
        } while (worker.started && !worker.done);
    }
}