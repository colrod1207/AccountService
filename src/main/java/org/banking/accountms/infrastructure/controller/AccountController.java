package org.banking.accountms.infrastructure.controller;

import java.util.List;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.application.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts", description = "Operations related to bank accounts")
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Crear una nueva cuenta")
    @ApiResponse(responseCode = "200", description = "Cuenta creada con éxito",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class)))
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @Operation(summary = "Obtener cuenta por ID")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class)))
    @GetMapping("/id/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.get(id));
    }

    @Operation(summary = "Listar todas las cuentas")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida")
    @GetMapping
    public ResponseEntity<List<AccountResponse>> listAll() {
        return ResponseEntity.ok(accountService.listAll());
    }

    @Operation(summary = "Listar cuentas por cliente ID")
    @ApiResponse(responseCode = "200", description = "Cuentas obtenidas por cliente")
    @GetMapping("/clientes/{clientId}")
    public ResponseEntity<List<AccountResponse>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.listByClient(clientId));
    }

    @Operation(summary = "Eliminar cuenta por ID")
    @ApiResponse(responseCode = "204", description = "Cuenta eliminada con éxito")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar cuenta")
    public ResponseEntity<AccountResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.deactivate(id));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activar cuenta")
    public ResponseEntity<AccountResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.activate(id));
    }
}
