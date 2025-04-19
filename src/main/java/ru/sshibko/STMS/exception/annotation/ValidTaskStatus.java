package ru.sshibko.STMS.exception.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.sshibko.STMS.exception.validator.TaskStatusValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaskStatusValidator.class)
public @interface ValidTaskStatus {

    String message() default "Invalid value for \"Task status\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
