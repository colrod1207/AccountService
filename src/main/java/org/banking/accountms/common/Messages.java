package org.banking.accountms.common;

public final class Messages {

    private Messages() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ========= General =========
    public static final String INTERNAL_ERROR = "Error interno del servidor";

    // ========= Account =========
    public static final String ACCOUNT_NOT_FOUND = "Cuenta no encontrada";
    public static final String ACCOUNT_ALREADY_ACTIVE = "La cuenta ya está activa";
    public static final String ACCOUNT_ALREADY_INACTIVE = "La cuenta ya está inactiva";
    public static final String ACCOUNT_BALANCE_NOT_ZERO = "No se puede eliminar una cuenta con saldo distinto de 0";
    public static final String ACCOUNT_NUMBER_GENERATION_FAILED = "El sistema no pudo generar un número de cuenta válido";

    // ========= Client =========
    public static final String CLIENT_NOT_FOUND = "El cliente no existe";

    // ========= Validation =========
    public static final String BALANCE_MUST_BE_POSITIVE = "El saldo inicial debe ser mayor que 0.";
    public static final String INVALID_FORMAT = "Formato de datos inválido.";
    public static final String INVALID_ACCOUNT_TYPE = "Valor inválido para el campo 'type'. Valores permitidos: SAVINGS, CHECKING.";
}