import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/5/27
 * Time: 17:12
 * Email:tujinpeng@lvmama.com
 */
public class MainTest {

    /**
     * volatile保证修改后对其他线程立马可见
     */
    private volatile static boolean stop;

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(new Runnable() {
            public void run() {
                while (!stop) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("sub thread stop ");
            }

        }, "work-thread-01");


        t.start();

        TimeUnit.SECONDS.sleep(3);

        stop = true;
    }
}
