import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by tujinpeng on 2017/9/12.
 */
public class QueueTest {
    public static void main(String[] args) throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>(true);

        Thread putThread = new Thread(new Runnable() {
            public void run() {
                System.out.println("put thread start");
                try {
                    queue.put(1);
                } catch (InterruptedException e) {
                }
                System.out.println("put thread end");
            }
        }, "SynchronousQueue-put-thread");

        Thread takeThread = new Thread(new Runnable() {
            public void run() {
                System.out.println("take thread start");
                try {
                    System.out.println("take from putThread: " + queue.take());
                } catch (InterruptedException e) {
                }
                System.out.println("take thread end");
            }
        }, "SynchronousQueue-take-thread");

        putThread.start();
        Thread.sleep(1000);
        takeThread.start();


        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());
        cachedPool.execute(new LightTask());

    }


    static class LightTask implements Runnable {
        public void run() {
            System.out.println("light task");

        }
    }
}
