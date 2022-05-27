package com.yil.workflow.config;

import com.yil.workflow.base.ErrorResponce;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            if (fieldError.getObjectName() != null && !fieldError.getObjectName().isBlank())
                sb.append(fieldError.getObjectName()).append(":");
            if (fieldError.getField() != null && !fieldError.getField().isBlank())
                sb.append(fieldError.getField()).append(":");
            sb.append(ObjectUtils.nullSafeToString(fieldError.getDefaultMessage()));
            errors.add(sb.toString());
        }
        String[] arr = errors.toArray(String[]::new);
        ErrorResponce responce = ErrorResponce.builder()
                .message(status.getReasonPhrase())
                .status(status.value())
                .details(arr)
                .timestamp(new Date())
                .build();
        return new ResponseEntity(responce, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status))
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        ErrorResponce responce = ErrorResponce.builder()
                .message(status.getReasonPhrase())
                .status(status.value())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(responce, headers, status);
    }


    @ExceptionHandler({Exception.class})
    @Nullable
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @Nullable
    public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}