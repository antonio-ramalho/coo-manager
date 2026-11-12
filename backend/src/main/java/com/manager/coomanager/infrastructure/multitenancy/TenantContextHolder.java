package com.manager.coomanager.infrastructure.multitenancy;

/**
 * Armazena e gerencia o identificador do tenant (cooperativa) na thread atual da requisição.
 * Utilizado pelos filtros e interceptadores para isolamento seguro de dados.
 */
public final class TenantContextHolder {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    /**
     * Define o identificador da cooperativa para a thread em execução.
     *
     * @param tenantId identificador da cooperativa
     */
    public static void setTenantId(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Recupera o identificador da cooperativa associado à thread atual.
     *
     * @return identificador da cooperativa ou null se não definido
     */
    public static Long getTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Limpa o identificador do tenant da thread atual para evitar vazamento em pools de threads.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}

