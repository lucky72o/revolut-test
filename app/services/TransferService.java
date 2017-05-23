package services;

import com.google.inject.ImplementedBy;
import dtos.MoneyTransferResponse;
import models.Currency;
import services.impl.TransferServiceImpl;

import java.math.BigDecimal;

@ImplementedBy(TransferServiceImpl.class)
public interface TransferService {
    MoneyTransferResponse transferFunds(Long fromUserId, Long toUserId, Currency currency, BigDecimal amount);
}
