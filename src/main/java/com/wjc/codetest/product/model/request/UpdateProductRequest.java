package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 문제: 요청 DTO가 가변 구조(Setter)를 가지며 입력 검증이 없음
 * 원인: 수정 요청 DTO를 불변 객체로 설계 하지 않음
 * 개선안: Setter 제거 후 불변 DTO(record 또는 final 필드)로 변경
 */
@Getter
@Setter
public class UpdateProductRequest {
    private Long id;
    private String category;
    private String name;

    public UpdateProductRequest(Long id) {
        this.id = id;
    }

    public UpdateProductRequest(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public UpdateProductRequest(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}

