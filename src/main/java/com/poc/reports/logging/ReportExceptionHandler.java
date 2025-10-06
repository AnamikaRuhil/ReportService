package com.poc.reports.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ReportExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request){
        logger.error("Exception at : [{} {}]: {}",request.getMethod(),request.getRequestURI(),ex.getMessage(),ex);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", LocalDateTime.now());
        responseMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseMap.put("error", "Internal Server Error");
        responseMap.put("message", ex.getMessage());
        responseMap.put("path", request.getRequestURI());
        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request){
        logger.warn("Bad Request at : [{} {}]: {}",request.getMethod(),request.getRequestURI(),ex.getMessage());

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", LocalDateTime.now());
        responseMap.put("status", HttpStatus.BAD_REQUEST.value());
        responseMap.put("error", "Internal Server Error");
        responseMap.put("message", ex.getMessage());
        responseMap.put("path", request.getRequestURI());
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFound(DataNotFoundException ex, HttpServletRequest request) {
        logger.warn("Bad Request at : [{} {}]: {}",request.getMethod(),request.getRequestURI(),ex.getMessage());

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", LocalDateTime.now());
        responseMap.put("status", HttpStatus.NOT_FOUND.value());
        responseMap.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        responseMap.put("message", ex.getMessage());
        responseMap.put("path", request.getRequestURI());
        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

}
