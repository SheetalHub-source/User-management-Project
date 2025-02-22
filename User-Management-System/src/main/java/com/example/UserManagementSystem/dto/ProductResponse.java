package com.example.UserManagementSystem.dto;

import java.util.Set;

public record ProductResponse(
        Long uniqueProductId,
        String productName,
        String productDesc,
        Double price,
        String productImage,
        String categoryName,
        Set<VariantResponse> variantSet) {
}
