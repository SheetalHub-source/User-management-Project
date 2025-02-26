package com.example.UserManagementSystem.controller;

import com.example.UserManagementSystem.dto.ProductRequest;
import com.example.UserManagementSystem.dto.ProductResponse;
import com.example.UserManagementSystem.dto.VariantResponse;
import com.example.UserManagementSystem.entities.Category;
import com.example.UserManagementSystem.entities.Product;
import com.example.UserManagementSystem.entities.Variant;
import com.example.UserManagementSystem.service.CategoryService;
import com.example.UserManagementSystem.service.ProductService;
import com.example.UserManagementSystem.service.VariantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VariantService variantService;
    @Autowired
    private  ObjectMapper objectMapper;




    @GetMapping("/product")
    public String readProductData(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "uniqueProductId") String field,
                                  @RequestParam(defaultValue = "desc") String orderBy,
                                  @RequestParam(required = false) Long uniqueProductId,
                                  @RequestParam(required = false) Long categoryId,
                                  @RequestParam(required = false) String productName,
                                  @RequestParam(required = false) String categoryName,
                                  @RequestParam(required = false) String variant,
                                  @RequestParam(required = false) Long minPrice,
                                  @RequestParam(required = false) Long maxPrice,
                                  Model model) {
        Page<ProductResponse> productResponses = productService.findByCriteria(page, size, field, orderBy, uniqueProductId, categoryId, productName, categoryName, variant,minPrice,maxPrice);
        List<Category> categories = categoryService.getAllCategories();
        List<ProductResponse> products = productResponses.getContent();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productResponses.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", orderBy);
        return "Product";
    }

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping("/api/images/view/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(System.getProperty("user.dir") + "/uploads", imageName);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Resource resource = new UrlResource(imagePath.toUri());
        String contentType = Files.probeContentType(imagePath);

        if (contentType == null) {
            contentType = "application/octet-stream"; // Fallback
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


    @GetMapping("/variants/{id}")
    public String getVariants(@PathVariable("id") Long id, Model model) {
        List<VariantResponse> variants = variantService.findByProductId(id);

        // Debugging print
        System.out.println("Variants: " + variants);

        model.addAttribute("variants", variants);
        return "variants";
    }


    @PostMapping(value = "/product",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<ProductResponse> createOrUpdateProduct(
            @RequestParam String productRequestField,
            @RequestParam(value = "productImage", required = false) MultipartFile productImage,
            @RequestParam(value = "variantImage", required = false) MultipartFile[] variantImage) throws Exception {

        System.out.println("Incoming Request field: " + productRequestField);
        ProductRequest productRequest = objectMapper.readValue(productRequestField, ProductRequest.class);
        System.out.println("Mapped productRequest: " + productRequest);

        ProductResponse productResponse = productService.createOrUpdateProductWithVariant(productRequest, productImage, variantImage);
        return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/product/update/{uniqueProductId}")
    public String getProduct(@PathVariable(name="uniqueProductId") Long uniqueProductId,Model model){
         ProductResponse product = productService.findByUniqueProductId(uniqueProductId);
         List<VariantResponse> variantResponses = variantService.findByProductId(uniqueProductId);
        List<Category> categories = categoryService.getAllCategories();
         model.addAttribute("product",product);
         model.addAttribute("Variants",variantResponses);
        model.addAttribute("categories", categories);

        return "updateProduct";
    }


}

