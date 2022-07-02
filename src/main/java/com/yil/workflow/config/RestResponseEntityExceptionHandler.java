package com.yil.workflow.config;

import com.yil.workflow.base.ApiError;
import com.yil.workflow.base.ApiException;
import com.yil.workflow.base.ApiFieldError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<ApiError> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ApiFieldError responce = new ApiFieldError();
            responce.setField(fieldError.getField());
            responce.setMessage(ObjectUtils.nullSafeToString(fieldError.getDefaultMessage()));
            errors.add(responce);
        }
        ApiError[] arr = new ApiError[errors.size()];
        errors.toArray(arr);
        ApiError responce = ApiError.builder()
                .message(status.getReasonPhrase())
                .code(status.value())
                .errors(arr)
                .build();
        return handleApiError(ex, responce, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status))
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        ApiError responce = ApiError.builder()
                .message(status.getReasonPhrase())
                .code(status.value())
                .build();
        return handleApiError(ex, responce, headers, status, request);
    }

    protected final ResponseEntity<Object> handleApiError(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity(body, headers, status);
    }

    @ExceptionHandler({Exception.class})
    @Nullable
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null)
            status = responseStatus.value();
        ApiException apiException = ex.getClass().getAnnotation(ApiException.class);
        if (apiException == null)
            return handleApiError(ex, null, new HttpHeaders(), status, request);
        ApiError responce = ApiError.builder()
                .message(apiException.code().getMessage())
                .code(apiException.code().getCode())
                .build();
        return handleApiError(ex, responce, new HttpHeaders(), status, request);
    }
}