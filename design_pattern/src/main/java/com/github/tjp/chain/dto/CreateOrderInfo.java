package com.github.tjp.chain.dto;

import java.io.Serializable;
import java.util.List;

public class CreateOrderInfo implements Serializable {

    private static final long serialVersionUID = -4844739918806911569L;

    /**
     * 订单名称
     */
    private String orderName;

    /***
     * 品类
     */
    private Long categoryId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 供应商订单ID
     */
    private String supplyOrderId;

    /**
     * 分销商订单号
     */
    private String partnerOrderId;

    /**
     * 主站会员编号
     */
    private Long memberNo;

    /**
     * 游玩日期
     */
    private String visitTime;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 销售渠道
     */
    private Long channelId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 传真备注
     */
    private String faxMemo;

    /**
     * 前台传过来的总价
     */
    private Long orderAmount;

    /**
     * 游玩人
     */
    private List<OrderPersonDTO> travellers;

    /**
     * 子订单信息
     */
    private List<ItemInfo> itemList;

    /**
     * 下单人信息
     */
    private OrderDistVo booker = new OrderDistVo();

    /**
     * 联系人
     */
    private OrderPersonDTO contact;

    /**
     * 导游
     */
    private OrderPersonDTO guide;

    /**
     * 邮寄
     */
    private OrderPostDTO express;

    /**
     * 订单来源 PC端、安卓客户端、iOS客户端、API接口、后台人工、微店
     */
    private String sourceName;

    /**
     * 加价类型
     */
    private String raiseRuleType;

    /**
     * 下单语种
     */
    private String lang;

    public Long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Long memberNo) {
        this.memberNo = memberNo;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public List<OrderPersonDTO> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<OrderPersonDTO> travellers) {
        this.travellers = travellers;
    }

    public OrderPersonDTO getContact() {
        return contact;
    }

    public void setContact(OrderPersonDTO contact) {
        this.contact = contact;
    }

    public OrderPersonDTO getGuide() {
        return guide;
    }

    public void setGuide(OrderPersonDTO guide) {
        this.guide = guide;
    }

    public OrderPostDTO getExpress() {
        return express;
    }

    public void setExpress(OrderPostDTO express) {
        this.express = express;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFaxMemo() {
        return faxMemo;
    }

    public void setFaxMemo(String faxMemo) {
        this.faxMemo = faxMemo;
    }

    public String getSupplyOrderId() {
        return supplyOrderId;
    }

    public void setSupplyOrderId(String supplyOrderId) {
        this.supplyOrderId = supplyOrderId;
    }

    public OrderDistVo getBooker() {
        return booker;
    }

    public void setBooker(OrderDistVo booker) {
        this.booker = booker;
    }

    public List<ItemInfo> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemInfo> itemList) {
        this.itemList = itemList;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public ItemInfo getMainItem() {
        List<ItemInfo> itemList = getItemList();
        if (itemList != null && !itemList.isEmpty()) {
            return getItemList().get(0);
        }
        return null;
    }

    public static class ItemInfo implements Serializable {
        private static final long serialVersionUID = 7150457859840948293L;
        private String mainItem;// 是否主订单标识
        private Long goodsId;// 商品Id
        private String itemName;
        private String saleUnitType;
        private int quantity;// 购买份数
        private int adultQuantity;// 成人数
        private int childQuantity;// 儿童数

        public String getMainItem() {
            return mainItem;
        }

        public void setMainItem(String mainItem) {
            this.mainItem = mainItem;
        }

        public Long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Long goodsId) {
            this.goodsId = goodsId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getAdultQuantity() {
            return adultQuantity;
        }

        public void setAdultQuantity(int adultQuantity) {
            this.adultQuantity = adultQuantity;
        }

        public int getChildQuantity() {
            return childQuantity;
        }

        public void setChildQuantity(int childQuantity) {
            this.childQuantity = childQuantity;
        }

        public String getSaleUnitType() {
            return saleUnitType;
        }

        public void setSaleUnitType(String saleUnitType) {
            this.saleUnitType = saleUnitType;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public String getRaiseRuleType() {
        return raiseRuleType;
    }

    public void setRaiseRuleType(String raiseRuleType) {
        this.raiseRuleType = raiseRuleType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public static class OrderPersonDTO {
    }

    public static class OrderDistVo {
    }

    public static class OrderPostDTO {
    }
}
