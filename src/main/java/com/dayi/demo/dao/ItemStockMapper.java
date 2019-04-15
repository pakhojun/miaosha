package com.dayi.demo.dao;

import com.dayi.demo.model.ItemStock;
import org.apache.ibatis.annotations.Param;

public interface ItemStockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStock record);

    int insertSelective(ItemStock record);

    ItemStock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStock record);

    int updateByPrimaryKey(ItemStock record);

    ItemStock getItemStockByItemId(Integer id);

    int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}