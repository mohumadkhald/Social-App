package com.projects.socialapp.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String storeFile(MultipartFile file, String folder) throws IOException {
        // Normalize folder name
        String normalizedFolderName = folder.trim().replaceAll("\\s+", "_");

        // Create folder if it doesn't exist
        Path folderPath = this.fileStorageLocation.resolve(normalizedFolderName);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // Get the original file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // Resolve the file path
        Path filePath = folderPath.resolve(fileName);

        // Copy the file to the target location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

}
