package kr.elroy.aigoya.config;

import jakarta.servlet.http.HttpServletRequest;
import kr.elroy.aigoya.dto.ErrorResponse;
import kr.elroy.aigoya.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringWebExceptionHandler {
    @ExceptionHandler(DomainException.class)
    ResponseEntity<ErrorResponse> handleDomainException(HttpServletRequest request, DomainException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(ErrorResponse.of(exception.getErrorCode()));
    }
}
