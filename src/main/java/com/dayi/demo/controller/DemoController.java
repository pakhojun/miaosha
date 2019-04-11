package com.dayi.demo.controller;

import com.dayi.demo.dao.UserInfoMapper;
import com.dayi.demo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo controller
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
@RestController
public class DemoController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping(value = "demo", method = RequestMethod.GET)
    public String test() {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
        if (userInfo == null) {
            return "用户为空";
        }
        return userInfo.getName();
    }


}
