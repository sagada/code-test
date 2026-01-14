package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 문제: 요청 DTO가 가변 구조(Setter)를 가지며 입력 검증이 없고, 필수 필드 여부 확인 불가능, 불변성이 보장되지 않음
 * 원인: 요청 DTO를 불변 객체로 설계 하지 않음
 * 개선안:
 * - Setter 제거 후 불변 DTO로 변경 (record 사용)
 * - 필수 필드에 대한 검증 애노테이션 추가
 */
@Getter
@Setter
public class CreateProductRequest {
    private String category;
    private String name;

    public CreateProductRequest(String category) {
        this.category = category;
    }

    public CreateProductRequest(String category, String name) {
        this.category = category;
        this.name = name;
    }
}