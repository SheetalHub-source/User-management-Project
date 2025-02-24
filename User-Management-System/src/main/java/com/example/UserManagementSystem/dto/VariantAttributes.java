package com.example.UserManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VariantAttributes(
        @JsonProperty("option") String option,
        @JsonProperty("value") String value
) {}
