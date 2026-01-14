package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 문제: 서비스 계층의 책임(조회/변경)과 트랜잭션 경계가 명확히 드러나지 않음, 서비스 전용 DTO 미사용
 * 원인: 서비스 계층 설계 시 트랜잭션 적용 기준을 명시하지 않음
 * 개선안: 트랜잭션 선언과 서비스 전용 DTO 사용
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 문제: 엔티티 생성 로직이 서비스 레이어에 위치함, 서비스 전용 DTO 미사용
     * 원인: 엔티티 내부에 엔티티 생성 메서드 부재
     * 개선안: Product 엔티티 내부에 생성 팩토리 메서드 사용
     */
    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /**
     * 문제: 조회 실패 시 RuntimeException을 직접 사용함
     * 원인: 조회 실패 상황을 표현하는 사용자 정의 예외가 정의되어 있지 않음
     * 개선안: ProductNotFoundException 등 사용자 정의 예외를 만들어 사용
     */
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    /**
     * 문제: 수정 요청에서 null 값이 기존 데이터를 그대로 덮어쓸 수 있음, 엔티티 내부 update 메서드 부재
     * 원인: 수정 범위(전체/부분)에 대한 정책이 코드에 드러나지 않음
     * 개선안: 엔티티 내부에 update 메서드를 만들어서 수정 로직을 캡슐화
     */
    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    /**
     * 문제: 조회 정렬 기준이 서비스 레이어에 하드코딩됨, 서비스 전용 DTO 미사용
     * 원인: 조회 조건 및 정렬 정책을 요청/외부에서 분리하지 않음
     * 개선안: 정렬 조건을 요청 DTO로 전달받거나 공통 조회 정책으로 분리
     */
    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}