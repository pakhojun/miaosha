package com.dayi.demo.common;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
public class CommonResult {

    private String status;

    private Object data;

    public static CommonResult create(Object data) {
        return CommonResult.create(data, "success");
    }

    public static CommonResult create(Object data, String status) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(status);
        commonResult.setData(data);
        return commonResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
