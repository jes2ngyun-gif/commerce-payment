package com.jes2ngyun.commerce_payment.common.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.swing.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ① 우리가 의도적으로 던진 비즈니스 예외
    // 예) 재고 부족, 이메일 중복, 소유권 불일치
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[BusinessException] {} : {}", errorCode.name(), errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    //② @Valid 검증 실패 (Request Body)
    // 예) 이메일 형식 오류, 비밀번호 8자 미만, 필수값 누락 → 어떤 필드가 왜 틀렸는지 알려주는 게 사용자에게 유용하므로 실제 메시지를 담는다
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ErrorCode.INVALID_REQUEST.getMessage());

        log.warn("[Validation] {}", message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(new ErrorResponse(ErrorCode.INVALID_REQUEST.name(), message));
    }

    // ③ 파라미터 제약 위반 (@Min, @Max 등)
    // 예) page=-1, size=10000 Spring 6.1+ 는 HandlerMethodValidationException, 그 이전은 ConstraintViolationException
    @ExceptionHandler({
            HandlerMethodValidationException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleConstraintViolation(Exception e) {
        log.warn("[ConstraintViolation] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST));
    }


     // ④ 요청 자체를 읽을 수 없음
     // 예) JSON 문법 오류, Body 누락, 존재하지 않는 Enum 값, 필수 쿼리 파라미터 누락
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidRequest(Exception e) {
        log.warn("[InvalidRequest] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST));
    }

    // ⑤ 파라미터 타입 불일치
    // 예) /api/products/abc  (id가 Long인데 문자열)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("[TypeMismatch] parameter = {}, value = {}", e.getName(), e.getValue());

        return ResponseEntity
                .status(ErrorCode.INVALID_PARAMETER.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_PARAMETER));
    }

    // ⑥ 인가 실패 컨트롤러/서비스 안에서 발생한 AccessDeniedException이 ⑦번 Exception 핸들러에 잡혀 500이 되는 것을 막는다
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
        log.warn("[AccessDenied] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.ACCESS_DENIED.getStatus())
                .body(ErrorResponse.of(ErrorCode.ACCESS_DENIED));
    }

    // ⑦ 마지막 그물 — 예상하지 못한 모든 예외
    //여기 걸린 건 전부 "우리 코드의 버그"다
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[Unhandled] ", e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

// GlobalExceptionHnadler - 그물