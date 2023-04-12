package com.acgawade.reduca.controller;

import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/image")
public class ImageController {
    @Autowired
    ImageService imageService;

    @PostMapping()
    public ResponseEntity<ResponseModel> uploadFile(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("propertyId") String propertyId) {
        return new ResponseEntity<>(imageService.storeImage(file,propertyId), HttpStatus.CREATED);
    }
}
