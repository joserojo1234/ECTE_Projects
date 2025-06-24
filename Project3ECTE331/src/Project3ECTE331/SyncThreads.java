package Project3ECTE331;

import java.util.concurrent.CountDownLatch;

public class SyncThreads {
    static int valA1, valA2, valA3, valB1, valB2, valB3;

    static CountDownLatch latchA1 = new CountDownLatch(1);
    static CountDownLatch latchA2 = new CountDownLatch(1);
    static CountDownLatch latchB1 = new CountDownLatch(1);
    static CountDownLatch latchB2 = new CountDownLatch(1);
    static CountDownLatch latchB3 = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        Runnable taskA = () -> {
            valA1 = SumUtils.sum(500);
            latchA1.countDown();

            try { latchB2.await(); } catch (InterruptedException e) {}
            valA2 = valB2 + SumUtils.sum(300);
            latchA2.countDown();

            try { latchB3.await(); } catch (InterruptedException e) {}
            valA3 = valB3 + SumUtils.sum(400);
        };

        Runnable taskB = () -> {
            valB1 = SumUtils.sum(250);
            latchB1.countDown();

            try { latchA1.await(); } catch (InterruptedException e) {}
            valB2 = valA1 + SumUtils.sum(200);
            latchB2.countDown();

            try { latchA2.await(); } catch (InterruptedException e) {}
            valB3 = valA2 + SumUtils.sum(400);
            latchB3.countDown();
        };

        Runnable taskC = () -> {
            try { latchB3.await(); } catch (InterruptedException e) {}
            int finalOutput = valA2 + valB3;
            System.out.println("Thread C Output (A2 + B3): " + finalOutput);
        };

        Thread thread1 = new Thread(taskA);
        Thread thread2 = new Thread(taskB);
        Thread thread3 = new Thread(taskC);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}
