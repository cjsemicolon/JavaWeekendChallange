package safeBank.Services;

import safeBank.BankAccount;
import safeBank.InsufficientFundsException;
import safeBank.InvalidAmountException;
import safeBank.WithdrawalLimitExceededException;

public class Withdrawal {
    public String performWithdrawal(BankAccount account, double amount)
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        account.withdraw(amount);

        return "Withdrawal successful.";
    }
}
