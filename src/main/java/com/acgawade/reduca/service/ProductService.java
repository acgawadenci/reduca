package com.acgawade.reduca.service;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.acgawade.reduca.config.Constants.STATUS_ACTIVE;
import static java.util.Objects.nonNull;


@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    public List<Product> fetchProducts() {
        return productRepository.findByStatus(STATUS_ACTIVE);
    }
    
    public ResponseModel saveProduct(Product property) {
        ResponseModel response=new ResponseModel();
        try{
            property.setId(UUID.randomUUID());
            property.setStatus("A");
            property.setPostedOn(LocalDateTime.now());
            property.setPostedBy("userPrinciple");
            productRepository.save(property);
            response.setStatus("Success");
            response.setMessage("Operation Successful");
            response.setCreationId(property.getId().toString());
        } catch (Exception e){
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    public ResponseModel updateProduct(UUID propertyId, Product property) {
        ResponseModel response=new ResponseModel();
        try{
            productRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if(savedProperty.getPostedBy().equalsIgnoreCase(property.getPostedBy())) {
                            setUpdatedValues(savedProperty, property);
                            productRepository.save(savedProperty);
                            response.setStatus("Success");
                            response.setMessage("Operation Successful");
                        } else {
                            response.setStatus("Unsuccessful");
                            response.setMessage("Operation Unsuccessful : Not Authorized To Update This Property");
                        }
                    }
            );
        } catch (Exception e){
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    private void setUpdatedValues(Product property, Product updatedProp) {
        if(nonNull(updatedProp.getAddress()))
            property.setAddress(updatedProp.getAddress());

        if(nonNull(updatedProp.getYearMade()))
            property.setYearMade(updatedProp.getYearMade());

        if(nonNull(updatedProp.getFeatures()))
            property.setFeatures(updatedProp.getFeatures());

        if(nonNull(updatedProp.getName()))
            property.setName(updatedProp.getName());

        if(nonNull(updatedProp.getPrice()))
            property.setPrice(updatedProp.getPrice());

        if(nonNull(updatedProp.getPostalCode()))
            property.setPostalCode(updatedProp.getPostalCode());

        property.setModifiedOn(LocalDateTime.now());
    }

    public ResponseModel inactiveProduct(UUID propertyId) {
        ResponseModel response=new ResponseModel();
        try{
            productRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if(savedProperty.getPostedBy().equalsIgnoreCase("userPrinciple")) {
                            savedProperty.setStatus("I");
                            productRepository.save(savedProperty);
                            response.setStatus("Success");
                            response.setMessage("Operation Successful");
                        } else {
                            response.setStatus("Unsuccessful");
                            response.setMessage("Operation Unsuccessful : Not Authorized To Update This Property");
                        }
                    }
            );
        } catch (Exception e){
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }
}
