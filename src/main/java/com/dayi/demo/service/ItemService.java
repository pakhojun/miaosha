package com.dayi.demo.service;

import com.dayi.demo.dto.ItemDto;
import com.dayi.demo.error.BusinessException;

import java.util.List;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
public interface ItemService {

    ItemDto addItem(ItemDto itemDto) throws BusinessException;

    List<ItemDto> listItem();

    ItemDto getItemById(Integer id);

    boolean doDecreaseStock(Integer itemId, Integer amount);
}
