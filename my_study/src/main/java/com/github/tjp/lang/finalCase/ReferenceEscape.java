package com.github.tjp.lang.finalCase;

import java.util.HashMap;
import java.util.Map;

/**
 * final修饰的变量,【一定能保证】对象的线程安全发布么?
 * 【当引用逸出时不保证】:
 * (1)写final域的重排序规则可以确保：在引用变量为任意线程可见之前，该引用变量指向的对象的final域已经在构造函数中被正确初始化过了。
 * (2)其实要得到这个效果，还需要一个保证：
 * 在构造函数内部，不能让这个被构造对象的引用为其他线程可见，也就是对象引用不能在构造函数中“逸出”
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/10 下午9:08
 */
public class ReferenceEscape {

    private static Message message;

    private static class Message {

        private final Map<String, Object> messageMap;

        public Message() {
            messageMap = new HashMap<>();

            //这里人为的引用【逸出】了,Message对象提前暴露给其他线程,可能messageMap未初始化完毕
            message = this;

            //让messageMap变量初始化过程慢点
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageMap.put("ProdProductSubscriber", "PRODUCT_NAME,SALE_FLAG,CANCEL_FLAG,PACKAGE_TYPE,DISTRICT_ID");
            messageMap.put("ProdDestReSubscriber", "DEST_ID");
            messageMap.put("ProdProductPropSubscriber", "PROP_VALUE");
            messageMap.put("ProdSubjectSubscriber", "SEQ");
            messageMap.put("ProdProductBranchSubscriber", "BRANCH_NAME,CANCEL_FLAG,SALE_FLAG,MAX_VISITOR");
            messageMap.put("ProdProductBranchPropSubscriber", "PROD_VALUE,ADD_VALUE");
            messageMap.put("SuppGoodsSubscriber", "GOODS_NAME,CANCEL_FLAG,ONLINE_FLAG,PACKAGE_FLAG,PAY_TARGET,GROUP_ID,GOODS_TYPE");
            messageMap.put("SuppGoodsGroupStockSubscriber", "STOCK,TOTAL_STOCK");
            messageMap.put("ProdProductHotelApiSubscriber", "SALE_FLAG,CANCEL_FLAG");
            messageMap.put("SuppGoodsHotelApiSubscriber", "CANCEL_FLAG,ONLINE_FLAG");
            messageMap.put("SuppGoodsRouteSubscriber", "CANCEL_FLAG,SALE_FLAG,GROUP_ID");
            messageMap.put("SuppGoodsRouteGroupStockSubscriber", "STOCK,TOTAL_STOCK");
            messageMap.put("RouteProductSubscriber", "CANCEL_FLAG,SALE_FLAG");

            messageMap.put("HotelProduct2FreedomPackageSubscriber", "PRODUCT_NAME,SALE_FLAG,CANCEL_FLAG");
            messageMap.put("HotelProductBranch2FreedomPackageSubscriber", "BRANCH_NAME,CANCEL_FLAG,SALE_FLAG,MAX_VISITOR");
            messageMap.put("HotelGoods2FreedomPackageSubscriber", "CANCEL_FLAG,ONLINE_FLAG");


        }

        public Map<String, Object> getMessageMap() {
            return messageMap;
        }
    }

    public static void write() {
        if (message == null) {
            message = new Message();
        }
    }

    public static void read() {
        if (message != null) {
            System.out.println(message.getMessageMap().size());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread writeThread = new Thread(new Writer());
            writeThread.start();

            Thread readThread = new Thread(new Reader());
            readThread.start();
        }
    }

    private static class Reader implements Runnable {

        @Override
        public void run() {
            ReferenceEscape.read();
        }
    }

    private static class Writer implements Runnable {

        @Override
        public void run() {
            ReferenceEscape.write();
        }
    }

}
