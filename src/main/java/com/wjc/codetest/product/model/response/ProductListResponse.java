package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
@Getter
@Setter
public class ProductListResponse {
    /**
     * 문제: 응답 DTO가 JPA 엔티티(Product)에 직접 의존함
     * 원인: API 응답 전용 DTO와 도메인 모델을 분리하지 않음
     * 개선안: ProductResponse 등 조회 전용 DTO로 변환하여 반환
     */
    private List<Product> products;
    private int totalPages;
    private long totalElements;
    private int page;

    public ProductListResponse(List<Product> content, int totalPages, long totalElements, int number) {
        this.products = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = number;
    }
}
