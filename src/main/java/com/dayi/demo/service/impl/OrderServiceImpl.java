package com.dayi.demo.service.impl;

import com.dayi.demo.dao.OrderInfoMapper;
import com.dayi.demo.dao.SequenceInfoMapper;
import com.dayi.demo.dto.ItemDto;
import com.dayi.demo.dto.OrderDto;
import com.dayi.demo.dto.UserDto;
import com.dayi.demo.error.BusinessErrorEmun;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.model.OrderInfo;
import com.dayi.demo.model.SequenceInfo;
import com.dayi.demo.service.ItemService;
import com.dayi.demo.service.OrderService;
import com.dayi.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SequenceInfoMapper sequenceInfoMapper;

    @Override
    public OrderDto addOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //校验合法
        UserDto userDto = userService.getUserByUserId(userId);
        if (userDto == null) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }
        ItemDto itemDto = itemService.getItemById(itemId);
        if (itemDto == null) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR, "商品数量不合法");
        }

        if (promoId != null) {
            if (promoId.intValue() != itemDto.getPromoDto().getId()) {
                throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
            } else if (itemDto.getPromoDto().getStatus() != 2) {
                throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
            }
        }

        //落单减库存
        boolean result = itemService.doDecreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR, "库存不足");
        }
        //生成订单
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        orderDto.setItemId(itemId);
        orderDto.setAmount(amount);
        if (promoId != null) {
            orderDto.setItemPrice(itemDto.getPromoDto().getPromoPrice());
        } else {
            orderDto.setItemPrice(itemDto.getPrice());
        }
        orderDto.setOrderPrice(orderDto.getItemPrice().multiply(new BigDecimal(amount)));

        OrderInfo orderInfo = convertOrderInfoFromOrderDto(orderDto);

        String orderId = generateOrderNo();
        orderInfo.setId(orderId);

        orderInfoMapper.insert(orderInfo);
        return orderDto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {
        //订单号16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间6位自增序列
        int sequence = 0;
        SequenceInfo sequenceInfo = sequenceInfoMapper.getSequenceByName("order_info");
        sequenceInfo.setCurrentValue(sequenceInfo.getCurrentValue() + sequenceInfo.getStep());
        sequenceInfoMapper.updateByPrimaryKeySelective(sequenceInfo);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0;i < 6 - sequenceStr.length();i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        //后2位分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderInfo convertOrderInfoFromOrderDto(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderDto, orderInfo);
        orderInfo.setItemPrice(orderDto.getItemPrice().doubleValue());
        orderInfo.setOrderPrice(orderDto.getOrderPrice().doubleValue());
        return orderInfo;
    }
}
