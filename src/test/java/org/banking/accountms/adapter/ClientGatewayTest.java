package org.banking.accountms.adapter;

import org.banking.accountms.config.ClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientGatewayTest {

    private RestTemplate restTemplate;
    private ClientProperties clientProperties;
    private ClientGateway clientGateway;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        clientProperties = mock(ClientProperties.class);
        clientGateway = new ClientGateway(restTemplate, clientProperties);

        when(clientProperties.getServiceUrl()).thenReturn("http://fake-service");
    }

    @Test
    void exists_returnsTrue_whenClientExists() {
        when(restTemplate.getForEntity("http://fake-service/clientes/1", Void.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        boolean result = clientGateway.exists(1L);

        assertTrue(result);
    }

    @Test
    void exists_returnsFalse_whenNotFound() {
        when(restTemplate.getForEntity("http://fake-service/clientes/2", Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        boolean result = clientGateway.exists(2L);

        assertFalse(result);
    }

    @Test
    void exists_returnsFalse_whenOther4xx() {
        when(restTemplate.getForEntity("http://fake-service/clientes/3", Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        boolean result = clientGateway.exists(3L);

        assertFalse(result);
    }

    @Test
    void exists_throwsException_whenResourceAccessFails() {
        when(restTemplate.getForEntity("http://fake-service/clientes/4", Void.class))
                .thenThrow(new ResourceAccessException("connection failed"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> clientGateway.exists(4L)
        );

        assertTrue(ex.getMessage().contains("No se pudo conectar al servicio de clientes"));
    }

    @Test
    void exists_propagatesException_whenUnexpectedError() {
        when(restTemplate.getForEntity("http://fake-service/clientes/5", Void.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpClientErrorException.class, () -> clientGateway.exists(5L));
    }
}
