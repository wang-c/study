import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tujinpeng on 2017/9/14.
 */
public class CollectionTest {

    public static void main(String[] args) {
        // LinkedHashMap不仅仅是一个hashmap,还要维护一个双向的循环链表
        Map cache = new LinkedHashMap<Object, Object>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                return size() > 5;
            }
        };
        //put操作 将节点加入到链表尾部
        cache.put("1", new Object());
        cache.put("2", new Object());
        cache.put("3", new Object());
        cache.put("4", new Object());
        cache.put("5", new Object());
        //当访问某节点时,设置accessOrder为true时,会将该节点从链表中删除,加入到尾部(表示最近访问的),头部即为最近最少访问的元素(优先删除)
        cache.get("1");
        cache.get("3");
        cache.put("6", new Object());

    }
}
