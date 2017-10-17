import org.junit.Test;

/**
 * Created by TJP on 2017/10/18.
 */
public class JavaTest {

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
