package org.banking.accountms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.model.AccountType;
import org.banking.accountms.service.AccountService;
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

    @Test
    void createAccount_returns200() throws Exception {
        AccountResponse response = new AccountResponse(1L, "SVG-123456", new BigDecimal("100"), AccountType.SAVINGS, 1L, true);
        Mockito.when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        CreateAccountRequest request = new CreateAccountRequest(1L, AccountType.SAVINGS, new BigDecimal("100"));

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("SVG-123456"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void listAll_returns200() throws Exception {
        List<AccountResponse> list = List.of(
                new AccountResponse(1L, "SVG-111111", new BigDecimal("100"), AccountType.SAVINGS, 1L, true),
                new AccountResponse(2L, "CH-222222", new BigDecimal("200"), AccountType.CHECKING, 2L, false)
        );
        Mockito.when(accountService.listAll()).thenReturn(list);

        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("SVG-111111"))
                .andExpect(jsonPath("$[1].active").value(false));
    }

    @Test
    void activateAccount_returns200() throws Exception {
        AccountResponse response = new AccountResponse(1L, "CH-333333", new BigDecimal("0"), AccountType.CHECKING, 1L, true);
        Mockito.when(accountService.activate(1L)).thenReturn(response);

        mockMvc.perform(patch("/cuentas/{id}/activate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void deactivateAccount_returns200() throws Exception {
        AccountResponse response = new AccountResponse(1L, "CH-444444", new BigDecimal("0"), AccountType.CHECKING, 1L, false);
        Mockito.when(accountService.deactivate(1L)).thenReturn(response);

        mockMvc.perform(patch("/cuentas/{id}/deactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
