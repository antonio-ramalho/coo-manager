package com.manager.coomanager.infrastructure.adapters.storage;

import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.ports.storage.FileStoragePort;
import com.manager.coomanager.shared.ports.storage.StoredFileMetadata;
import com.manager.coomanager.shared.ports.storage.UploadFileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Adaptador de armazenamento em disco local.
 * Gerencia persistência de arquivos em ambiente de desenvolvimento e prepara
 * o contrato para posterior substituição transparente por Supabase/S3 em produção.
 */
@Component
public class LocalStorageAdapter implements FileStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    private final Path rootStoragePath;

    public LocalStorageAdapter(@Value("${app.storage.local-dir:./storage}") String storageDir) {
        this.rootStoragePath = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootStoragePath);
        } catch (IOException e) {
            log.warn("[StorageAdapter] Não foi possível criar o diretório base de storage: {}", this.rootStoragePath, e);
        }
    }

    @Override
    public StoredFileMetadata uploadFile(UploadFileRequest request) {
        String tenantPrefix = request.cooperativeId() != null ? "tenant_" + request.cooperativeId() : "global";
        String fileKey = tenantPrefix + "/" + request.folder() + "/" + UUID.randomUUID() + "_" + request.fileName();
        Path targetPath = this.rootStoragePath.resolve(fileKey).normalize();

        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, request.content());
            log.info("[StorageAdapter] Arquivo armazenado: chave='{}', tamanho={} bytes", fileKey, request.content().length);

            return new StoredFileMetadata(
                    fileKey,
                    request.fileName(),
                    request.contentType(),
                    request.content().length,
                    "/api/storage/" + fileKey,
                    Instant.now()
            );
        } catch (IOException e) {
            throw new DomainException("Falha ao gravar arquivo no storage local: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String fileKey) {
        Path targetPath = this.rootStoragePath.resolve(fileKey).normalize();
        if (!Files.exists(targetPath)) {
            throw new DomainException("Arquivo não encontrado no storage: " + fileKey);
        }
        try {
            return Files.readAllBytes(targetPath);
        } catch (IOException e) {
            throw new DomainException("Erro ao ler arquivo do storage: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileKey) {
        Path targetPath = this.rootStoragePath.resolve(fileKey).normalize();
        try {
            Files.deleteIfExists(targetPath);
            log.info("[StorageAdapter] Arquivo excluído: chave='{}'", fileKey);
        } catch (IOException e) {
            throw new DomainException("Erro ao excluir arquivo do storage: " + e.getMessage());
        }
    }

    @Override
    public String generatePresignedUrl(String fileKey, Duration duration) {
        return "/api/storage/" + fileKey + "?expiresIn=" + duration.toSeconds();
    }
}
