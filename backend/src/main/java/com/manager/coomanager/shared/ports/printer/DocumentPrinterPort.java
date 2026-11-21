package com.manager.coomanager.shared.ports.printer;

/**
 * Porta (Interface) Universal de Impressão de Documentos.
 * Permite que módulos de negócio (vendas, contratos, logística) enviem documentos
 * para impressão sem conhecer os detalhes físicos ou drivers de hardware.
 */
public interface DocumentPrinterPort {

    /**
     * Envia um documento para a fila de impressão.
     *
     * @param request Dados e conteúdo do documento a ser impresso.
     * @return Resultado contendo ID do trabalho ou mensagem de erro.
     */
    PrintResult printDocument(PrintDocumentRequest request);

    /**
     * Verifica a conectividade e status de uma impressora específica.
     *
     * @param printerIdentifier Identificador da impressora (IP, porta serial ou nome do spooler).
     * @return Status atual da impressora.
     */
    PrinterStatus checkPrinterStatus(String printerIdentifier);
}
