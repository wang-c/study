package com.github.tjp.algorithms.consitenthash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 分布式环形一致性哈希算法介绍(memcached不具备分布式,客户端通过此算法实现分布式):
 * (1)节点,数据使用同一hash算法,映射在0--（2^32）-1 环内。
 * (2)添加缓存节点时,为了考虑hash算法的平衡性,放入虚拟节点替换真实节点,若干虚拟节点从属一个真实节点。
 * (3)环中存放的都是虚拟节点
 * Created by tujinpeng on 2017/3/19.
 */
public class ConsistentHash {
    /**
     * 负责hash运算的工具
     */
    private final HashFunction hashFunction;
    /**
     * 虚拟节点个数
     */
    private final int numberOfReplicas;
    /**
     * 存放虚拟节点hash值到真实节点的映射
     */
    private final SortedMap<Long, CacheNode> circle;

    public ConsistentHash(Collection<CacheNode> cacheNodes) {
        this(new HashFunction(), 10, new TreeMap<Long, CacheNode>(), cacheNodes);
    }

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, SortedMap<Long, CacheNode> circle, Collection<CacheNode> cacheNodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        this.circle = circle;

        for (CacheNode cacheNode : cacheNodes) {
            addNode(cacheNode);
        }
    }

    /**
     * 添加缓存节点
     *
     * @param cacheNode
     */
    private void addNode(CacheNode cacheNode) {
        for (int i = 0; i < numberOfReplicas; i++) {
             /*
              * 对于一个实际机器节点 cacheNode, 对应 numberOfReplicas 个虚拟节点
              * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
              * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
              */
            circle.put(hashFunction.hash(cacheNode.toString() + i), cacheNode);
        }
    }

    /**
     * 删除缓存节点
     *
     * @param cacheNode
     */
    private void removeNode(CacheNode cacheNode) {
        /**
         * 要删除属于真实node的numberOfReplicas个虚拟节点
         */
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(cacheNode.toString() + i));
        }
    }

    /**
     * 通过数据的key,客户端通过此算法定位到具体的缓存节点
     *
     * @param key
     * @return
     */
    public CacheNode getRealNode(String key) {
        //1.根据一致性hash,定位到该数据放在哪个虚拟节点上
        if (circle.isEmpty()) {
            //hash环上没节点
            return null;
        }
        long hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {//数据在两个虚拟节点之间
            SortedMap<Long, CacheNode> tailMap = circle.tailMap(hash);//获取key大于hash的部分map
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        //2.通过定位到的虚拟节点的hash值,通过虚拟节点hash值到真实节点的映射,找到真实节点
        System.out.println("client connect " + circle.get(hash).toString()+" to data");
        return circle.get(hash);
    }

    /**
     * 客户端存放数据
     * (1)定位缓存真实节点
     * (2)通过客户端net真实缓存机器,获取value
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return getRealNode(key).get(key);
    }

    /**
     * 客户端存放数据
     * (1)定位缓存真实节点
     * (2)通过客户端net真实缓存机器,存放value
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        getRealNode(key).put(key, value);
    }


}
