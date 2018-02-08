import com.github.tjp.concurrent.limiter.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 限流：令牌桶，漏桶.
 * User: tujinpeng
 * Date: 2017/3/13
 * Time: 12:33
 * Email:tujinpeng@lvmama.com
 */
public class RateLimiterTest {

    private static final Integer MAX_QPS = 5;

    /**
     * 阻塞等待方式限流1
     */
    @Test
    public void blockLimiterTest1() {
        RateLimiter limiter = RateLimiter.create(MAX_QPS);
        System.out.println(limiter.acquire(10));//允许突发的流量
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
    }

    /**
     * 阻塞等待方式限流2
     */
    @Test
    public void limiterTest2() throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(2);
        System.out.println(limiter.acquire());
        Thread.sleep(2000L);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }

    /**
     * 无阻塞或者超时等待方式限流
     */
    @Test
    public void noBlocklimiterTest() {
        RateLimiter limiter = RateLimiter.create(2000);
        System.out.println(limiter.tryAcquire(100, TimeUnit.MILLISECONDS));//lts超时时间设定为100ms
        System.out.println(limiter.tryAcquire(100, TimeUnit.MILLISECONDS));
        System.out.println(limiter.tryAcquire(100, TimeUnit.MILLISECONDS));
        System.out.println(limiter.tryAcquire(100, TimeUnit.MILLISECONDS));
    }
}
