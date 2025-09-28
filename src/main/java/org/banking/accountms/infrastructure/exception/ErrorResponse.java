package org.banking.accountms.infrastructure.exception;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(name = "ErrorResponse", description = "Estructura estándar de errores")
public class ErrorResponse {

    @Schema(description = "Momento del error", example = "2025-09-28T15:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Descripción del error HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje detallado del error", example = "El saldo inicial debe ser mayor que 0")
    private String message;

    @Schema(description = "Ruta del request que causó el error", example = "/cuentas")
    private String path;
}
