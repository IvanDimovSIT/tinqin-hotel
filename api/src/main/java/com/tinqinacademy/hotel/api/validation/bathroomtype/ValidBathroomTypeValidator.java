package com.tinqinacademy.hotel.api.validation.bathroomtype;

import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ValidBathroomTypeValidator implements ConstraintValidator<ValidBathroomType, BathroomType> {
    @Override
    public void initialize(ValidBathroomType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BathroomType bathroomType, ConstraintValidatorContext context) {
        return !BathroomType.UNKNOWN.equals(bathroomType);
    }
}
