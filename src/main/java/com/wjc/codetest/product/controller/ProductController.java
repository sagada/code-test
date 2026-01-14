package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 문제: 컨트롤러 클래스에  공통 리소스 경로가 정의되어 있지 않음
 * 원인: 리소스 단위 URI 설계를 메서드 단위로 분산시킴
 * 개선안: @RequestMapping("/products")를 컨트롤러 레벨에 선언 하여 리소스 기준의 URI 구조로 정리
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 문제: 컨트롤러에서 엔티티를 그대로 반환함
     * 원인: API 전용 Response DTO 없이 서비스 반환 타입을 직접 사용함
     * 개선안: Response DTO 도입으로 API 스펙과 도메인 모델 분리
     * 선택 근거:
     * - 엔티티에 연관 관계 확장 시 LazyLoading 및 레이어 분리가 안되어 있음
     *
     * 문제: 조회 API에서 행위 기반 URI 사용 ('get')
     * 원인: HTTP Method 의미 반영 부족
     * 개선안: GET /products/{id}
     */
    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 문제: 생성 API에서 행위 기반 URI 및 적절하지 않은 상태 코드 사용, 서비스 레이어 DTO 분리 하지 않음
     * 원인: HTTP Method 및 상태 코드 설계 반영 부족
     * 개선안:
     * - POST /products + 201 Created
     * - 요청 DTO에 대한 입력 검증 (@Valid) 적용 고려
     */
    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    /**
     * 문제: 삭제 API에서 POST 사용 및 Boolean 응답 반환
     * 개선안:
     * - DELETE /products/{id}
     * - 성공 시 204 No Content 반환, ResponseEntity<Void>
     */
    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    /**
     * 문제: 수정 API에서 POST 사용 및 엔티티 직접 반환, 요청 DTO에 대한 입력 검증(@Valid) 적용 고려, 서비스 레이어 DTO 분리 하지 않음
     * 개선안:
     * - PUT /products/{id}
     * - 컨트롤러 전용 Response DTO 반환
     */
    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    /**
     * 문제: 조회 API에서 POST 메서드를 사용함
     * 원인: 조회 조건을 URI로 표현하는 설계 고려가 부족함
     * 개선안: GET /products?category=... 형태로 조회
     */
    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    /**
     * 문제: 리소스 계층이 URI에 충분히 드러나지 않음, 컨트롤러 레이어 DTO 분리 하지 않음
     * 개선안
     *  - GET /products/categories 형태로 단순화
     *  - 컨트롤러 전용 Response DTO 반환
     */
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}