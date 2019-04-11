package com.dayi.demo.service.impl;

import com.dayi.demo.dao.ItemMapper;
import com.dayi.demo.dao.ItemStockMapper;
import com.dayi.demo.dto.ItemDto;
import com.dayi.demo.error.BusinessErrorEmun;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.model.Item;
import com.dayi.demo.model.ItemStock;
import com.dayi.demo.service.ItemService;
import com.dayi.demo.validator.ValidatorImpl;
import com.dayi.demo.validator.ValidatorResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemStockMapper itemStockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemDto addItem(ItemDto itemDto) throws BusinessException {
        if (itemDto == null) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }
        ValidatorResult validatorResult = validator.validate(itemDto);
        if (validatorResult.isHasErrors()) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR, validatorResult.getErrMsg());
        }
        Item item = convertItemFromItemDto(itemDto);
        itemMapper.insertSelective(item);
        itemDto.setId(item.getId());
        ItemStock itemStock = convertItemStockFromItemDto(itemDto);
        itemStockMapper.insert(itemStock);
        return getItemById(item.getId());
    }

    private ItemStock convertItemStockFromItemDto(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setItemId(itemDto.getId());
        itemStock.setStock(itemDto.getStock());
        return itemStock;
    }

    private Item convertItemFromItemDto(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        item.setPrice(itemDto.getPrice().doubleValue());
        return item;
    }

    @Override
    public List<ItemDto> listItem() {
        List<Item> itemList = itemMapper.listItem();
        List<ItemDto> itemDtoList = itemList.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.getItemStockByItemId(item.getId());
            ItemDto itemDto = convertItemDtoFromItemAndItemStock(item, itemStock);
            return itemDto;
        }).collect(Collectors.toList());
        return itemDtoList;
    }

    @Override
    public ItemDto getItemById(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        ItemStock itemStock = itemStockMapper.getItemStockByItemId(id);
        ItemDto itemDto = convertItemDtoFromItemAndItemStock(item, itemStock);
        return itemDto;
    }

    private ItemDto convertItemDtoFromItemAndItemStock(Item item, ItemStock itemStock) {
        if (item == null) {
            return null;
        }
        ItemDto itemDto = new ItemDto();
        BeanUtils.copyProperties(item, itemDto);
        itemDto.setPrice(new BigDecimal(item.getPrice()));
        itemDto.setStock(itemStock.getStock());
        return itemDto;
    }
}
