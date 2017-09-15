import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tujinpeng on 2017/9/14.
 */
public class CollectionTest {

    public static void main(String[] args) {
        Map cache = new LinkedHashMap<Object, Object>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                return size() > 5;
            }
        };
        cache.put("1", new Object());
        cache.put("2", new Object());
        cache.put("3", new Object());
        cache.put("4", new Object());
        cache.put("5", new Object());
        cache.get("1");
        cache.get("3");
        cache.put("6", new Object());

    }
}
