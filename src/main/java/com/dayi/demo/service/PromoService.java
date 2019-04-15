package com.dayi.demo.service;

import com.dayi.demo.dto.PromoDto;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/12
 */
public interface PromoService {
    PromoDto getPromoByItemId(Integer itemId);
}
