package com.example.UserManagementSystem.service;

import com.example.UserManagementSystem.ExceptionHandling.ResourceNotFoundException;
import com.example.UserManagementSystem.dto.VariantRequest;
import com.example.UserManagementSystem.dto.VariantResponse;
import com.example.UserManagementSystem.entities.Product;
import com.example.UserManagementSystem.entities.Variant;
import com.example.UserManagementSystem.repository.VariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
public class VariantService {
    @Autowired
    private VariantRepository variantRepository;

    private String encodeImageToBase64(MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);  // Correct usage
    }
    private String saveImage(MultipartFile image) throws IOException {
        final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads";
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String uniqueFileName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll("\\s", "");
        String filePath = Paths.get(UPLOAD_DIR, uniqueFileName).toString();
        image.transferTo(new File(filePath));

        String imageUrl = "http://localhost:8080/api/images/view/" + uniqueFileName;
        return imageUrl;
    }

    public static VariantResponse convertToResponseDTO(Variant variant) {
        VariantResponse responseDTO = new VariantResponse(variant.getUniqueId(), variant.getOptionsData(), variant.getImage());
        return responseDTO;
    }

    /// CREATE AND UPDATE VARIANT
    public VariantResponse createAndUpdateVariant(VariantRequest variantRequest, MultipartFile variantImage, Product product) throws IOException {
        String imageUrl = saveImage(variantImage);
        if (variantRequest.uniqueId() == null) {
            Variant variant = new Variant();
            variant.setOptionsData(variantRequest.optionsData());
            variant.setImage(imageUrl);
            variant.setProduct(product);

            Variant savedVariant = variantRepository.save(variant);
            return convertToResponseDTO(variant);
        } else {
            Variant existingVariant = variantRepository.findByUniqueId(variantRequest.uniqueId());
            if (existingVariant == null)
                throw new ResourceNotFoundException("Variant with this Unique id: " + variantRequest.uniqueId() + " is not found");
            existingVariant.setOptionsData(variantRequest.optionsData());
            existingVariant.setImage(imageUrl);
            existingVariant.setProduct(product);
            Variant updateVariant = variantRepository.save(existingVariant);
            return convertToResponseDTO(updateVariant);
        }

    }

    /*public Map<String, Object> deleteVariant(List<Long> variantIds, Long uniqueProductId) {
        List<String> notDeleted = new ArrayList<>();
        List<String> successfullyDeleted = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Long uniqueVariantId : variantIds) {
            Variant existingVariant = variantRepository.findByUniqueId(uniqueVariantId);

            if (existingVariant == null) {
                notDeleted.add("Variant with Unique ID " + uniqueVariantId + " not found for deletion.");
                continue;
            }

            if (existingVariant.getProduct().getUniqueProductId().equals(uniqueProductId)) {
                successfullyDeleted.add("Variant with Unique ID " + uniqueVariantId + " deleted successfully.");
                ids.add(uniqueVariantId);
            } else {
                notDeleted.add("Variant ID " + uniqueVariantId + " cannot be deleted as it is not linked to Product ID " + uniqueProductId + ".");
            }
        }
        for (Long idn : ids) {
            variantRepository.deleteByUniqueId(idn);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("Deleted Variants", successfullyDeleted);
        result.put("Not Deleted Variants", notDeleted);

        return result;
    }

*/
 /*   public Map<String, Object> deleteVariant(Long[] variantIds, Long uniqueProductId) {
        List<String> notDeleted = new ArrayList<>();
        List<String> successfullyDeleted = new ArrayList<>();
        List<Long> ids=new ArrayList<>();
        for (Long uniqueVariantId : variantIds) {
            Variant existingVariant = variantRepository.findByUniqueId(uniqueVariantId);

            if (existingVariant == null) {
                notDeleted.add("Variant with Unique ID " + uniqueVariantId + " not found for deletion.");
                continue;
            }

            if (existingVariant.getProduct().getUniqueProductId().equals(uniqueProductId)) {
                successfullyDeleted.add("Variant with Unique ID " + uniqueVariantId + " deleted successfully.");
                ids.add(uniqueVariantId);
            } else {
                notDeleted.add("Variant ID " + uniqueVariantId + " cannot be deleted as it is not linked to Product ID " + uniqueProductId + ".");
            }
        }

        // Instead of deleting within the loop, collect the ids to delete
        List<Long> idsToDelete = new ArrayList<>(ids);
        System.out.println("Ids to be deleted are "+idsToDelete);
       // variantRepository.deleteByUniqueIds(idsToDelete);

        for (Long idn : idsToDelete) {
            variantRepository.deleteByUniqueId(idn);  // Now we are deleting from the copied list
        }

        Map<String, Object> result = new HashMap<>();
        result.put("Deleted Variants", successfullyDeleted);
        result.put("Not Deleted Variants", notDeleted);

        return result;
    }*/
    public List<String> deleteVariants( Long variantId, Long uniqueProductId) {
           List<String> msg = new ArrayList<>();
            Variant existingVariant = variantRepository.findByUniqueId(variantId);

            if (existingVariant==null) {
                msg.add("Variant with Unique ID " + variantId + " not found for deletion.");
            }
            else if (existingVariant!=null&&existingVariant.getProduct().getUniqueProductId().equals(uniqueProductId)) {
                msg.add("Variant with Unique ID " + variantId + " deleted successfully.");
                variantRepository.delete(existingVariant);
            } else {
                msg.add("Variant ID " + variantId + " cannot be deleted as it is not linked to Product ID " + uniqueProductId + ".");
            }
        return msg;
    }





    public List<Variant> findByProductId(Long uniqueProductId) {
        return  variantRepository.findByProductId(uniqueProductId);
    }

    public void deleteAll(List<Variant> productVariants) {
        variantRepository.deleteAll(productVariants);
    }
}
