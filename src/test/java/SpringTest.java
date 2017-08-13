import com.tjp.framework.spring.aop.Bussiness;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/3/15
 * Time: 15:24
 * Email:tujinpeng@lvmama.com
 */
public class SpringTest {

    @Test
    public void iocTest() {
        BeanFactory factory=new ClassPathXmlApplicationContext("applicationContext.xml");
        Bussiness productBussiness=(Bussiness) factory.getBean("productBussiness");
        Bussiness orderBussiness=(Bussiness) factory.getBean("orderBussiness");
        productBussiness.dosomething();
        orderBussiness.dosomething();
    }

    @Test
    public void chuTest() {
        System.out.println(3&4);
    }
}
