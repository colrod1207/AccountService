package org.banking.accountms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ErrorResponse response = new ErrorResponse("CODE123", "Mensaje de error");

        assertEquals("CODE123", response.getCode());
        assertEquals("Mensaje de error", response.getMessage());
    }

    @Test
    void testSetters() {
        ErrorResponse response = new ErrorResponse("INIT", "init msg");
        response.setCode("CODE456");
        response.setMessage("Otro mensaje");

        assertEquals("CODE456", response.getCode());
        assertEquals("Otro mensaje", response.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponse r1 = new ErrorResponse("C1", "msg");
        ErrorResponse r2 = new ErrorResponse("C1", "msg");
        ErrorResponse r3 = new ErrorResponse("C2", "otro");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
    }

    @Test
    void testToString() {
        ErrorResponse response = new ErrorResponse("C2", "mensaje");
        String str = response.toString();

        assertTrue(str.contains("C2"));
        assertTrue(str.contains("mensaje"));
    }
}
