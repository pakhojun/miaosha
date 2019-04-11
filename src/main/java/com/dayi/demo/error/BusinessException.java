package com.dayi.demo.error;

/**
 * 包装器业务异常类实现
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errmsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errmsg);
    }


    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
