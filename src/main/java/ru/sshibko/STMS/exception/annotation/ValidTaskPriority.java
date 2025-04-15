package ru.sshibko.STMS.exception.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.sshibko.STMS.exception.validator.TaskPriorityValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaskPriorityValidator.class)
public @interface ValidTaskPriority {

    String message() default "Invalid value for \"Task priority\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
