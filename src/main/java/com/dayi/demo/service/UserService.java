package com.dayi.demo.service;

import com.dayi.demo.dto.UserDto;
import com.dayi.demo.error.BusinessException;

import java.security.NoSuchAlgorithmException;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
public interface UserService {
    UserDto getUserByUserId(Integer id);

    int getOtp();

    void register(UserDto userDto) throws BusinessException;

    UserDto login(String telephone, String password) throws BusinessException, NoSuchAlgorithmException;
}
