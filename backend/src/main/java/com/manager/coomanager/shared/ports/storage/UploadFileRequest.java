package com.manager.coomanager.shared.ports.storage;

import java.util.Objects;

/**
 * Requisição universal de upload de arquivo.
 */
public record UploadFileRequest(
        String fileName,
        String contentType,
        byte[] content,
        String folder,
        Long cooperativeId
) {
    public UploadFileRequest {
        Objects.requireNonNull(fileName, "O nome do arquivo é obrigatório.");
        Objects.requireNonNull(contentType, "O tipo de conteúdo (MIME) é obrigatório.");
        Objects.requireNonNull(content, "O conteúdo do arquivo não pode ser nulo.");
        if (folder == null || folder.isBlank()) {
            folder = "general";
        }
    }
}
