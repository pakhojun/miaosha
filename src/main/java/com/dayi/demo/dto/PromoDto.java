package com.dayi.demo.dto;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/12
 */
public class PromoDto {

    private Integer id;

    private Integer itemId;

    private String promoName;

    private DateTime startDate;

    private BigDecimal promoPrice;

    private DateTime endTime;

    /** 1.未开始 2.进行中 3.已结束 */
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
