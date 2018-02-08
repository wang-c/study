package com.tjp.utils;

import com.tjp.design.proxy.Subject;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 代理类的生成工具
 * User: tujinpeng
 * Date: 2017/3/15
 * Time: 11:11
 * Email:tujinpeng@lvmama.com
 */
public class ProxyGeneratorUtils {
    /**
     * 把代理类的字节码写到硬盘上
     *
     * @param path 保存路径
     */
    public static void writeProxyClassToHardDisk(String proxy, String path) {
        // 第一种方法，这种方式在刚才分析ProxyGenerator时已经知道了
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", true);

        // 第二种方法
        // 获取代理类的字节码
        byte[] classFile = ProxyGenerator.generateProxyClass(proxy, Subject.class.getInterfaces());

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(path);
            out.write(classFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
