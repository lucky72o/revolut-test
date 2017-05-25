package services;

import com.google.inject.ImplementedBy;
import dtos.MoneyTransferResponse;
import models.Currency;
import models.User;
import services.impl.TransferServiceImpl;

import java.math.BigDecimal;

@ImplementedBy(TransferServiceImpl.class)
public interface TransferService {
    MoneyTransferResponse transferFunds(User fromUser, User toUser, Currency currency, BigDecimal amount);
}
