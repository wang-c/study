package com.github.tjp.algorithms.consitenthash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务器节点信息
 * Created by tujinpeng on 2017/3/19.
 */
public class CacheNode {
    /**
     * 节点ip
     */
    private String ip;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 节点名称
     */
    private String name;

    /**
     * 模拟高并发场景,向缓存节点放入数据
     */
    private Map<String, Object> dataMap = new ConcurrentHashMap<String, Object>();


    public CacheNode(String ip, Integer port, String name) {
        this.ip = ip;
        this.name = name;
        this.port = port;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CacheNode[name=" + name + ",ip=" + ip + ",port:" + port + "]";
    }


    public Object get(String key) {
        return dataMap.get(key);

    }

    public void put(String key, Object value) {
        dataMap.put(key, value);
    }

}
