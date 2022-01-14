package Task1;
public class HardIterator {
    public int i;
    public HardIterator() {
        this(0);
    }
    public HardIterator(int i) {
        this.i = i;
    }

    // Naive approach, without CAS
    public void add() {
        int wasI = i;

        wasI++;

        // Simulating hard work timeout to cause an error
        try {
            Thread.sleep(10l);
        } catch (Exception e) {}

        i = wasI;
    }

    // Compare-And-Swap approach
    synchronized public void addWithCAS() {
        while (true) {
            int oldValue = i;
            int newValue = oldValue + 1;

            // Simulating hard work timeout to cause an error
            try {
                Thread.sleep(10l);
            } catch (Exception e) {}

            // if value stayed the same,
            if (i == oldValue) {
                // then apply a new value
                i = newValue;
                break;
            } // else, try again
        }
    }
}