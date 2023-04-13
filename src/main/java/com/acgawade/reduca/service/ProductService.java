package com.acgawade.reduca.service;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.repository.ProductRepository;
import com.acgawade.reduca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.acgawade.reduca.config.Constants.STATUS_ACTIVE;
import static java.util.Objects.nonNull;


@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;

    public List<Product> fetchProducts() {
        return productRepository.findByStatus(STATUS_ACTIVE);
    }

    @Value("${s3ImageUrl}")
    private String s3ImageUrl;

    public ResponseModel saveProduct(Product property) {
        ResponseModel response = new ResponseModel();
        try {
            property.setId(UUID.randomUUID());
            System.out.println("Created ID befor : "+ property.getId().toString());
            property.setStatus("A");
            property.setPostedOn(LocalDateTime.now());
            property.setPostedBy("userPrinciple");
            productRepository.save(property);
            response.setStatus("Success");
            response.setMessage("Operation Successful");
            System.out.println("Created ID after : "+ property.getId().toString());
            response.setCreationId(property.getId().toString());
        } catch (Exception e) {
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    public ResponseModel updateProduct(UUID propertyId, Product property) {
        ResponseModel response = new ResponseModel();
        try {
            productRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if (savedProperty.getPostedBy().equalsIgnoreCase(property.getPostedBy())) {
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
        } catch (Exception e) {
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    private void setUpdatedValues(Product property, Product updatedProp) {
        if (nonNull(updatedProp.getAddress()))
            property.setAddress(updatedProp.getAddress());

        if (nonNull(updatedProp.getYearMade()))
            property.setYearMade(updatedProp.getYearMade());

        if (nonNull(updatedProp.getFeatures()))
            property.setFeatures(updatedProp.getFeatures());

        if (nonNull(updatedProp.getName()))
            property.setName(updatedProp.getName());

        if (nonNull(updatedProp.getPrice()))
            property.setPrice(updatedProp.getPrice());

        if (nonNull(updatedProp.getPostalCode()))
            property.setPostalCode(updatedProp.getPostalCode());

        property.setModifiedOn(LocalDateTime.now());
    }

    public ResponseModel inactiveProduct(UUID propertyId) {
        ResponseModel response = new ResponseModel();
        try {
            productRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if (savedProperty.getPostedBy().equalsIgnoreCase("userPrinciple")) {
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
        } catch (Exception e) {
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    @Transactional
    public void updateImageUrl(String propertyId, String fileKey) {
        productRepository.findById(UUID.fromString(propertyId)).ifPresent(
                product -> {
                    if (nonNull(product.getImages())) {
                        product.getImages().add(s3ImageUrl + fileKey);
                    } else {
                        product.setImages(List.of(s3ImageUrl + fileKey));
                    }
                    productRepository.save(product);
                }
        );
    }

    public ResponseModel emailProductOwner(UUID productId) {
        ResponseModel response = new ResponseModel();
        /* productRepository.findById(productId)
                .flatMap(product -> userRepository.findById(UUID.fromString(product.getPostedBy())))
                .ifPresent(user -> sendEmail(user.getEmail())); */
        sendEmail("gawade.achyut96@gmail.com");
        response.setStatus("Success");
        response.setMessage("Operation Successful");
        return response;
    }

    private void sendEmail(String emailId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("reduca.app@gmail.com");
        message.setTo(emailId);
        message.setSubject("Enquiry for your cataloged product");
        message.setText("A person has made an enquiry for your product, kindly call or text the potential buyer of your product ");
        emailSender.send(message);
    }
}
