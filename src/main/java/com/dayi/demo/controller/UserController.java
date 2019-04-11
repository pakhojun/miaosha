package com.dayi.demo.controller;

import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import com.alibaba.druid.util.StringUtils;
import com.dayi.demo.common.CommonResult;
import com.dayi.demo.dto.UserDto;
import com.dayi.demo.error.BusinessErrorEmun;
import com.dayi.demo.error.BusinessException;
import com.dayi.demo.service.UserService;
import com.dayi.demo.util.Md5Utils;
import com.dayi.demo.vo.UserVo;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * user controller
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/10
 */
@Controller
@RequestMapping("user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping("login")
    @ResponseBody
    public CommonResult login(@RequestParam("telephone") String telephone,
                              @RequestParam("password") String password) throws BusinessException, NoSuchAlgorithmException {
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone) ||
                org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR);
        }
        UserDto userDto = userService.login(telephone, password);
        request.getSession().setAttribute("IS_LOGIN", true);
        request.getSession().setAttribute("LOGIN_INFO", userDto);
        return CommonResult.create(null);
    }

    @RequestMapping("register")
    @ResponseBody
    public CommonResult register(@RequestParam("otpCode") String otpCode,
                                 @RequestParam("name") String name,
                                 @RequestParam("gender") Byte gender,
                                 @RequestParam("age") Integer age,
                                 @RequestParam("telephone") String telephone,
                                 @RequestParam("password") String password) throws BusinessException, NoSuchAlgorithmException {
        String otpCodeInSession = (String) request.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode, otpCodeInSession)) {
            throw new BusinessException(BusinessErrorEmun.PARAMETER_VALIDATION_ERROR, "验证码错误");
        }
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setGender(gender);
        userDto.setAge(age);
        userDto.setTelephone(telephone);
        userDto.setEncrptPassword(Md5Utils.encodeByMd5(password));
        userService.register(userDto);
        return CommonResult.create(null);
    }

    @RequestMapping(value = "getOtp", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonResult getOtp(@RequestParam("telephone") String telephone) {
        int code = userService.getOtp();
        request.getSession().setAttribute(telephone, code+"");
        logger.info("手机号: {} , otp: {}", telephone, code);
        return CommonResult.create(null);
    }

    @RequestMapping("getUserByUserId")
    @ResponseBody
    public CommonResult getUserByUserId(@RequestParam("id") Integer id) throws BusinessException {
        UserDto userDto = userService.getUserByUserId(id);

        if(userDto == null) {
            throw new BusinessException(BusinessErrorEmun.USER_NOT_EXIST);
            //userDto.setAge(1);
        }

        UserVo userVo = convertFromModel(userDto);
        return CommonResult.create(userVo);
    }

    private UserVo convertFromModel(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDto, userVo);
        return userVo;
    }
}
