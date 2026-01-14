package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 문제: 필터링 되는 컬럼과 메서드 파라미터가 불일치
     * 원인: category 기준으로 조회하려고 했으나 name 파라미터라고 메서드 파라미터가 되어있어 혼동하여 사용할 수 있음
     * 개선안: 파라미터 명을 category로 통일하여 가독성 개선
     */
    Page<Product> findAllByCategory(String name, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}
