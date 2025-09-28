package org.banking.accountms.domain.port;

public interface ClientsPort {
    /**
     * Verifica si existe un cliente con el ID dado.
     * Devuelve true si el cliente existe en el microservicio de clientes.
     */
    boolean exists(Long clientId);
}
