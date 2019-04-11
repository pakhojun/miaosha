package com.dayi.demo.error;


/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
public interface CommonError {

    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);

}
