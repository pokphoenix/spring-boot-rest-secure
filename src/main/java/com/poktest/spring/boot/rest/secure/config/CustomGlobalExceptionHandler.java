package com.poktest.spring.boot.rest.secure.config;

import com.poktest.spring.boot.rest.secure.api.error.BookNotFoundException;
import com.poktest.spring.boot.rest.secure.api.error.BookUnSupportedFieldPatchException;
import com.poktest.spring.boot.rest.secure.api.error.CustomErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
    // by default when JPA select not found data it return error 500
    @ExceptionHandler(BookNotFoundException.class)
//    public void springHandleNotFound(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.NOT_FOUND.value());
//    }
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, HttpServletRequest request) {
        // below code for custom error
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setPath(request.getServletPath());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BookUnSupportedFieldPatchException.class)
    public void springUnSupportedFieldPatch(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
    }


    // @Validate For Validating Path Variables and Request Parameters
//    @ExceptionHandler(ConstraintViolationException.class)
//    public void constraintViolationException(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.BAD_REQUEST.value());
//    }

    // error handle for @Valid
//    @Override
//    protected ResponseEntity<Object>
//    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                 HttpHeaders headers,
//                                 HttpStatus status, WebRequest request ) {
//
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", new Date());
//        body.put("status", status.value());
//
//        //Get all fields errors
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(x -> x.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        body.put("errors", errors);
//
//        System.err.println( request.getRemoteUser());
//        System.err.println( request.getContextPath());
//
//
//        request.getParameterMap().forEach((k,v)->{
//            System.err.println("poktest : getParameterMap "+k+" : "+v);
//        });
//
//        System.err.println( request.getUserPrincipal());
//        System.err.println( request.getRemoteUser());
//        return new ResponseEntity<>(body, headers, status);
//
//    }


}
