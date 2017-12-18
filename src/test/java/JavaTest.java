import org.junit.Test;

/**
 * Created by TJP on 2017/10/18.
 */
public class JavaTest {
    public static void main(String[] args) {

    }

//    @sun.misc.Contended
//    static class AtomicLong {
//        private volatile long value;
//    }

    @Test
    public void testReference() {
        Object p = new Object();
        System.out.println("start p:" + p);
        /**
         * 方法里对引用p做修改，实际上是改的p的copy副本，本身p的引用没被修改
         */
        modify(p);
//        Object q = new Object();
//        p = q;
        System.out.println("end p:" + p);

    }

    @Test
    public void testReference1() {
        Long p = 1L;
        Long newP = p;
        System.out.println("p:" + p + " , new P :" + newP);

        p++;
        System.out.println("p:" + p + " , new P :" + newP);

    }

    @Test
    public void testReference2() {
        Long currentPage = 100l;
        int i = 0;
        do {
            i++;
        } while (currentPage++ < 100l + 20l - 1l);

        System.out.println(currentPage);
        System.out.println(i);

        Long curr = 119l;
        System.out.println(++curr < 20 + 100);
        System.out.println(curr);

    }

    /**
     * 对象作为方法参数，实际上是copy的引用
     *
     * @param p
     */
    private void modify(Object p) {
        System.out.println("method copy p:" + p);
        Object q = new Object();
        p = q;
        System.out.println("method modify p:" + p);
    }
}
