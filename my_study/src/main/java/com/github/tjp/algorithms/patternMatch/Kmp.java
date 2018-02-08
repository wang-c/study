package com.github.tjp.algorithms.patternMatch;

/**
 * Created by tujinpeng on 2017/10/20.
 */
public class Kmp {


    /**
     * 求模式匹配串的next数组
     * next[j] 表示j之前的字符串最大公共前后缀的长度
     *
     * @param pattern
     * @return
     */
    private static int[] getNext(char[] pattern) {

        int length = pattern.length;
        int[] next = new int[length];
        int j = 0;//当前位置
        int k = -1;//表示当前位置之前最大公共前后缀长度,即k=next[j]
        //已知当前位置j,next[j]的值,求j++位置的next[j++]值
        next[0] = -1;
        while (j < length - 1) {
            if (k == -1 || pattern[k] == pattern[j]) {
                //k位置和当前位置j匹配上,说明最长公共前后缀可以更大++1
                k++;
                j++;
                // 优化点:修改next数组求法
                if (pattern[j] != pattern[k]) {
                    next[j] = k;// KMPStringMatcher中只有这一行
                } else {
                    // 不能出现p[j] = p[next[j]],所以如果出现这种情况则继续递归,如 k = next[k],
                    // k = next[[next[k]]
                    next[j] = next[k];
                }

            } else {
                //k位置和当前位置j失配,说明此时最长公共前后缀会更短,递归更短的前缀,k=next[k]
                k = next[k];
            }

        }
        return next;
    }

    /**
     * 找出source中的关键字pattern
     *
     * @param source
     * @param pattern
     * @return
     */
    public static int match(String source, String pattern) {
        if (isEmptyString(source)) {
            throw new NullPointerException("source str is must be not null");
        }
        if (isEmptyString(pattern)) {
            return -1;
        }
        char[] src = source.toCharArray();
        char[] ptn = pattern.toCharArray();
        //获取模式串的next数组
        int[] next = getNext(ptn);
        int i = 0;//原字符串的匹配下标
        int j = 0;//模式串的匹配成功的下标
        while ((i < src.length) && (j < ptn.length)) {
            if (j == -1 || src[i] == ptn[j]) {
                //j==-1代表模式串首节点就失配,此时i,j下标都要向前移动
                //src[i] == ptn[j] 代表当前位置匹配成功,此时两者下标也要向前移动
                i++;
                j++;
            } else {
                //若不是从模式串的第一个节点失配,则需要将模式串pattern向右移动(已经匹配的位置j)-(位置j以前的最大公共前后缀长度)next[j]
                //相当于j下标移动到next[j]位置
                j = next[j];

            }
        }
        //若模式串匹配到最后一个位置,代表在源串中找到关键字了
        if (j == ptn.length) {
            return i - j;
        }
        return -1;
    }

    public static boolean isEmptyString(String str) {
        return str == null && str.length() == 0;
    }


    public static void main(String[] args) {

        String pattern1 = "ababa";

        //获取模式串的next数组
        for (int e : getNext(pattern1.toCharArray())) {
            System.out.println(e);
        }

        //在source查找模式串pattern
        String source = "ababoababaa";
        String pattern = "abaa";

        System.out.println(source + " match " + pattern + " result : " + match(source, pattern));
    }
}
