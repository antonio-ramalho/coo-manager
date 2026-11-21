package com.manager.coomanager.shared.ports.printer;

/**
 * Status de conectividade e prontidão da impressora física ou de rede.
 */
public enum PrinterStatus {
    ONLINE,
    OFFLINE,
    PAPER_OUT,
    BUSY,
    UNKNOWN
}
