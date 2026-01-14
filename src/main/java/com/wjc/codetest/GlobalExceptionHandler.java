package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 문제: REST API 예외 처리임에도 @ControllerAdvice를 사용하고, 특정 패키지로 스캔 범위를 제한하여 전역 예외 처리 의도가 불명확함
 * 원인: 예외 처리 범위 및 역할에 대한 설계 기준이 명확하지 않음
 * 개선안:
 * - @RestControllerAdvice 사용으로 REST API 예외 처리 의도 명확화
 * - 공통 예외 처리라면 스캔 범위 확장
 */
@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    /**
     * 문제: 모든 RuntimeException을 500 Internal Server Error로 처리함
     * 원인: 예외 타입별 HTTP 상태 코드 매핑 정책이 정의되어 있지 않음
     * 개선안: 사용자 정의 예외별로 핸들러를 분리하여 상태 코드 명확화
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
