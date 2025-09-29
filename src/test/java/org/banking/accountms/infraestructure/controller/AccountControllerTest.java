package org.banking.accountms.infraestructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.banking.accountms.application.service.AccountService;
import org.banking.accountms.domain.model.AccountType;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.infrastructure.controller.AccountController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountResponse accountResponse;

    @BeforeEach
    void setUp() {
        accountResponse = new AccountResponse(
                1L,
                "ACC123456",
                BigDecimal.valueOf(1000),
                AccountType.SAVINGS,
                10L,
                true
        );
    }

    @Test
    void shouldCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(
                10L,
                AccountType.SAVINGS,
                BigDecimal.valueOf(1000)
        );

        when(accountService.createAccount(any(CreateAccountRequest.class)))
                .thenReturn(accountResponse);

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.clientId").value(10));
    }

    @Test
    void shouldGetAccountById() throws Exception {
        when(accountService.get(1L)).thenReturn(accountResponse);

        mockMvc.perform(get("/cuentas/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"));
    }

    @Test
    void shouldListAllAccounts() throws Exception {
        when(accountService.listAll()).thenReturn(List.of(accountResponse));

        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldListAccountsByClient() throws Exception {
        when(accountService.listByClient(10L)).thenReturn(List.of(accountResponse));

        mockMvc.perform(get("/cuentas/clientes/{clientId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(10));
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mockMvc.perform(delete("/cuentas/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(accountService).delete(1L);
    }

    @Test
    void shouldActivateAccount() throws Exception {
        when(accountService.activate(1L)).thenReturn(accountResponse);

        mockMvc.perform(patch("/cuentas/{id}/activate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldDeactivateAccount() throws Exception {
        AccountResponse inactiveAccount = new AccountResponse(
                1L,
                "ACC123456",
                BigDecimal.valueOf(1000),
                AccountType.SAVINGS,
                10L,
                false
        );
        when(accountService.deactivate(1L)).thenReturn(inactiveAccount);

        mockMvc.perform(patch("/cuentas/{id}/deactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
