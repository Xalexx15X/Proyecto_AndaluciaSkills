package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.andaluciaskills.andaluciasckills.Error.FileStorageException;

import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;


@Service
@Slf4j
public class FileStorageService {
    private Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            this.fileStorageLocation = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();
            Files.createDirectories(this.fileStorageLocation);
            log.info("Directorio de almacenamiento inicializado en: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo crear el directorio para almacenar los archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new FileStorageException("No se puede almacenar un archivo vacío");
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (fileName.contains("..")) {
                throw new FileStorageException("El nombre del archivo contiene una ruta inválida " + fileName);
            }

            if (!fileName.toLowerCase().endsWith(".pdf")) {
                throw new FileStorageException("Solo se permiten archivos PDF");
            }

            // Generar nombre único
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Archivo almacenado exitosamente: {}", uniqueFileName);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo almacenar el archivo " + file.getOriginalFilename(), ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("Archivo no encontrado " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("Archivo no encontrado " + fileName, ex);
        }
    }
}
