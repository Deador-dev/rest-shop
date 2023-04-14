package com.deador.restshop.exception.handler;

import com.deador.restshop.dto.exception.ExceptionResponse;
import com.deador.restshop.exception.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            stringBuilder.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(" and ");
        });
        stringBuilder.setLength(stringBuilder.length() - 5);

        return buildExceptionBody(new BadRequestException(stringBuilder.toString()), status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        // TODO: 14.04.2023 Create an appropriate error message for the BadRequestException thrown when an invalid value is used in a @PathVariable.
        return buildExceptionBody(
                new BadRequestException(String.format("Invalid value used in path variable: '%s'." +
                        " Please ensure that the provided parameter is of the correct type and format.", exception.getValue())),
                status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return buildExceptionBody(new BadRequestException(exception.getMessage()), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        return buildExceptionBody(new MethodNotSupportedException(exception.getMessage()), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        return buildExceptionBody(new BadRequestException(exception.getMessage()), status);
    }

    // TODO: 14.04.2023 need create handler for NotBlank

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
