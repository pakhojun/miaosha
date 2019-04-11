package com.dayi.demo.controller;

import com.dayi.demo.common.CommonResult;
import com.dayi.demo.error.BusinessErrorEmun;
import com.dayi.demo.error.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
public class BaseController {

    protected static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String,Object> responseMap = new HashMap<>(16);
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException)ex;
            responseMap.put("errCode", businessException.getErrCode());
            responseMap.put("errMsg", businessException.getErrMsg());
        } else {
            logger.error("未知错误", ex);
            responseMap.put("errCode", BusinessErrorEmun.UNKNOWN_ERROR.getErrCode());
            responseMap.put("errMsg", BusinessErrorEmun.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonResult.create(responseMap, "fail");
    }

}
