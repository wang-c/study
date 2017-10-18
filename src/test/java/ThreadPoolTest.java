import org.junit.Test;

/**
 * Created by tujinpeng on 2017/10/16.
 */
public class ThreadPoolTest {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }


    @Test
    public void testRunState(){
        System.out.println(workerCountOf(200));
        System.out.println(runStateOf(200));
        System.out.println(RUNNING);
        System.out.println(SHUTDOWN);
        System.out.println(STOP);
    }
}
