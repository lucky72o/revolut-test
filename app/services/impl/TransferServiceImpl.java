package services.impl;

import dtos.MoneyTransferResponse;
import models.Currency;
import models.User;
import models.Wallet;
import services.TransferService;
import services.UserService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferServiceImpl implements TransferService {

    private UserService userService;

    @Inject
    public TransferServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public MoneyTransferResponse transferFunds(Long fromUserId, Long toUserId, Currency currency, BigDecimal amount) {

        // Basic solution to prevent race condition.
        lock.lock();

        try {
            // fetch users again to get actual wallet balances
            User fromUser = userService.findById(fromUserId).get();
            User toUser = userService.findById(toUserId).get();

            if (validateTransferAmountIsAvailable(fromUser, currency, amount)) {
                if (validateTargetedUserHasAppropriateWallet(toUser, currency)) {

                    transfer(fromUser, toUser, amount);
                    return new MoneyTransferResponse(true, "Money transfer was successful.");

                } else {
                    return new MoneyTransferResponse(false, String.format("User with id: {%d} doesn't have a wallet supporting currency: {%s}",
                            fromUserId, currency));
                }
            }


            return new MoneyTransferResponse(false, String.format("User with id: {%d} doesn't have enough amount: {%f} of currency: {%s}",
                    fromUserId, amount, currency));

        } finally {
            lock.unlock();
        }
    }

    private void transfer(User fromUser, User toUser, BigDecimal amount) {
        BigDecimal initialFromUserAmount = fromUser.getWallet().getAmount();
        BigDecimal initialToUserAmount = toUser.getWallet().getAmount();

        fromUser.getWallet().setAmount(initialFromUserAmount.subtract(amount));
        toUser.getWallet().setAmount(initialToUserAmount.add(amount));

        userService.update(fromUser);
        userService.update(toUser);
    }

    private boolean validateTargetedUserHasAppropriateWallet(User user, Currency currency) {
        Wallet wallet = user.getWallet();
        return currency.equals(wallet.getCurrency());
    }

    private boolean validateTransferAmountIsAvailable(User user, Currency currency, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        return currency.equals(wallet.getCurrency()) && wallet.getAmount().compareTo(amount) >= 0;
    }
}
