package com.example.UserManagementSystem.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record VariantRequest(
        Long uniqueId,
        @NotNull(message = "Variant Option Data should not be null")
        String optionsData){
}
