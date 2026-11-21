package com.manager.coomanager.shared.ports.storage;

import java.time.Duration;

/**
 * Porta (Interface) Universal de Armazenamento de Arquivos.
 * Desacopla a aplicação dos provedores concretos de nuvem (Supabase Storage, S3, MinIO)
 * ou armazenamento local em disco.
 */
public interface FileStoragePort {

    /**
     * Realiza o upload de um arquivo.
     *
     * @param request Dados e bytes do arquivo a ser armazenado.
     * @return Metadados do arquivo armazenado incluindo sua chave única e URL de acesso.
     */
    StoredFileMetadata uploadFile(UploadFileRequest request);

    /**
     * Baixa o conteúdo binário de um arquivo a partir de sua chave.
     *
     * @param fileKey Chave única do arquivo.
     * @return Bytes do arquivo armazenado.
     */
    byte[] downloadFile(String fileKey);

    /**
     * Exclui um arquivo do sistema de armazenamento.
     *
     * @param fileKey Chave única do arquivo a ser removido.
     */
    void deleteFile(String fileKey);

    /**
     * Gera uma URL temporária assinada para acesso direto e seguro ao arquivo.
     *
     * @param fileKey Chave única do arquivo.
     * @param duration Tempo de validade do link.
     * @return URL assinada temporária.
     */
    String generatePresignedUrl(String fileKey, Duration duration);
}
