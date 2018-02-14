package com.github.tjp.lang.finalCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/10 下午3:34
 */
public class UnSafeFinalCase {

    //共享变量 多线程下存在数据竞争
    private static Map<String, Object> messageMap;

    /**
     * write方法 初始化map
     */
    public static void write() {
        messageMap = new HashMap<>();
        //初始化
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

        //init酒套餐监听变更列
        //商品变更:商品名称，是否组合销售，是否发传真，成人儿童数，有效性，最小最大预定数
        messageMap.put("HotelPackageGoodsSubscriber", "GOODS_NAME,CANCEL_FLAG,PACKAGE_FLAG,FAX_FLAG,MIN_QUANTITY,MAX_QUANTITY,ADULT,CHILD");
        //价格变更:售价，结算价，提前预定时间，禁售，剩余库存，超卖，库存类型
        messageMap.put("HotelPackagePriceStockSubscriber", "SALE_PRICE,SETTLEMENT_PRICE,ONSALE_FLAG,AHEAD_BOOK_TIME,STOCK,OVERSELL_FLAG,STOCK_TYPE,STOCK_STATUS");
        //产品变更:可售性，有效性，产品名称
        messageMap.put("HotelPackageProdSubscriber", "PRODUCT_NAME,CANCEL_FLAG,SALE_FLAG");
        //行程变更:有效性，行程天数
        messageMap.put("HotelPackageSchedulSubscriber", "CANCEL_FLAG,ROUTE_NUM,STAY_NUM");
        //规格变更:有效性
        messageMap.put("HotelPackageBranchSubscriber", "CANCEL_FLAG");
    }

    /**
     * 读map中的数据,读写线程高并发的情况下,可能会读到为初始化完全的messageMap,导致数据有问题
     */
    public static void read() {
        if (messageMap != null) {
            System.out.println(messageMap.size());//这里访问的时候,可能输出的静态map未初始化完毕,导致数据丢失
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
            UnSafeFinalCase.read();
        }
    }

    private static class Writer implements Runnable {


        @Override
        public void run() {
            UnSafeFinalCase.write();
        }
    }
}
