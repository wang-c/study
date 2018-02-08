import org.junit.Test;
import com.tjp.design.chain.DefaultHandlerChain;
import com.tjp.design.chain.HandlerA;
import com.tjp.design.chain.HandlerB;
import com.tjp.design.chain.HandlerChain;

/**
 * Created by tujinpeng on 2017/5/22.
 */
public class ChainTest {
    @Test
    public void chainTest() {
        HandlerChain chain = new DefaultHandlerChain();
        chain.addLast(new HandlerA());
        chain.addLast(new HandlerB());

        chain.handleRequest();
    }

}
