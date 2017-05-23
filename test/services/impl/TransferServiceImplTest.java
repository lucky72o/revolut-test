package services.impl;

import dtos.MoneyTransferResponse;
import models.Currency;
import models.User;
import models.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import services.TransferService;
import services.UserService;

import java.math.BigDecimal;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceImplTest {

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private TransferService transferService = new TransferServiceImpl(userServiceMock);

    @Test
    public void shouldNotProcessIfNotEnoughMoney() throws Exception {
        User fromUser = getUser();
        User toUser = getUser();

        when(userServiceMock.findById(1L)).thenReturn(Optional.of(fromUser));
        when(userServiceMock.findById(2L)).thenReturn(Optional.of(toUser));

        MoneyTransferResponse response = transferService.transferFunds(1L, 2L, Currency.GBP, BigDecimal.valueOf(11));

        assertThat(response.isSuccessful()).isFalse();
        verify(userServiceMock, times(0)).update(any(User.class));
    }

    @Test
    public void shouldNotProcessIfTargetedUserDoesNotHaveAppropriatedWallet() throws Exception {
        User fromUser = getUser();
        User toUser = getUser();
        toUser.getWallet().setCurrency(Currency.EUR);

        when(userServiceMock.findById(1L)).thenReturn(Optional.of(fromUser));
        when(userServiceMock.findById(2L)).thenReturn(Optional.of(toUser));

        MoneyTransferResponse response = transferService.transferFunds(1L, 2L, Currency.GBP, BigDecimal.valueOf(11));

        assertThat(response.isSuccessful()).isFalse();
        verify(userServiceMock, times(0)).update(any(User.class));
    }

    @Test
    public void shouldTransferMoney() throws Exception {
        User fromUser = getUser();
        User toUser = getUser();

        when(userServiceMock.findById(1L)).thenReturn(Optional.of(fromUser));
        when(userServiceMock.findById(2L)).thenReturn(Optional.of(toUser));

        MoneyTransferResponse response = transferService.transferFunds(1L, 2L, Currency.GBP, BigDecimal.valueOf(5));

        assertThat(response.isSuccessful()).isTrue();
        verify(userServiceMock, times(2)).update(any(User.class));
    }

    private User getUser() {
        Wallet wallet = new Wallet(Currency.GBP, BigDecimal.TEN);
        return new User("user-name", wallet);
    }
}