import com.tjp.design.proxy.ProxyHandler;
import com.tjp.design.proxy.RealSubject;
import com.tjp.design.proxy.Subject;
import com.tjp.utils.ProxyGeneratorUtils;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Created by tujinpeng on 2017/3/14.
 */
public class ProxyTest {
    /**
     * 动态代理
     */
    @Test
    public void dynamicProxy() {
        RealSubject real = new RealSubject();
        /**
         * 1.根据类加载器，目标接口，调用处理器，动态生成代理对象，
         * 继承Proxy（Proxy里关联InvocationHandler），实现目标接口Subject
         */
        Subject proxySubject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, new ProxyHandler(real));
        /**
         * 2.这里proxySubject代理对象执行dosomething，实际是调用ProxyHandler的invoke（）方法
         */
        proxySubject.dosomething();
    }

    /**
     * 代理类生成工具
     */
    @Test
    public void proxyGeneratorTest() {
        ProxyGeneratorUtils.writeProxyClassToHardDisk("$Proxy2", "F:/$Proxy2.class");
    }
}
