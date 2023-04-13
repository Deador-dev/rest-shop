package com.deador.restshop.exception.handler;

import com.deador.restshop.dto.exception.ExceptionResponse;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.IncorrectInputException;
import com.deador.restshop.exception.NotExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(NotExistException.class)
    public final ResponseEntity<Object> handleNotExistException(NotExistException exception) {
        return buildExceptionBody(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectInputException.class)
    public final ResponseEntity<Object> handleIncorrectInputException(IncorrectInputException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public final ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException exception) {
        return buildExceptionBody(exception, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .build();
        log.debug(exception.getMessage());

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }
}
