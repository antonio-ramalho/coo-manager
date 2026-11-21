package com.manager.coomanager.infrastructure.adapters.printer;

import com.manager.coomanager.shared.ports.printer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adaptador padrão de impressão que simula e registra em log as operações de impressão.
 * Serve como base e fallback seguro antes da configuração de impressoras térmicas físicas ou ESC/POS.
 */
@Component
public class LoggingDocumentPrinterAdapter implements DocumentPrinterPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingDocumentPrinterAdapter.class);

    @Override
    public PrintResult printDocument(PrintDocumentRequest request) {
        String jobId = "job-" + UUID.randomUUID();
        log.info("[PrinterAdapter] Documento recebido para impressão: título='{}', tipo='{}', bytes={}, impressora='{}', jobId='{}'",
                request.documentTitle(),
                request.documentType(),
                request.content().length,
                request.printerIdentifier(),
                jobId);

        return PrintResult.success(jobId, "Documento enviado com sucesso para a fila de impressão.");
    }

    @Override
    public PrinterStatus checkPrinterStatus(String printerIdentifier) {
        log.debug("[PrinterAdapter] Verificando status da impressora '{}'", printerIdentifier);
        return PrinterStatus.ONLINE;
    }
}
