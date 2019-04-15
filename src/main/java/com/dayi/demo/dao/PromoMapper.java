package com.dayi.demo.dao;

import com.dayi.demo.model.Promo;

public interface PromoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Promo record);

    int insertSelective(Promo record);

    Promo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Promo record);

    int updateByPrimaryKey(Promo record);

    Promo getPromoByItemId(Integer itemId);
}