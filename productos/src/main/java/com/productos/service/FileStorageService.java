package com.productos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.storage.path:uploads/productos}") String path) throws IOException {
        this.root = Paths.get(path).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public String guardar(MultipartFile file) throws IOException {
        String tipo = file.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        ext = (ext != null && ext.matches("[A-Za-z0-9]{1,5}")) ? "." + ext : "";
        String nombre = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), root.resolve(nombre));
        return nombre;
    }

    public void eliminar(String nombre) throws IOException {
        if (nombre != null) {
            Files.deleteIfExists(root.resolve(nombre).normalize());
        }
    }
}