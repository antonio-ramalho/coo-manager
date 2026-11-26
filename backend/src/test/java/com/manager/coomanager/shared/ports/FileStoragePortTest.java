package com.manager.coomanager.shared.ports;

import com.manager.coomanager.infrastructure.adapters.storage.LocalStorageAdapter;
import com.manager.coomanager.shared.ports.storage.FileStoragePort;
import com.manager.coomanager.shared.ports.storage.StoredFileMetadata;
import com.manager.coomanager.shared.ports.storage.UploadFileRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class FileStoragePortTest {

    @TempDir
    Path tempStorageDir;

    private FileStoragePort storagePort;

    @BeforeEach
    void setUp() {
        storagePort = new LocalStorageAdapter(tempStorageDir.toString());
    }

    @Test
    @DisplayName("Deve realizar upload, download e deleção de arquivo via porta de storage")
    void shouldUploadDownloadAndDeleteFile() {
        byte[] content = "Certificado Orgânico Válido 2026".getBytes(StandardCharsets.UTF_8);
        UploadFileRequest request = new UploadFileRequest(
                "certificado.pdf",
                "application/pdf",
                content,
                "certificates",
                1L
        );

        StoredFileMetadata metadata = storagePort.uploadFile(request);

        assertThat(metadata.fileKey()).contains("tenant_1/certificates/");
        assertThat(metadata.sizeInBytes()).isEqualTo(content.length);

        byte[] downloaded = storagePort.downloadFile(metadata.fileKey());
        assertThat(downloaded).isEqualTo(content);

        String presignedUrl = storagePort.generatePresignedUrl(metadata.fileKey(), Duration.ofMinutes(15));
        assertThat(presignedUrl).contains("expiresIn=900");

        storagePort.deleteFile(metadata.fileKey());
    }
}
