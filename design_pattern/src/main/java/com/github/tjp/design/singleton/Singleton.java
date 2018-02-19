package com.github.tjp.design.singleton;

/**
 * 具体的单例对象:
 * 数据库连接 memcache客户端 zookeeper客户端等
 * <p/>
 * 一个jvm一个实例
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/19 下午11:04
 */
public class Singleton {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
