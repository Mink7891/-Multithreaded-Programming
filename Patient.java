import java.util.concurrent.CountDownLatch;

class Patient {
    private final int id;
    private final CountDownLatch latch;

    public Patient(int id) {
        this.id = id;
        this.latch = new CountDownLatch(1);
    }

    public int getId() {
        return id;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
