package ru.sshibko.STMS.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import ru.sshibko.STMS.exception.annotation.ValidTaskPriority;
import ru.sshibko.STMS.model.enums.TaskPriority;

public class TaskPriorityValidator implements ConstraintValidator<ValidTaskPriority,TaskPriority> {

    @Override
    public void initialize(ValidTaskPriority constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TaskPriority taskPriority, ConstraintValidatorContext constraintValidatorContext) {
        if (taskPriority == null) {
            return false;
        }

        return taskPriority == TaskPriority.LOW || taskPriority == TaskPriority.HIGH ||
                taskPriority == TaskPriority.MEDIUM;
    }
}
