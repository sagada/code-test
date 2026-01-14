package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 문제: 엔티티가 Setter 기반의 완전 가변 구조로 설계됨, Lombok의 Getter를 사용하지만 중복된 메서드 사용
 * 원인: 엔티티 변경 및 생성 책임을 캡슐화하지 않음
 * 개선안: Setter 제거 후 의도를 드러내는 변경 메서드(update 등)와 생성을 위한 팩토리 메서드 추가하여 처리, 중복 메서드 제거 getCategory, getName
 */
@Entity
@Getter
@Setter
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
