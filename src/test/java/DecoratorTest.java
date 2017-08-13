import com.tjp.design.decorator.*;
import org.junit.Test;

/**
 * Created by tujinpeng on 2017/5/14.
 */
public class DecoratorTest {

    @Test
    public void decoratorTest() {
        //1.申明被装饰者烘焙咖啡
        Beverage darkRoast = new DarkRoast();
        //2.调料装饰者装饰
        //咖啡加摩卡
        darkRoast = new MochaDecorator(darkRoast);
        //咖啡加糖
        darkRoast = new SugarDecorator(darkRoast);
        //咖啡加奶油
        darkRoast = new WhipDecorator(darkRoast);


        System.out.println("cost:" + darkRoast.cost());
        System.out.println("desc:" + darkRoast.getDesc());
    }

}
