package com.github.tjp.lang;

/**
 * <pre>
 * 深入理解枚举类:
 * 枚举的反编译代码---》
 *
 * public final class EnumCase extends Enum
 * {
 *      //自定义枚举的私有构造方法
 *      //枚举jvm是不允许被反射调用
 *      private EnumCase(String s, int i)
 *      {
 *          super(s, i);
 *      }
 *
 *      public static EnumCase[] values()
 *      {
 *          EnumCase at[];
 *          int i;
 *          EnumCase at1[];
 *          System.arraycopy(at = ENUM$VALUES, 0, at1 = new EnumCase[i = at.length], 0, i);
 *          return at1;
 *      }
 *
 *      public static EnumCase valueOf(String s)
 *      {
 *          return (EnumCase)Enum.valueOf(demo/EnumCase, s);
 *      }
 *
 *      //持有没有静态final的枚举实例对象
 *      public static final EnumCase SMALL;
 *      public static final EnumCase NORMAL;
 *      public static final EnumCase HUGE;
 *
 *      /**
 *       *通过static静态代码块创建所有枚举实例对象属性,以及枚举的数组:
 *       *(1)静态代码块只在类第一次被使用的时候调用,并且jvm保证线程安全
 *       *(2)同时保证它的构造函数只是被调用一次
 *       *
 *       /
 *      static
 *      {
 *          SMALL = new EnumCase("SMALL", 0);
 *          NORMAL = new EnumCase("NORMAL", 1);
 *          HUGE = new EnumCase("HUGE", 1);
 *          ENUM$VALUES = (new EnumCase[] {
 *              SMALL, NORMAL, HUGE
 *          });
 *      });
 *
 * }
 * EnumCase继承jdk的Enum,同时本身是fianl的class,不能被继承
 * (1)自定义的枚举类持有【所有枚举的静态final实例】,以及一个【静态final的枚举数组】
 * 每个枚举实例因为继承Enum,有两个成员变量name【名称】,oridinal【排序号】
 * (2)通过静态代码块初始化这些枚举实例和枚举数组EnumCase[],保证一个【线程安全】 =====》线程安全初始化特点可以用在【线程安全的单例模式上】
 * (3)枚举类申明成final,不能写抽象方法,方法也不能被重写
 *
 * 常用方法分析:
 *    EnumCase.small.name():调用枚举的name方法,因为枚举类继承Enum类,持有了父类两个默认属性name以及ordinal(排序号)
 *    EnumCase.values:实际上返回的是枚举数组的clone【浅拷贝】
 *
 * 序列化(待分析):
 * </pre>
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/17 下午10:17
 */
public enum EnumCase {
    SMALL,
    NORMAL,
    HUGE;
}