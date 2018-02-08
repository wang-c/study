import org.junit.Test;
import com.github.tjp.design.chain.DefaultHandlerChain;
import com.github.tjp.design.chain.HandlerA;
import com.github.tjp.design.chain.HandlerB;
import com.github.tjp.design.chain.HandlerChain;

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
