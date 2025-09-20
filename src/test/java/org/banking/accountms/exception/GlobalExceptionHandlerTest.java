package org.banking.accountms.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.banking.accountms.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Cuenta no encontrada");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().getCode());
    }

    @Test
    void testHandleValidation() {
        ValidationException ex = new ValidationException("Validación fallida");
        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }

    @Test
    void testHandleIllegalState() {
        IllegalStateException ex = new IllegalStateException("Servicio caído");
        ResponseEntity<ErrorResponse> response = handler.handleIllegalState(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("SERVICE_UNAVAILABLE", response.getBody().getCode());
    }

    @Test
    void testHandleGeneral() {
        Exception ex = new Exception("Error inesperado");
        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().getCode());
    }

    @Test
    void testHandleInvalidFormatWithEnum() {
        InvalidFormatException cause = new InvalidFormatException(
                null, "valor", "invalid", AccountType.class
        );
        HttpMessageConversionException ex = new HttpMessageConversionException("Formato inválido", cause);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidFormat(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Valores permitidos"));
    }


    @Test
    void testHandleInvalidFormatGeneric() {
        HttpMessageConversionException ex =
                new HttpMessageConversionException("Formato inválido");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidFormat(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("INVALID_FORMAT", response.getBody().getCode());
    }

}
