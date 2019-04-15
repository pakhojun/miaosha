package com.dayi.demo.controller;

import com.dayi.demo.common.CommonResult;
import com.dayi.demo.dto.OrderDto;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
@Controller
@RequestMapping("order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("addOrder")
    @ResponseBody
    public CommonResult addOrder(@RequestParam("itemId") Integer itemId,
                                 @RequestParam("amount") Integer amount,
                                 @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException {
        OrderDto orderDto = orderService.addOrder(null, itemId, promoId, amount);
        return CommonResult.create(null);
    }


}
