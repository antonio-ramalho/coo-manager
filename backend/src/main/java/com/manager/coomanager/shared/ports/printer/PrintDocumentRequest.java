package com.manager.coomanager.shared.ports.printer;

import java.util.Map;
import java.util.Objects;

/**
 * Requisição agnóstica de impressão de documento.
 */
public record PrintDocumentRequest(
        String documentTitle,
        byte[] content,
        PrintDocumentType documentType,
        String printerIdentifier,
        Map<String, Object> metadata
) {
    public PrintDocumentRequest {
        Objects.requireNonNull(documentTitle, "O título do documento é obrigatório.");
        Objects.requireNonNull(content, "O conteúdo do documento não pode ser nulo.");
        Objects.requireNonNull(documentType, "O tipo do documento é obrigatório.");
        if (metadata == null) {
            metadata = Map.of();
        }
    }
}
