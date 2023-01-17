package org.czaplinski.bookshop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerException(MethodArgumentNotValidException e) {
        ResponseEntity<Object> errors = handleError(HttpStatus.BAD_REQUEST, e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + " - " + x.getDefaultMessage())
                .collect(Collectors.toList()));
        return errors;
    }

    private static ResponseEntity<Object> handleError(HttpStatus status, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> iOExceptionHandler(IOException e){
        Map<String, Object> body = new LinkedHashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", e.getMessage());
        return new ResponseEntity<>(body, status);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handler(IllegalArgumentException e){
        return handleError(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
    }
}
