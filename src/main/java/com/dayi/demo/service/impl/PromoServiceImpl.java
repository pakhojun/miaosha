package com.dayi.demo.service.impl;

import com.dayi.demo.dao.PromoMapper;
import com.dayi.demo.dto.PromoDto;
import com.dayi.demo.model.Promo;
import com.dayi.demo.service.PromoService;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/12
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoMapper promoMapper;

    @Override
    public PromoDto getPromoByItemId(Integer itemId) {

        Promo promo = promoMapper.getPromoByItemId(itemId);
        PromoDto promoDto = convertDtoFromModel(promo);
        if (promoDto == null) {
            return null;
        }

        if (promoDto.getStartDate().isAfterNow()) {
            promoDto.setStatus(1);
        } else if (promoDto.getEndTime().isBeforeNow()) {
            promoDto.setStatus(3);
        } else {
            promoDto.setStatus(2);
        }

        return promoDto;
    }

    private PromoDto convertDtoFromModel(Promo promo) {
        if (promo == null) {
            return null;
        }
        PromoDto promoDto = new PromoDto();
        BeanUtils.copyProperties(promo, promoDto);
        promoDto.setPromoPrice(new BigDecimal(promo.getPromoPrice()));
        promoDto.setStartDate(new DateTime(promo.getStartDate()));
        promoDto.setEndTime(new DateTime(promo.getEndTime()));
        return promoDto;
    }
}
