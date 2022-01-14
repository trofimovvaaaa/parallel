package Task3;

public class Main {

    public static void main(String[] args) {
        Queue queue = new Task3.Queue();

        queue.push(1);

        QueuePusher pusher = new QueuePusher(queue, 25);
        QueuePopper popper = new QueuePopper(queue, 15);

        pusher.start();
        popper.start();

        QueuePusher.waitFor(pusher);
        QueuePopper.waitFor(popper);

        System.out.println(queue.count() == (1 + 25 - 15)); // must be true
    }

    public static class QueuePusher extends Thread {
        private boolean started = false;
        private boolean done = false;

        private Queue queue;
        private int count;
        public QueuePusher(Queue queue, int count) {
            this.queue = queue;
            this.count = count;
        }

        @Override
        public void run() {
            started = true;
            for (int i = 0; i < this.count; ++i)
                this.queue.push(i);
            done = true;
        }

        public static void waitFor(QueuePusher pusher) {
            do {
                try {
                    Thread.sleep(100l);
                } catch (Exception e) {}
            } while (pusher.started && !pusher.done);
        }
    }

    public static class QueuePopper extends Thread {
        private boolean started = false;
        private boolean done = false;

        private Queue queue;
        private int count;
        public QueuePopper(Queue queue, int count) {
            this.queue = queue;
            this.count = count;
        }

        @Override
        public void run() {
            started = true;
            for (int i = 0; i < this.count; ++i)
                this.queue.pop();
            done = true;
        }

        public static void waitFor(QueuePopper popper) {
            do {
                try {
                    Thread.sleep(100l);
                } catch (Exception e) {}
            } while (popper.started && !popper.done);
        }
    }
}
