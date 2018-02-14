package com.github.tjp.lang.finalCase;

import java.util.HashMap;
import java.util.Map;

/**
 * final可以用于声明字段、方法和类.
 * (1)final声明字段时若为基本类型，表示该变量值初始化后不再改变；若为引用类型，则表示引用不可变，但引用所指向的对象是可以改变的。
 * (2)final声明方法时表示方法不可覆写（常用来限制子类不可以改写父类中方法）。
 * (3)final声明类时，表示类不可继承，如String类就是final的，你不能继承它
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/10 下午3:34
 */
public class SafeMessagePublisher {

    private final Map<String, Object> messageMap;

    public SafeMessagePublisher() {
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


        //当对final域写时,编译器会事先插入一个StoreStore屏障,等待final域数据写完,构造函数才return
        //StoreStore
    }

    public Map<String, Object> getMessageMap() {
        return messageMap;
    }
}