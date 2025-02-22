package com.example.UserManagementSystem.Specification;

import com.example.UserManagementSystem.entities.Product;
import com.example.UserManagementSystem.entities.Variant;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasProductName(String productName){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productName"),productName));
    }
    public static Specification<Product> hasCategoryName(String categoryName){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("categoryName"),categoryName));
    }
    public static Specification<Product> hasCategoryId(Long categoryId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"),categoryId));
    }
    public static Specification<Product> hasUniqueProductId(Long uniqueProductId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("uniqueProductId"),uniqueProductId));
    }
    public static Specification<Product> hasVariantWithOptionData(String optionData) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Variant> variantJoin = root.join("variantSet");
            return criteriaBuilder.like(variantJoin.get("optionsData"), "%" + optionData + "%");
        };
    }


}
