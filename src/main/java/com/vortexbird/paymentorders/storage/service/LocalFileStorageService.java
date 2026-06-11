package com.vortexbird.paymentorders.storage.service;

import com.vortexbird.paymentorders.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class LocalFileStorageService
        implements FileStorageService {

    private static final String UPLOAD_DIR =
            "uploads/invoices";

    @Override
    public String storeFile(
            MultipartFile file,
            Long orderId
    ) {

        validateFile(file);

        try {

            Path uploadPath =
                    Paths.get(UPLOAD_DIR);

            Files.createDirectories(uploadPath);

            String originalFilename =
                    file.getOriginalFilename();

            String extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );

            String fileName =
                    "invoice_"
                            + orderId
                            + "_"
                            + System.currentTimeMillis()
                            + extension;

            Path targetPath =
                    uploadPath.resolve(fileName);

            file.transferTo(targetPath);

            log.info(
                    "Invoice stored successfully: {}",
                    targetPath
            );

            return targetPath.toString();

        } catch (IOException ex) {

            throw new BusinessException(
                    "Error storing invoice file."
            );
        }
    }

    private void validateFile(
            MultipartFile file
    ) {

        String fileName =
                file.getOriginalFilename();

        if (fileName == null) {

            throw new BusinessException(
                    "Invalid file name."
            );
        }

        String lowerCaseName =
                fileName.toLowerCase();

        boolean validExtension =
                lowerCaseName.endsWith(".pdf")
                        || lowerCaseName.endsWith(".png")
                        || lowerCaseName.endsWith(".jpg")
                        || lowerCaseName.endsWith(".jpeg");

        if (!validExtension) {

            throw new BusinessException(
                    "Only PDF, PNG and JPG files are allowed."
            );
        }
    }
}