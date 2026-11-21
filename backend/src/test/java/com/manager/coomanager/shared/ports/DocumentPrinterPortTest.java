package com.manager.coomanager.shared.ports;

import com.manager.coomanager.infrastructure.adapters.printer.LoggingDocumentPrinterAdapter;
import com.manager.coomanager.shared.ports.printer.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DocumentPrinterPortTest {

    private final DocumentPrinterPort printerPort = new LoggingDocumentPrinterAdapter();

    @Test
    @DisplayName("Deve simular com sucesso a emissão de um comprovante de venda")
    void shouldPrintReceiptSuccessfully() {
        byte[] content = "Comprovante de Venda #1024 - R$ 150,00".getBytes(StandardCharsets.UTF_8);
        PrintDocumentRequest request = new PrintDocumentRequest(
                "Recibo de Venda",
                content,
                PrintDocumentType.RECEIPT,
                "thermal-printer-01",
                Map.of("saleId", 1024L)
        );

        PrintResult result = printerPort.printDocument(request);

        assertThat(result.success()).isTrue();
        assertThat(result.jobId()).isNotBlank();
        assertThat(result.printedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve validar status da impressora")
    void shouldCheckPrinterStatus() {
        PrinterStatus status = printerPort.checkPrinterStatus("printer-test");
        assertThat(status).isEqualTo(PrinterStatus.ONLINE);
    }

    @Test
    @DisplayName("Deve falhar ao instanciar requisição com parâmetros nulos")
    void shouldValidateRequestInvariants() {
        assertThatThrownBy(() -> new PrintDocumentRequest(null, new byte[0], PrintDocumentType.LABEL, "p1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("título do documento é obrigatório");

        assertThatThrownBy(() -> new PrintDocumentRequest("Título", null, PrintDocumentType.LABEL, "p1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("conteúdo do documento não pode ser nulo");
    }
}
