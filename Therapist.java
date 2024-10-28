import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class Therapist implements Runnable {
    private final BlockingQueue<Patient> patientQueue;
    private final BlockingQueue<Patient> mriQueue;
    private Patient previousPatient = null;

    public Therapist(BlockingQueue<Patient> patientQueue, BlockingQueue<Patient> mriQueue) {
        this.patientQueue = patientQueue;
        this.mriQueue = mriQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient patient = patientQueue.poll(100, TimeUnit.MILLISECONDS);
                if (patient == null) {
                    if (ClinicSimulation.noMorePatients && patientQueue.isEmpty()) {
                        break;
                    } else {
                        continue;
                    }
                }
                System.out.println("Пациент " + patient.getId() + " у терапевта");
                Thread.sleep(examinationTime());


                if (previousPatient != null) {
                    previousPatient.getLatch().await();
                }

                System.out.println("Пациент " + patient.getId() + " вышел от терапевта");
                mriQueue.put(patient);
                previousPatient = patient;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int examinationTime() {
        return new Random().nextInt(200) + 500;
    }
}
