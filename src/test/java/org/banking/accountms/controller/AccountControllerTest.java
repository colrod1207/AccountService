package org.banking.accountms.controller;

import org.banking.accountms.dto.request.CreateAccountRequest;
import org.banking.accountms.dto.response.AccountResponse;
import org.banking.accountms.model.Account;
import org.banking.accountms.model.AccountType;
import org.banking.accountms.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountResponse sampleResponse;
    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleResponse = AccountResponse.builder()
                .id(1L)
                .accountNumber("ACC123")
                .balance(BigDecimal.valueOf(1000))
                .type(AccountType.SAVINGS)
                .clientId(10L)
                .build();

        sampleAccount = Account.builder()
                .id(1L)
                .accountNumber("ACC123")
                .balance(BigDecimal.valueOf(1000))
                .type(AccountType.SAVINGS)
                .clientId(10L)
                .active(true)
                .build();
    }

    @Test
    void testCreate() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientId(10L);
        request.setInitialBalance(BigDecimal.valueOf(1000));
        request.setType(AccountType.SAVINGS);

        when(accountService.createAccount(request)).thenReturn(sampleResponse);

        ResponseEntity<AccountResponse> response = accountController.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ACC123", response.getBody().getAccountNumber());
        verify(accountService, times(1)).createAccount(request);
    }

    @Test
    void testGetById() {
        when(accountService.get(1L)).thenReturn(sampleAccount);

        ResponseEntity<AccountResponse> response = accountController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ACC123", response.getBody().getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getBalance());
        verify(accountService, times(1)).get(1L);
    }

    @Test
    void testListAll() {
        when(accountService.listAll()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<AccountResponse>> response = accountController.listAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(accountService, times(1)).listAll();
    }

    @Test
    void testListByClient() {
        when(accountService.listByClient(10L)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<AccountResponse>> response = accountController.listByClient(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(accountService, times(1)).listByClient(10L);
    }

    @Test
    void testDelete() {
        doNothing().when(accountService).delete(1L);

        ResponseEntity<Void> response = accountController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(accountService, times(1)).delete(1L);
    }

    @Test
    void testDeactivate() {
        when(accountService.deactivate(1L)).thenReturn(sampleResponse);

        ResponseEntity<AccountResponse> response = accountController.deactivate(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ACC123", response.getBody().getAccountNumber());
        verify(accountService, times(1)).deactivate(1L);
    }

    @Test
    void testActivate() {
        when(accountService.activate(1L)).thenReturn(sampleResponse);

        ResponseEntity<AccountResponse> response = accountController.activate(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ACC123", response.getBody().getAccountNumber());
        verify(accountService, times(1)).activate(1L);
    }
}
