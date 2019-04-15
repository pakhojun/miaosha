package com.dayi.demo.service;

import com.dayi.demo.dto.OrderDto;
import com.dayi.demo.error.BusinessException;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
public interface OrderService {

    OrderDto addOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
