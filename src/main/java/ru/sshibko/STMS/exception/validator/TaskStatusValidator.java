package ru.sshibko.STMS.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.sshibko.STMS.exception.annotation.ValidTaskStatus;
import ru.sshibko.STMS.model.enums.TaskStatus;

public class TaskStatusValidator implements ConstraintValidator<ValidTaskStatus, TaskStatus> {
    @Override
    public void initialize(ValidTaskStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TaskStatus taskStatus, ConstraintValidatorContext constraintValidatorContext) {
        if (taskStatus == null) {
            return false;
        }

        return taskStatus == TaskStatus.PENDING || taskStatus == TaskStatus.COMPLETED ||
                taskStatus == TaskStatus.IN_PROGRESS;
    }
}
