package com.mss.framework.base.user.server.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static ServerResponse check(Object param) {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            StringBuffer errorMessage = new StringBuffer();
            for (String message : map.values()) {
                errorMessage.append(message);
            }
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), errorMessage.toString());
        }
        return ServerResponse.createBySuccess();
    }

    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (first != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        }
        LinkedHashMap errors = Maps.newLinkedHashMap();
        Iterator iterator = validateResult.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation violation = (ConstraintViolation) iterator.next();
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }
}