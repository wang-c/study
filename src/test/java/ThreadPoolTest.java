import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tujinpeng on 2017/10/16.
 */
public class ThreadPoolTest {

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }


    @Test
    public void testRunState() {
        System.out.println(Integer.toBinaryString(RUNNING));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        System.out.println(Integer.toBinaryString(STOP));
        System.out.println(Integer.toBinaryString(TIDYING));
        System.out.println(Integer.toBinaryString(TERMINATED));
        System.out.println("CAPACITY : " + Integer.toBinaryString(CAPACITY));
        System.out.println("~CAPACITY : " + Integer.toBinaryString(~CAPACITY));


        System.out.println("runState : " + runStateOf(ctl.get()));
        System.out.println("workerCount : " + workerCountOf(ctl.get()));


        int c = ctl.get();
        //改变worker count
        ctl.compareAndSet(c, c + 1);
        //改变state
        c = ctl.get();
        ctl.compareAndSet(c, ctlOf(SHUTDOWN, workerCountOf(c)));
        System.out.println("-------------change-------------");
        System.out.println("runState : " + runStateOf(ctl.get()));
        System.out.println("workerCount : " + workerCountOf(ctl.get()));

    }
}
