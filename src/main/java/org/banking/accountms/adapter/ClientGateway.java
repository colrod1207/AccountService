package org.banking.accountms.adapter;

import lombok.AllArgsConstructor;
import org.banking.accountms.config.ClientProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class ClientGateway {

    private final RestTemplate restTemplate;
    private final ClientProperties clientProperties;

    public boolean exists(Long clientId) {
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(
                    clientProperties.getServiceUrl() + "/clientes/" + clientId,
                    Void.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            if (e.getStatusCode().is4xxClientError()) {
                return false;
            }
            throw e;
        } catch (ResourceAccessException e) {
            throw new IllegalStateException("No se pudo conectar al servicio de clientes", e);
        }
    }
}
