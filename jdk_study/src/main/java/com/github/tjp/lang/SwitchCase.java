package com.github.tjp.lang;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/19 下午4:26
 */
public class SwitchCase {

    public static void main(String[] args) {
        char charVar = 'a';
        byte byteVar = 11;
        int intVar = 11;
        int shortVar = 11;
        String strVar = "11111";
        EnumVar enumVar = EnumVar.ONE;

        /*
         *switch只支持 char、byte、int、short以及他们的包装类、还有String、enum
         *  为啥switch表达式只支持这几种? :
         *  支持的这些数据类型都能隐性的转化为int型数据
         *  =====》
         *  char            通过Ascii码转化为int
         *  String          通过调用String重写的hashcode()
         *  enum            通过调用枚举类的ordinal()序号方法转化为int
         *
         */
        switch (charVar) {
            case 'a':
                System.out.println("haha");
                break;
            default:
                break;
        }
        ;
        switch (byteVar) {
            case 1:
                System.out.println("haha");
                break;
            default:
                break;
        }
        ;
        switch (intVar) {
            case 1:
                System.out.println("haha");
                break;
            default:
                break;
        }
        ;
        switch (shortVar) {
            case 1:
                System.out.println("haha");
                break;
            default:
                break;
        }
        ;
        switch (strVar) {
            case "111":
                System.out.println("haha");
                break;
            default:
                break;
        }
        ;
        switch (enumVar) {
            case ONE:
                System.out.println("haha");
                break;
            case TWO:
                break;
            default:
                break;
        }
        ;

    }

    private static enum EnumVar {
        ONE,
        TWO
    }
}
