package org.banking.accountms.infrastructure.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banking.accountms.domain.port.ClientsPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientsGateway implements ClientsPort {

    private final RestTemplate restTemplate;

    @Value("${client.service-url:http://localhost:8080}")
    private String clientServiceUrl;

    @Override
    public boolean exists(Long clientId) {
        String url = clientServiceUrl + "/clientes/" + clientId;

        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Cliente no encontrado: {}", clientId);
            return false;
        } catch (HttpClientErrorException e) {
            log.error("Error del servicio de clientes ({}): {}", e.getStatusCode(), e.getMessage());
            throw new IllegalStateException("Error al verificar cliente en ClientMS: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            log.error("Error de conexión con el servicio de clientes en {}", url, e);
            throw new IllegalStateException("No se pudo conectar al servicio de clientes", e);
        }
    }

}
