package com.dayi.demo.service.impl;

import com.dayi.demo.dao.UserInfoMapper;
import com.dayi.demo.dao.UserPasswordMapper;
import com.dayi.demo.dto.UserDto;
import com.dayi.demo.error.BusinessErrorEmun;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.model.UserInfo;
import com.dayi.demo.model.UserPassword;
import com.dayi.demo.service.UserService;
import com.dayi.demo.util.Md5Utils;
import com.dayi.demo.validator.ValidatorImpl;
import com.dayi.demo.validator.ValidatorResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Autowired
    private ValidatorImpl validator;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto getUserByUserId(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null) {
            return null;
        }
        UserPassword userPassword = userPasswordMapper.getUserPasswordByUserId(id);
        return convertFromModel(userInfo, userPassword);
    }

    @Override
    public int getOtp() {
        int code = ThreadLocalRandom.current().nextInt(99999);
        code += 10000;
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserDto userDto) throws BusinessException {
        if (userDto == null) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }
       /* if (StringUtils.isEmpty(userDto.getName()) || userDto.getGender() == null || userDto.getAge() == null ||
            StringUtils.isEmpty(userDto.getTelephone()) || StringUtils.isEmpty(userDto.getEncrptPassword())) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidatorResult validatorResult = validator.validate(userDto);
        if (validatorResult.isHasErrors()) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR, validatorResult.getErrMsg());
        }

        UserInfo userInfo = converUserInfoFromDto(userDto);
        try {
            userInfoMapper.insertSelective(userInfo);
        } catch (DuplicateKeyException e) {
            logger.error("手机号冲突");
            throw new BusinessException(BusinessErrorEmun.UNKNOWN_ERROR, "手机号冲突");
        }
        userDto.setId(userInfo.getId());
        UserPassword userPassword = converUserPasswordFromDto(userDto);
        userPasswordMapper.insertSelective(userPassword);
    }

    @Override
    public UserDto login(String telephone, String password) throws BusinessException, NoSuchAlgorithmException {
        UserInfo userInfo = userInfoMapper.getUserInfoByTelephone(telephone);
        if (userInfo == null) {
            throw new BusinessException(BusinessErrorEmun.USER_NOT_EXIST);
        }
        UserPassword userPassword = userPasswordMapper.getUserPasswordByUserId(userInfo.getId());
        if (!com.alibaba.druid.util.StringUtils.equals(Md5Utils.encodeByMd5(password), userPassword.getEncrptPassword())) {
            throw new BusinessException(BusinessErrorEmun.USERNAME_PASSWORD_ERRROR);
        }
        UserDto userDto = convertFromModel(userInfo, userPassword);
        return userDto;
    }

    private UserPassword converUserPasswordFromDto(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userDto.getEncrptPassword());
        userPassword.setUserId(userDto.getId());
        return userPassword;
    }

    private UserInfo converUserInfoFromDto(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userDto, userInfo);
        return userInfo;
    }

    private UserDto convertFromModel(UserInfo userInfo, UserPassword userPassword) {
        if (userInfo == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userInfo, userDto);
        if (userPassword != null) {
            userDto.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userDto;
    }
}
