package com.github.tjp.structure.tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 红黑树实现的
 * 有序的map
 * Created by tujinpeng on 2017/10/11.
 */
public class TreeMapExample {

    public static void main(String[] args) {
        Map<String, Object> treeMap = new TreeMap<String, Object>();
        //插入(插入key不能为空 否则报空指针)
//        treeMap.put(null, null);
        treeMap.put("1", "value1");
        treeMap.put("2", "value2");
        treeMap.put("3", "value3");
        treeMap.put("4", "value4");
        treeMap.put("5", "value5");
        treeMap.put("6", "value6");

        //获取
        Object value = treeMap.get("2");

        Set<Map.Entry<String, Object>> entrySet = treeMap.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        //有序遍历map
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            System.out.println("entry key : " + entry.getKey() + ", value:" + entry.getValue());

        }

        //有序遍历key
        for (Object o : treeMap.keySet()) {
            System.out.println(o);
        }

        //有序遍历value
        for (Object o : treeMap.values()) {
            System.out.println(o);
        }
    }
}
