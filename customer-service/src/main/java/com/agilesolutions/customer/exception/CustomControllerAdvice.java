package com.agilesolutions.customer.exception;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ErrorResponse toProblemDetail(Throwable throwable) {

        return ErrorResponse.builder(throwable, HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage())
                .title(throwable.getMessage())
                .type(URI.create("https://mycomp.com/whatever.html"))
                .detail(throwable.getMessage())
                .property("errorCategory", "Generic")
                .property("timestamp", Instant.now())
                .build();

    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("business validation exception");
        problemDetail.setType(URI.create("https://mycomp.com/whatever.html"));
        ex.getProblems().stream().forEach(pr -> problemDetail.setProperty(pr.getCode(), pr.getMessage()));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBadRequestException(MethodArgumentNotValidException ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("bean validation error");
        problemDetail.setType(URI.create("https://mycomp.com/whatever.html"));
        ex.getBindingResult().getFieldErrors().stream().forEach(fe -> problemDetail.setProperty(fe.getField(), fe.getDefaultMessage()));
        problemDetail.setProperty("timestamp", Instant.now());

        return Mono.just(ResponseEntity.status(problemDetail.getStatus()).body(problemDetail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleHttpMessageNotReadable(MethodArgumentNotValidException ex, ServerWebExchange exchange) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("jackson de-serialization error");
        problemDetail.setType(URI.create("https://mycomp.com/whatever.html"));
        problemDetail.setProperty("details", ex.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());

        return Mono.just(ResponseEntity.status(problemDetail.getStatus()).body(problemDetail));
    }
}
