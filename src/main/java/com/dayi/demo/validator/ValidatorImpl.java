package com.dayi.demo.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author GuoXuJun <guoxj@pvc123.com>
 * @date 2019/4/11
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public ValidatorResult validate(Object bean) {
            ValidatorResult validatorResult = new ValidatorResult();
            Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
            if (constraintViolationSet.size() > 0) {
                validatorResult.setHasErrors(true);
                constraintViolationSet.forEach(constraintViolation -> {
                    String errmsg = constraintViolation.getMessage();
                    String propertyName = constraintViolation.getPropertyPath().toString();
                    validatorResult.getErrorMsgMap().put(propertyName, errmsg);
                });
            }
            return validatorResult;
    }

}
