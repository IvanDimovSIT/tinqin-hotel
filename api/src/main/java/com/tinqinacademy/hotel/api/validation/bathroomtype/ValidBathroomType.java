package com.tinqinacademy.hotel.api.validation.bathroomtype;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD,TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidBathroomTypeValidator.class)
public @interface ValidBathroomType {
    String message() default "Invalid bathroom type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
