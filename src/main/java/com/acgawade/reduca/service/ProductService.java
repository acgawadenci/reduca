package com.acgawade.reduca.service;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.repository.ProductRepository;
import com.acgawade.reduca.repository.UserRepository;
import com.acgawade.reduca.security.User;
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

    public List<Product> fetchMyProducts(String userId) {
        return productRepository.findByStatusAndPostedBy(STATUS_ACTIVE, userId);
    }

    @Value("${s3ImageUrl}")
    private String s3ImageUrl;

    public ResponseModel saveProduct(Product property, String userName) {
        ResponseModel response = new ResponseModel();
        try {
            property.setId(UUID.randomUUID());
            property.setStatus("A");
            property.setPostedOn(LocalDateTime.now());
            property.setPostedBy(userName);
            productRepository.save(property);
            response.setStatus("Success");
            response.setMessage("Operation Successful");
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
                        if (savedProperty.getPostedBy().equals(property.getPostedBy())) {
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
                        if (savedProperty.getPostedBy().equals(UUID.fromString("userPrinciple"))) {
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

    public ResponseModel emailProductOwner(UUID productId, String userName) {
        ResponseModel response = new ResponseModel();
        Product prod = productRepository.findById(productId).orElse(null);
        User productUser = null;
        if (nonNull(prod)) {
            productUser = userRepository.findByUsername(prod.getPostedBy()).orElse(null);
        }
        User enquiryUser = userRepository.findByUsername(userName).orElse(null);
        if (nonNull(productUser) && nonNull(enquiryUser)) {
            sendEmail(productUser, enquiryUser, prod);
        }
        response.setStatus("Success");
        response.setMessage("Operation Successful");
        return response;
    }

    private void sendEmail(User productUser, User enquiryUser, Product product) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("reduca.app@gmail.com");
        message.setTo(productUser.getEmail());
        message.setSubject("Enquiry for your cataloged product : " + product.getName());
        message.setText("A person has made an enquiry for your product : "
                + product.getName() +
                ", kindly call or text the potential buyer of your product </br> Below Are The Details Of Enquirer. <br>" +
                " Name: " + enquiryUser.getFirstname() + " " + enquiryUser.getLastname() + " </br>" +
                " Email ID: " + enquiryUser.getEmail() + " </br>");
        emailSender.send(message);
    }
}
