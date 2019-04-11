package com.dayi.demo.controller;

import com.dayi.demo.common.CommonResult;
import com.dayi.demo.dto.ItemDto;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.service.ItemService;
import com.dayi.demo.vo.ItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
@Controller
@RequestMapping("item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("addItem")
    @ResponseBody
    public CommonResult addItem(@RequestParam("title") String title,
                                @RequestParam("price")BigDecimal price,
                                @RequestParam("stock") Integer stock,
                                @RequestParam("description") String description,
                                @RequestParam("imgUrl") String imgUrl) throws BusinessException {
        ItemDto itemDto = new ItemDto();
        itemDto.setTitle(title);
        itemDto.setPrice(price);
        itemDto.setStock(stock);
        itemDto.setDescription(description);
        itemDto.setImgUrl(imgUrl);
        ItemDto itemDtoFromReturn = itemService.addItem(itemDto);

        ItemVo itemVo = convertItemVoFromItemDto(itemDto);
        return CommonResult.create(itemVo);
    }

    @RequestMapping("getItem")
    @ResponseBody
    public CommonResult getItem(@RequestParam("id") Integer id) {
        ItemDto itemById = itemService.getItemById(id);
        ItemVo itemVo = convertItemVoFromItemDto(itemById);
        return CommonResult.create(itemVo);
    }

    private ItemVo convertItemVoFromItemDto(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemDto, itemVo);
        return itemVo;
    }

    @RequestMapping("listItem")
    @ResponseBody
    public CommonResult listItem() {
        List<ItemDto> itemDtoList = itemService.listItem();
        List<ItemVo> itemVoList = itemDtoList.stream().map(itemDto -> {
            ItemVo itemVo = convertItemVoFromItemDto(itemDto);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonResult.create(itemVoList);
    }


}
