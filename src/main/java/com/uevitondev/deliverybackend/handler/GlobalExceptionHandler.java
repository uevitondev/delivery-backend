package com.uevitondev.deliverybackend.handler;

import com.uevitondev.deliverybackend.config.security.jwt.JwtBearerTokenException;
import com.uevitondev.deliverybackend.domain.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            BadCredentialsException.class,
            AuthenticationException.class,
            JwtBearerTokenException.class
    })
    protected ResponseEntity<ProblemDetail> handleAuthenticationException(Exception e) {
        var httpStatus = HttpStatus.UNAUTHORIZED;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatchException(Exception e) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<ProblemDetail> handleAccessDeniedException(Exception e) {
        var httpStatus = HttpStatus.FORBIDDEN;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ProblemDetail> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException e) {
        var httpStatus = HttpStatus.NOT_FOUND;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }


    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<ProblemDetail> handleDatabaseException(DatabaseException e) {
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler(RefreshTokenRevokedException.class)
    protected ResponseEntity<ProblemDetail> handleRefreshTokenRevokedException(RefreshTokenRevokedException e) {
        var httpStatus = HttpStatus.UNAUTHORIZED;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @ExceptionHandler(InvalidTokenVerificationException.class)
    protected ResponseEntity<ProblemDetail> handleRefreshTokenRevokedException(InvalidTokenVerificationException e) {
        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        super.handleMethodArgumentNotValid(e, headers, status, request);
        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")));
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(httpStatus).body(problemDetail);
    }


}