package com.manager.coomanager.shared.ports.storage;

import java.time.Instant;

/**
 * Metadados de um arquivo armazenado no sistema de storage.
 */
public record StoredFileMetadata(
        String fileKey,
        String fileName,
        String contentType,
        long sizeInBytes,
        String accessUrl,
        Instant uploadedAt
) {
}
