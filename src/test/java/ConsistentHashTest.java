import com.tjp.algorithms.consitenthash.CacheNode;
import com.tjp.algorithms.consitenthash.ConsistentHash;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/3/20
 * Time: 11:19
 * Email:tujinpeng@lvmama.com
 */
public class ConsistentHashTest {

    @Test
    public void hashtest() {
        /**
         * 1.初始化分布式缓存节点组
         */
        List<CacheNode> cacheNodes = new ArrayList<CacheNode>();
        CacheNode node1 = new CacheNode("10.111.2.5", 80, "cache-01");
        CacheNode node2 = new CacheNode("10.111.2.6", 80, "cache-02");
        CacheNode node3 = new CacheNode("10.111.2.7", 80, "cache-03");
        CacheNode node4 = new CacheNode("10.111.2.8", 80, "cache-04");
        CacheNode node5 = new CacheNode("10.111.2.10", 80, "cache-05");
        cacheNodes.add(node1);
        cacheNodes.add(node2);
        cacheNodes.add(node3);
        cacheNodes.add(node4);
        cacheNodes.add(node5);

        ConsistentHash cacheCluster=new ConsistentHash(cacheNodes);

        cacheCluster.put("key1","value1");
        System.out.println(cacheCluster.get("key1"));
        System.out.println("--------------------------");
        cacheCluster.put("key2","value2");
        System.out.println(cacheCluster.get("key2"));

    }

}
