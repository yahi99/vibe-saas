package com.yongche.yopsaas.core.validator;

import com.google.common.collect.Lists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class RideDispatchSortValidator implements ConstraintValidator<RideDispatchSort, String> {
    private List<String> valueList;

    @Override
    public void initialize(RideDispatchSort sort) {
        valueList = Lists.newArrayList();
        for (String val : sort.accepts()) {
            valueList.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!valueList.contains(s.toUpperCase())) {
            return false;
        }
        return true;
    }
}
