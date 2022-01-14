package Task1;
class Main {
    public static void main(String[] args) {
        HardIterator iterator = new HardIterator();

        HardIteratorWorker hardIteratorWorker1 = new HardIteratorWorker(iterator, 10);
        HardIteratorWorker hardIteratorWorker2 = new HardIteratorWorker(iterator, 20);
        HardIteratorWorker hardIteratorWorker3 = new HardIteratorWorker(iterator, 30);

        hardIteratorWorker1.start();
        hardIteratorWorker2.start();
        hardIteratorWorker3.start();

        HardIteratorWorker.waitFor(hardIteratorWorker1);
        HardIteratorWorker.waitFor(hardIteratorWorker2);
        HardIteratorWorker.waitFor(hardIteratorWorker3);

        System.out.println(iterator.i == (10 + 20 + 30)); // must be true
        System.out.println(iterator.i);
    }
}