package com.tinqinacademy.hotel.core.processors;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public abstract class BaseOperationProcessor {
    protected final ConversionService conversionService;
    protected final ErrorMapper errorMapper;
    private final Validator validator;

    protected <T extends OperationInput> void validate(T input) {
        Set<ConstraintViolation<T>> set = validator.validate(input);
        if(!set.isEmpty()){
            throw new ConstraintViolationException(set);
        }
    }
}
