import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class MRI implements Runnable {
    private final BlockingQueue<Patient> mriQueue;

    public MRI(BlockingQueue<Patient> mriQueue) {
        this.mriQueue = mriQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient patient = mriQueue.poll(100, TimeUnit.MILLISECONDS);
                if (patient == null) {
                    if (ClinicSimulation.noMorePatients && mriQueue.isEmpty()) {
                        break;
                    } else {
                        continue;
                    }
                }
                System.out.println("Пациент " + patient.getId() + " на МРТ");
                Thread.sleep(scanTime());
                System.out.println("Пациент " + patient.getId() + " вышел с МРТ");
                patient.getLatch().countDown();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int scanTime() {
        return new Random().nextInt(200) + 500;
    }
}
