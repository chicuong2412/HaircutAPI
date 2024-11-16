package com.haircutAPI.HaircutAPI.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.nimbusds.jose.JOSEException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIresponse> handlingRuntimeException(Exception exception) {
        ErrorCode errCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        APIresponse rp = new APIresponse<>(errCode.getCode());
        System.out.println(exception.getMessage());
        rp.setMessage(errCode.getMessage());
        return ResponseEntity.badRequest().body(rp);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIresponse> handlingAppException(AppException exception) {
        System.out.println(exception.getMessage());
        ErrorCode code = exception.getErrCode();
        APIresponse rp = new APIresponse<>(code.getCode());
        rp.setMessage(code.getMessage());
        return ResponseEntity.badRequest().body(rp);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIresponse> handlingValidations(MethodArgumentNotValidException e) {
        ErrorCode errCode = ErrorCode.valueOf(e.getFieldError().getDefaultMessage());
        APIresponse rp = new APIresponse<>(errCode.getCode());
        rp.setMessage(errCode.getMessage());
        return ResponseEntity.badRequest().body(rp);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = JOSEException.class)
    ResponseEntity<APIresponse> handlingJOSEException(JOSEException e) {
        ErrorCode errCode = ErrorCode.WRONG_TOKEN;
        APIresponse rp = new APIresponse<>(errCode.getCode());
        rp.setMessage(errCode.getMessage());
        return ResponseEntity.badRequest().body(rp);
    }
}
