package com.ntschy.underground.handler;

import com.ntschy.underground.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public Result commonException(CommonException commonException) {
        return new Result(false, commonException.getCodeEnum().getCode(), commonException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Result validatorException(MethodArgumentNotValidException validation) {
        BindingResult result = validation.getBindingResult();
        List<String> errMsg = new ArrayList<>(10);
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                errMsg.add(fieldError.getDefaultMessage());
                log.error("Data check failure : object{" + fieldError.getObjectName() + "}, field{" + fieldError.getField() +
                        "}, errorMessage{" + fieldError.getDefaultMessage() + "}");
            });
        }
        return new Result(false, ExceptionEnum.REQUEST_PARAMS_EXCEPTION.getCode(), errMsg.stream().collect(Collectors.joining(";")));
    }

    @ExceptionHandler(BindException.class)
    protected Result BindException(BindException bindException) {
        return new Result(false, ExceptionEnum.REQUEST_PARAMS_EXCEPTION.getCode(),
                bindException.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected Result constraintViolationException(ConstraintViolationException exception) {
        return new Result(false, ExceptionEnum.REQUEST_PARAMS_EXCEPTION.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected Result argumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return new Result(false, ExceptionEnum.REQUEST_PARAMS_EXCEPTION.getCode(), exception.getMessage());
    }
}
