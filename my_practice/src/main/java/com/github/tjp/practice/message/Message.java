package com.github.tjp.practice.message;

import java.io.Serializable;

/**
 * 消息对象
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/11 上午11:25
 */
public class Message implements Serializable {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 消息类型
     */
    private String objectType;

    /**
     * 按天变价日期
     */
    private String date;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
