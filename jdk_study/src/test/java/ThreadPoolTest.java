import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tujinpeng on 2017/11/30.
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


    /**
     * <pre>
     *
     * 线程池状态和工作者线程参数说明:
     *
     * --两者用原子的int变量表示 :
     *
     *    0       00    00000000000000000000000000000
     *  符号位  线程池状态           工作者线程数
     *
     * --线程状态
     *
     *   RUNNING:       1 11 00000000000000000000000000000
     *   SHUTDOWN:      0 00 00000000000000000000000000000
     *   STOP:          0 01 00000000000000000000000000000
     *   TIDYING:       0 10 00000000000000000000000000000
     *   TERMINATED:    0 11 00000000000000000000000000000
     * --
     * </pre>
     */
    @Test
    public void testRunSateAndWorkCount() {
        //111后29位 负数
        System.out.println(Integer.toBinaryString(RUNNING));
        //000后29位 正数
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        //001后29位 正数
        System.out.println(Integer.toBinaryString(STOP));
        //010后29位 正数
        System.out.println(Integer.toBinaryString(TIDYING));
        //011后29位 正数
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

    @Test
    public void testException() {
        while (true) {
            try {
                int a = 0;
                Integer b = null;
                System.out.println(a == b);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                System.out.println("finally");
            }
        }
    }
}
